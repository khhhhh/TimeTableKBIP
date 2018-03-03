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
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;


import by.kbp.timetabledesign2.Lecture;
import by.kbp.timetabledesign2.R;
import by.kbp.timetabledesign2.RVAdapter;


public class DayFragment extends Fragment {

    SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Lecture> lectures;
    private boolean currentInfo;
    private TextView textView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        lectures = getArguments().getParcelableArrayList("lectures");
        currentInfo = getArguments().getBoolean("currentInfo");
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        //Если что, при каждой смене дня недели используя TabLayout запускается метод onCreateView


        textView = (TextView) view.findViewById(R.id.relevance_text);

        if(currentInfo)
            textView.setText("Актуально");
        else
            textView.setVisibility(View.GONE);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(lectures);
        rv.setAdapter(adapter);


        return view;
    }

    public static DayFragment newInstance(ArrayList<Lecture> lecture, boolean currentInfo) {
        DayFragment dayFragment = new DayFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList("lectures", lecture);
        args.putBoolean("currentInfo",currentInfo);
        dayFragment.setArguments(args);

        return dayFragment;
    }
}