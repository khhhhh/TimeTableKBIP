package layout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import by.kbp.timetabledesign2.R;
import java.util.Calendar;
import java.util.Date;


public class DaysFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle   savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_days, container, false);

        setupViewPager((ViewPager) view.findViewById(R.id.frame), (TabLayout) view.findViewById(R.id.tabs));
        return view;
    }


    private void setupViewPager(ViewPager viewPager, TabLayout tabLayout){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new DayFragment());
        adapter.addFragment(new DayFragment());
        adapter.addFragment(new DayFragment());
        adapter.addFragment(new DayFragment());
        adapter.addFragment(new DayFragment());
        adapter.addFragment(new DayFragment());
        viewPager.setAdapter(adapter);

        Date date = new Date();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        tabLayout.setScrollPosition(dayOfWeek-2,0f,true);
        viewPager.setCurrentItem(dayOfWeek-2);

        tabLayout.setupWithViewPager(viewPager);
    }

}
