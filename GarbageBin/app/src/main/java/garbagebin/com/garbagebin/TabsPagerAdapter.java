package garbagebin.com.garbagebin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.HotGagsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new Home_Fragment();
		case 1:
			// Games fragment activity
			return new HotGagsFragment();
		case 2:
			// Movies fragment activity
			return new HotGagsFragment()
					;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
