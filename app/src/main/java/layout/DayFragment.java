package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import by.kbp.timetabledesign2.Lecture;
import by.kbp.timetabledesign2.R;
import by.kbp.timetabledesign2.RVAdapter;


public class DayFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;

    private List<Lecture> lectures;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        SwipeRefresh(view);

        initializeData();


        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(lectures);
        rv.setAdapter(adapter);

        return view;
    }


    private void SwipeRefresh(View view){
        swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: Add refresh here
            }
        });
    }


    private void initializeData(){
        lectures = new ArrayList<>();
        lectures.add(new Lecture("Emma Wilson", "23 years old", "Lavery Maiss", "25 years old"));
        lectures.add(new Lecture("Lavery Maiss", "25 years old", "Emma Wilson", "23 years old"));
        lectures.add(new Lecture("Lillie Watts", "35 years old", "Lillie Watts", "35 years old"));
    }

}
