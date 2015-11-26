package garbagebin.com.garbagebin;

import android.app.Activity;
import android.os.Bundle;

public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.setTabTextColors(getResources().getColor(R.color.light_grey),getResources().getColor(R.color.orange));
//        tabLayout.addTab(tabLayout.newTab().setText("ACTIVITIES"));
//        tabLayout.addTab(tabLayout.newTab().setText("ORDERS"));
//        tabLayout.addTab(tabLayout.newTab().setText("POINTS"));
//        tabLayout.addTab(tabLayout.newTab().setText("INVITE"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter
//                (getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

}
