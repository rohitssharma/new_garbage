package garbagebin.com.garbagebin;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.garbagebin.fragments.Cart_Fragment;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.HotGagsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Home_Fragment tab1 = new Home_Fragment();
                return tab1;
            case 1:
                HotGagsFragment tab2 = new HotGagsFragment();
                return tab2;
            case 2:
                Cart_Fragment tab3 = new Cart_Fragment();
                return tab3;
            case 3:
                Cart_Fragment tab4 = new Cart_Fragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}