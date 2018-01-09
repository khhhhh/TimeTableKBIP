package layout;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


import by.kbp.timetabledesign2.Lecture;
import by.kbp.timetabledesign2.R;
import by.kbp.timetabledesign2.RVAdapter;


public class DayFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Lecture> lectures;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        lectures = getArguments().getParcelableArrayList("lectures");
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        //Если что, при каждой смене дня недели используя TabLayout запускается метод onCreateView



        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(lectures);
        rv.setAdapter(adapter);

        SwipeRefresh(view);

        return view;
    }

    public static DayFragment newInstance(ArrayList<Lecture> lecture, int i) {
        DayFragment dayFragment = new DayFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("lectures", lecture);
        args.putInt("i", i);
        dayFragment.setArguments(args);

        return dayFragment;
    }

    private void SwipeRefresh(View view){
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: Add refresh here
            }
        });
    }






}