package layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;

import java.util.ArrayList;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> fragments = new ArrayList<>();
    public SectionsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment f){
        fragments.add(f);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Пондельник";
            case 1:
                return "Вторник";
            case 2:
                return "Среда";
            case 3:
                return "Четверг";
            case 4:
                return "Пятница";
            case 5:
                return "Суббота";
        }
        return null;
    }
}
