package by.kbp.timetabledesign2;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import layout.DayFragment;
import layout.SectionsPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private Document HtmlData;
    private CustomSwipe swipeContainer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Spinner spinner;
    private SectionsPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.frame);
        spinner = (Spinner) findViewById(R.id.spinner);
        swipeContainer = (CustomSwipe) findViewById(R.id.swipe_refresh);


        //Проверка на наличие интернета
        if(!InternetConnected()){
            Snackbar.make(viewPager, "Отсутствует подключение к интернету", Snackbar.LENGTH_LONG).show();
        }

        //подключение к сайту
        connectToSite();

        //Добавляем поиск
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Выбираем текущую неделю
        setupSpinner(HtmlData);



        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupViewPager(spinner.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        swipeContainer.setColorSchemeColors(Color.parseColor("#00796b"));
        swipeContainer.setOnRefreshListener(new CustomSwipe.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        connectToSite();
                        int i = spinner.getSelectedItemPosition();
                        setupSpinner(HtmlData);
                        if(spinner.getSelectedItemPosition() == i){
                            setupViewPager(spinner.getSelectedItemPosition());
                        }
                        swipeContainer.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }


    //Проверка интернета
    private boolean InternetConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    void connectToSite(){
        HtmlData = null;
        NewThread AsyncParse = new NewThread(); // Объект асинхронного потока
        AsyncParse.execute(receiveSearchWord()); // Запуск асинхронного потока (Работа с сетью с версии android 3.0 работает только в отдельном потоке при попытке взаимодействовать с сетью в UI потоке - error)
        try {
            HtmlData = AsyncParse.get(); // Получаю возвращаемые данные из асинхронного потока (Для этого - запускается поток, затем используется метод .get)
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    //Настраиваем поиск
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        saveSearchWord(searchView.getQuery().toString());
                        connectToSite();
                        int i = spinner.getSelectedItemPosition();
                        setupSpinner(HtmlData);
                        if(spinner.getSelectedItemPosition() == i){
                            setupViewPager(spinner.getSelectedItemPosition());
                        }
                        item.collapseActionView();
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );
        return super.onCreateOptionsMenu(menu);
    }


    public void setupViewPager(int week){
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        adapter = new SectionsPagerAdapter(fm);

        for(int i = 1; i < 7; i++) {
            adapter.addFragment(DayFragment.newInstance(SetData(HtmlData, i, week), setRelevanceInfo(HtmlData, i, week), i));
        }
        if(HtmlData.select("table").isEmpty() && InternetConnected()) {
            Snackbar.make(findViewById(R.id.frame), "Ничего не найдено!", Snackbar.LENGTH_SHORT).show();
        }
        viewPager.setAdapter(adapter);

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        tabLayout.setScrollPosition(dayOfWeek-2,0f,true);
        viewPager.setCurrentItem(dayOfWeek-2);

        tabLayout.setupWithViewPager(viewPager);

    }

    public class NewThread extends AsyncTask<String, Void, Document> //<Входные, Промежуточные, Возвращаемые>Данные (Для работы в асинхронном потоке)
    {
        @Override
        protected Document doInBackground(String... args)
        {
            Document doc;
            try {
                doc = Jsoup.connect("https://kbp.by/rasp/timetable/view_beta_tbp/?q="+args[0]).get();
                savePage(doc.toString());
            } catch (IOException e1) {
                doc = Jsoup.parse(receivePage());
            }
            return doc;
        }

    }

    private boolean setRelevanceInfo(Document doc, int Day, int Week){
        if(doc == null || doc.select("table").isEmpty())
            return false;

        Element table = doc.select("table").get(Week); //Выбор левой таблицы(0) /  Правой (1)
        Elements rows = table.select("tr"); //9-ь строк (0-день недели(th); 1-замены(th); 2,3..8 - пары(td))
        //
        Element rowZamena = rows.get(1);
        Elements Ths = rowZamena.select("th");
        Element th = Ths.get(Day);
        return !th.text().equals("");
    }


    private void setupSpinner(Document doc){
        if(doc == null || doc.select("table").isEmpty())
            return;
        Element today = doc.selectFirst("p.today");

        int i = today.text().equals("первая неделя")? 0 : 1;
        spinner.setSelection(1 - i);
        spinner.setSelection(i);
    }

    private ArrayList<Lecture> SetData(Document doc, int Day, int Week) {
        ArrayList<Lecture> lectures = new ArrayList<>();

        if(doc == null || doc.select("table").isEmpty()) {
            return lectures;
        }

        Element[] group;
        Element[] places;
        Element[] teachers;
        Element subj;
        int number = 1;

        Element table = doc.select("table").get(Week); //Выбор левой таблицы(0) /  Правой (1)
        Elements rows = table.select("tr"); //9-ь строк (0-день недели(th); 1-замены(th); 2,3..8 - пары(td))



        for (int RowInd = 2; RowInd < 9; RowInd = RowInd + 1, number++) {
            Element row = rows.get(RowInd); //  получить все ячейки по горизонтали x строки в элемент
            //th
            Elements cell = row.select("td"); // Все ячейки по горизонтали x строки в массив
            Element isAdded = cell.get(Day); // Пара[RowInd] для дня [Day]
            Elements isAddedSubj = isAdded.select("div.pair.added"); //  Size 2/3/4 = если были замены , size = 1 - если замен нет. Size = 0 - если пар вообще нет


            if(isAddedSubj.size() == 0)
                isAddedSubj = isAdded.select("div.pair:not(.removed)");

            if (isAddedSubj.size() > 0) {
                places = new Element[isAddedSubj.size()];
                teachers = new Element[isAddedSubj.size()];
                group = new Element[isAddedSubj.size()];
                for (int i = 0; i < isAddedSubj.size(); i++) {

                    teachers[i] = isAddedSubj.select(".teacher").get(i == 1 ? 2 : i);
                    places[i] = isAddedSubj.select(".place").get(i);
                    group[i] = isAddedSubj.select(".group").get(i);
                }
                subj = isAddedSubj.select(".subject").get(isAddedSubj.size() - 1);
                if (isAddedSubj.size() >= 2)
                    lectures.add(new Lecture(subj.text(),
                            teachers[0].text() + "/" + teachers[1].text(),
                            places[0].text()+ "/" + places[1].text(),
                            group[0].text()+ "/" + group[1].text(),
                            String.valueOf(number)));
                else if(isAddedSubj.size() == 1)
                    lectures.add(new Lecture(subj.text(), teachers[0].text(),
                            places[0].text(), group[0].text(),String.valueOf(number)));
            }else {
                lectures.add(new Lecture("Пары нет", " ", " ", " ",  String.valueOf(number)));
            }
        }
        return  lectures;
    }



    void saveSearchWord(String search){
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("SEARCH", search);
        editor.apply();
    }

    String receiveSearchWord() {
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        return preferences.getString("SEARCH","Т-617");
    }

    void savePage(String page){
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PAGE", page);
        editor.apply();
    }

    String receivePage() {
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        return preferences.getString("PAGE",null);
    }
}
