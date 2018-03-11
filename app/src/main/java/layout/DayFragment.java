package layout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;


import by.kbp.timetabledesign2.Lecture;
import by.kbp.timetabledesign2.R;
import by.kbp.timetabledesign2.RVAdapter;


public class DayFragment extends Fragment {
    private ArrayList<Lecture> lectures;
    private boolean currentInfo;
    private TextView textView;
    private int day;
    private SparseArray<String> dictionary = new SparseArray<String>();
    private SparseArray<String> dictionaryS = new SparseArray<String>();
    private RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        lectures = getArguments().getParcelableArrayList("lectures");
        day = getArguments().getInt("day");
        currentInfo = getArguments().getBoolean("currentInfo");

        dictionary.put(0,"8:30 - 10:00");
        dictionary.put(1,"10:10 - 11:40");
        dictionary.put(2,"12:20 - 13:50");
        dictionary.put(3,"14:10 - 15:40");
        dictionary.put(4,"15:50 - 17:20");
        dictionary.put(5,"17:30 - 19:00");
        dictionary.put(6,"19:10 - 20:30");

        dictionaryS.put(0,"8:30 - 10:00");
        dictionaryS.put(1,"10:10 - 11:40");
        dictionaryS.put(2,"11:50 - 13:20");
        dictionaryS.put(3,"13:30 - 15:00");
        dictionaryS.put(4,"15:10 - 16:40");
        dictionaryS.put(5,"16:50 - 18:20");
        dictionaryS.put(6,"18:30 - 20:00");


        if(lectures.size() != 0)
            for(int i = 0; i < 7; i++)
                lectures.get(i).Time = day == 6 ? dictionaryS.get(i) : dictionary.get(i);


        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        textView = (TextView) view.findViewById(R.id.relevance_text);
        rv = (RecyclerView)view.findViewById(R.id.rv);

        if(currentInfo)
            textView.setText("Актуально");
        else
            textView.setVisibility(View.GONE);

        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(lectures);
        rv.setAdapter(adapter);

        return view;
    }

    public static DayFragment newInstance(ArrayList<Lecture> lecture, boolean currentInfo, int day) {
        DayFragment dayFragment = new DayFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("lectures", lecture);
        args.putBoolean("currentInfo",currentInfo);
        args.putInt("day",day);
        dayFragment.setArguments(args);

        return dayFragment;
    }
}