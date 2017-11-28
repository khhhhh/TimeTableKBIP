package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import by.kbp.timetabledesign2.R;



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

        tabLayout.setupWithViewPager(viewPager);
    }
}
