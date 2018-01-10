package layout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import by.kbp.timetabledesign2.Lecture;
import by.kbp.timetabledesign2.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class DaysFragment extends Fragment {


    private ArrayList<Lecture> lectures;

    private Document HtmlData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        HtmlData = null;
        NewThread AsyncParse = new NewThread(); // Объект асинхронного потока
        AsyncParse.execute(); // Запуск асинхронного потока (Работа с сетью с версии android 3.0 работает только в отдельном потоке при попытке взаимодействовать с сетью в UI потоке - error)
        try {
            HtmlData = AsyncParse.get(); // Получаю возвращаемые данные из асинхронного потока (Для этого - запускается поток, затем используется метод .get)
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_days, container, false);



        //Добавляем поиск
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager((ViewPager) view.findViewById(R.id.frame), (TabLayout) view.findViewById(R.id.tabs));
        return view;
    }

    //Настраиваем поиск
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        searchView.onActionViewCollapsed();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
        super.onCreateOptionsMenu(menu, inflater);
    }




    private void setupViewPager(ViewPager viewPager, TabLayout tabLayout){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        for(int i = 1; i < 7; i++)
            adapter.addFragment(DayFragment.newInstance(SetData(HtmlData, i),i));
        viewPager.setAdapter(adapter);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        tabLayout.setScrollPosition(dayOfWeek-2,0f,true);
        viewPager.setCurrentItem(dayOfWeek-2);

        tabLayout.setupWithViewPager(viewPager);
    }


    public class NewThread extends AsyncTask<Void, Void, Document> //<Входные, Промежуточные, Возвращаемые>Данные (Для работы в асинхронном потоке)
    {
        @Override
        protected Document doInBackground(Void... args)
        {
            Document doc = null;
            try {
                doc = Jsoup.connect("https://kbp.by/rasp/timetable/view_beta_tbp/?cat=group&id=133").get();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return doc;
        }

    }

    private ArrayList<Lecture> SetData(Document doc, int Day) {
        // TODO
        //https://kbp.by/rasp/timetable/view_beta_tbp/?q=           <---[GET]Поиск

        ArrayList<Lecture> lectures = new ArrayList<>();
        Element group = null;
        Element place = null;
        Element teacher = null;
        Element subj = null;

        Element table = doc.select("table").get(0); //Выбор левой таблицы(0) /  Правой (1)
        Elements rows = table.select("tr"); //9-ь строк (0-день недели(th); 1-замены(th); 2,3..8 - пары(td))

        for (int RowInd = 2; RowInd < 9; RowInd = RowInd + 1) {
            Element row = rows.get(RowInd); //  получить все ячейки по горизонтали x строки в элемент
            //th
            Elements cell = row.select("td"); // Все ячейки по горизонтали x строки в массив
            Element isAdded = cell.get(Day); // Пара[RowInd] для дня [Day]
            Elements isAddedSubj = isAdded.select("div.pair"); //  Size 2/3/4 = если были замены , size = 1 - если замен нет. Size = 0 - если пар вообще нет

            if (isAddedSubj.size() != 0) {
                subj = isAddedSubj.select(".subject").get(isAddedSubj.size() / 2);
                teacher = isAddedSubj.select(".teacher").get(isAddedSubj.size() / 2);
                if(teacher.text() == "")
                teacher = isAddedSubj.select(".teacher").get(isAddedSubj.size());
                place = isAddedSubj.select(".place").get(isAddedSubj.size() / 2);
                group = isAddedSubj.select(".group").get(isAddedSubj.size() / 2);

            }
            if (isAddedSubj.size() == 0)
                lectures.add(new Lecture("Пара снята", " ", " ", " "));
            else
                lectures.add(new Lecture(subj.text(), teacher.text(), place.text(), group.text()));
        }
        return  lectures;
    }


}
