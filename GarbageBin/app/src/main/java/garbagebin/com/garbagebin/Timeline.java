package garbagebin.com.garbagebin;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.garbacgebin.sliderlibrary.RightSlideMenuFunctions;
import com.garbacgebin.sliderlibrary.SlideMenuFunctions;
import com.garbacgebin.sliderlibrary.SlideMenuModels;
import com.garbacgebin.sliderlibrary.SliderAdapter;
import com.garbacgebin.sliderlibrary.SlidingMenu;
import com.garbagebin.Utils.CommonUtils;
import com.garbagebin.fragments.AboutUsFragment;
import com.garbagebin.fragments.Cart_Fragment;
import com.garbagebin.fragments.Characters_Fragment;
import com.garbagebin.fragments.Comics_Fragment;
import com.garbagebin.fragments.Featured_Posts_Fragment;
import com.garbagebin.fragments.FeedBack_Fragment;
import com.garbagebin.fragments.HelpFragment;
import com.garbagebin.fragments.Home_Fragment;
import com.garbagebin.fragments.HotGagsFragment;
import com.garbagebin.fragments.Invite_Referral_Fragment;
import com.garbagebin.fragments.NotificationFragment;
import com.garbagebin.fragments.Points_Badges_Fragment;
import com.garbagebin.fragments.Search_Fragment;
import com.garbagebin.fragments.Settings_Fragment;
import com.garbagebin.fragments.Terms_Conditions_Fragment;
import com.garbagebin.fragments.UserProfileFragment;
import com.garbagebin.fragments.VersionFragment;
import com.garbagebin.fragments.Videos_Fragment;
import com.garbagebin.fragments.WalletFragment;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

public class Timeline extends FragmentActivity implements View.OnClickListener {

    RelativeLayout main_frag_layout;
    public static    SlidingMenu menu, right_menu;
    SlideMenuFunctions menufunctions;
    RightSlideMenuFunctions rightMenuFunctions;
    Activity activity;
    public static  TextView header_textview,notification_counter_tv;
    public static  LinearLayout menu_icon_layout, profile_pic_layout, options_layout,
            cart_layout,search_layout,home_layout,videos_layout,hotgags_layout,back_icon_layout,
            submenu_icon_layout,settings_layout,notification_layout;

    public static  ImageView hot_gags_imageView,videos_imageView,home_imageView,cart_imageView,search_imageView;
    Fragment fragment;
    public static FragmentManager fm;
    FragmentTransaction ft;
    public static View headerView,bottom,right_drawer,left_drawer;
    public static ImageView timeline_tutorial_imageView,profile_imageView;

    //...................Hover................
    Context context;
    LinearLayout flyt_blurimg;
    public static ImageView fabIconNew;
    public static FloatingActionMenu rightLowerMenu;
    public static FloatingActionButton rightLowerButton;
    boolean left_menu_flag= false;
    String profile_image="",customer_id="";
    SharedPreferences sharedPreferences;
    Uri uri;
    public static Bitmap bmp;

    public static LinearLayout progress_lyt;

    //............drawer layout.....................
    DrawerLayout drawerLayout;
    public static SliderAdapter adapter;
    ListView products_list;
    ArrayList<SlideMenuModels> al;
    public static ImageView slider_profile_pic;
    public static TextView slider_profile_name;
    ImageView logo_imageView;
ActionBarDrawerToggle drawerToggle;
    String username="",fromwhere="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        activity = Timeline.this;
        context = Timeline.this;
        // set app icon to behave as action to toggle navigation drawer
        // getActionBar().setDisplayHomeAsUpEnabled(true);


        sharedPreferences = getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        customer_id = sharedPreferences.getString("customer_id", "");
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
        fromwhere =  sharedPreferences.getString("fromwhere","");
        profile_image =  sharedPreferences.getString("profile_image", "");


        headerView = findViewById(R.id.header_lyt);
        headerView.setVisibility(View.GONE);

        right_drawer = findViewById(R.id.right_drawer);
        left_drawer= findViewById(R.id.left_drawer);
        bottom = findViewById(R.id.bottom);

        //  main_frag_layout = (RelativeLayout)(findViewById(R.id.main_frag_layout));

//        //........... open Left slider ........................
//        menu = CommonUtils.SetLeftSlidingMenu(Timeline.this);
//        menufunctions = new SlideMenuFunctions(activity, Timeline.this, menu);
//        menu.addIgnoredView(main_frag_layout);
//
//        //........... open Right slider ........................
//        right_menu = CommonUtils.SetRightSlidingMenu(Timeline.this);
//        rightMenuFunctions = new RightSlideMenuFunctions(activity, Timeline.this, right_menu);
//        right_menu.addIgnoredView(main_frag_layout);

        //...............................................
        flyt_blurimg = (LinearLayout)(findViewById(R.id.flyt_blurimg));
        flyt_blurimg.setOnClickListener(this);

        // Set up the white button on the lower right corner
        // more or less with default parameter

//        fabIconNew = new ImageView(context);
//        fabIconNew.setBackgroundDrawable(getResources().getDrawable(R.drawable.infinity));
//
//        rightLowerButton = new FloatingActionButton.Builder(context)
//                .setContentView(fabIconNew).setPosition(FloatingActionButton.POSITION_BOTTOM_RIGHT)
//                .build();
//
//        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(context);
//        //  ImageView rlIcon1 = new ImageView(context);
//        ImageView rlIcon1 = new ImageView(context);
//        ImageView rlIcon2 = new ImageView(context);
//        ImageView rlIcon3 = new ImageView(context);
//        ImageView rlIcon4 = new ImageView(context);
//        ImageView rlIcon5 = new ImageView(context);
//
//        rlIcon1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(rightLowerMenu.isOpen())
//                    rightLowerMenu.close(true);
//                menuClosed();
//                ft = fm.beginTransaction();
//                fragment = new Featured_Posts_Fragment();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
//                return false;
//            }
//        });
//        rlIcon2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Toast.makeText(context, "notification_hover", Toast.LENGTH_SHORT).show();
//                if(rightLowerMenu.isOpen())
//                    rightLowerMenu.close(true);
//                menuClosed();
//                return false;
//            }
//        });
//
//        rlIcon3.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Toast.makeText(context,"archive_hover",Toast.LENGTH_SHORT).show();
//                if(rightLowerMenu.isOpen())
//                    rightLowerMenu.close(true);
//                menuClosed();
//                return false;
//            }
//        });
//        rlIcon4.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(rightLowerMenu.isOpen())
//                    rightLowerMenu.close(true);
//                menuClosed();
//
//                ft = fm.beginTransaction();
//                fragment = new Characters_Fragment();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
//                return false;
//            }
//        });
//        rlIcon5.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(rightLowerMenu.isOpen())
//                    rightLowerMenu.close(true);
//                menuClosed();
//                ft = fm.beginTransaction();
//                fragment = new Comics_Fragment();
//                ft.replace(R.id.content_frame, fragment);
//                ft.commit();
//                return false;
//            }
//        });
//
//        // rlIcon1.setBackgroundDrawable(getResources().getDrawable(R.drawable.wallet_hover));
//        rlIcon1.setBackgroundDrawable(getResources().getDrawable(R.drawable.feature_post_hover));
//        rlIcon2.setBackgroundDrawable(getResources().getDrawable(R.drawable.notification_hover));
//        rlIcon3.setBackgroundDrawable(getResources().getDrawable(R.drawable.archive_hover));
//        rlIcon4.setBackgroundDrawable(getResources().getDrawable(R.drawable.characters_hover));
//        rlIcon5.setBackgroundDrawable(getResources().getDrawable(R.drawable.comics_hover));
//
//        // Build the menu with default options: light theme, 90 degrees, 72dp radius.
//        // Set 4 default SubActionButtons
//        rightLowerMenu = new FloatingActionMenu.Builder(context)
//                // .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon1).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon2).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon3).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon4).build())
//                .addSubActionView(rLSubBuilder.setContentView(rlIcon5).build())
//                .attachTo(rightLowerButton)
//                .build();
//
//        // Listen menu open and close events to animate the button content view
//        rightLowerMenu.setStateChangeListener(new FloatingActionMenu.MenuStateChangeListener() {
//            @Override
//            public void onMenuOpened(FloatingActionMenu menu) {
//                // Rotate the icon of rightLowerButton 45 degrees clockwise
//                menuOpened();
//            }
//
//            @Override
//            public void onMenuClosed(FloatingActionMenu menu) {
//                menuClosed();
//            }
//        });
        //.............initialize views..............
        initializeViews();
    }

    public void menuOpened()
    {
        flyt_blurimg.setVisibility(View.VISIBLE);
        fabIconNew.setBackgroundDrawable(getResources().getDrawable(R.drawable.cross_hover));
        fabIconNew.setRotation(0);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 45);
        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
        animation.start();
    }

    public void menuClosed()
    {
        flyt_blurimg.setVisibility(View.GONE);
        // Rotate the icon of rightLowerButton 45 degrees counter-clockwise
        fabIconNew.setBackgroundDrawable(getResources().getDrawable(R.drawable.infinity));
        fabIconNew.setRotation(45);
        PropertyValuesHolder pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 0);
        ObjectAnimator animation = ObjectAnimator.ofPropertyValuesHolder(fabIconNew, pvhR);
        animation.start();
    }

    public void initializeViews() {
        sharedPreferences = context.getSharedPreferences(getResources().getString(R.string.prefs_name), Context.MODE_PRIVATE);
        profile_image = sharedPreferences.getString("profile_image", "");

        //............drawer layout.....................
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        // Set shadow to drawer
        drawerLayout.setDrawerShadow(R.drawable.background,
                GravityCompat.START);


        drawerToggle = new ActionBarDrawerToggle(this, // Host Activity
                drawerLayout, // layout container for navigation drawer
               R.drawable.launcher_icon, // Application Icon
               R.string.app_name, // Open Drawer Description
                R.string.app_name) // Close Drawer Description
        {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {

            }
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    Log.e("toggle", "here 000");
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(R.id.right_drawer));
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, findViewById(R.id.left_drawer));

                }
            }
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

                    if(drawerView!=null && drawerView == right_drawer){
                        Log.e("toggle", "here 123");

                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.left_drawer));

                    }else  if(drawerView!=null && drawerView == left_drawer){
                        Log.e("toggle","here456");
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.right_drawer));

                    }

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

         products_list = (ListView) findViewById(R.id.sec_products_list);
        al = new ArrayList<SlideMenuModels>();

        al.add(new SlideMenuModels("MY PROFILE", R.drawable.feedback));
        al.add(new SlideMenuModels("RATE US", R.drawable.rate_us));
        al.add(new SlideMenuModels("FEEDBACK", R.drawable.feedback));
        al.add(new SlideMenuModels("TERMS & CONDITIONS", R.drawable.t_c));
        al.add(new SlideMenuModels("ABOUT US", R.drawable.points_badges));
        al.add(new SlideMenuModels("SUBMIT YOUR STORY", R.drawable.invite_referrals));
        al.add(new SlideMenuModels("VERSION", R.drawable.invite_referrals));


        adapter = new SliderAdapter(context, activity, al);
        products_list.setAdapter(adapter);

        slider_profile_name = (TextView)(findViewById(R.id.slider_profile_name));
        slider_profile_name.setText(username);
        slider_profile_pic = (ImageView)(findViewById(R.id.slider_profile_pic));

        if(profile_image.equalsIgnoreCase(""))
        {
            slider_profile_pic.setImageResource(R.drawable.profile);
        }
        else
        {
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(slider_profile_pic);

        }

        slider_profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(right_drawer);
                ft = Timeline.fm.beginTransaction();
                fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("other_user_id", customer_id);
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        slider_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(right_drawer);
                ft = Timeline.fm.beginTransaction();
                fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("other_user_id", customer_id);
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });


        products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (al.get(position).getMenu_name().equalsIgnoreCase("Wallet")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("WalletFragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new WalletFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Order Queries")) {
                    drawerLayout.closeDrawer(right_drawer);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + "123456789"));
                    activity.startActivity(callIntent);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("History")) {
                    drawerLayout.closeDrawer(right_drawer);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Feedback")) {
                    drawerLayout.closeDrawer(right_drawer);
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "abc@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    activity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Rate us")) {
                    drawerLayout.closeDrawer(right_drawer);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("about us")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("AboutUsFragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new AboutUsFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("submit your story")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("FeedBack_Fragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new FeedBack_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("version")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("VersionFragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new VersionFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Terms & Conditions")) {

                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Terms_Conditions_Fragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Terms_Conditions_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Points/Badges")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Points_Badges_Fragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Points_Badges_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
//                else if (al.get(position).getMenu_name().equalsIgnoreCase("Change Password")) {
//                    Fragment f = getVisibleFragment();
//                    Log.e("Slider", f.getClass().getSimpleName());
//
//                    if(f.getClass().getSimpleName().equalsIgnoreCase("ChangePasswordFragment"))
//                    {
//                        
//                    }
//                    else
//                    {
//                        
//                        ft = Timeline.fm.beginTransaction();
//                        fragment = new ChangePasswordFragment();
//                        ft.replace(R.id.content_frame, fragment);
//                        ft.addToBackStack(null);
//                        ft.commit();
//                    }
//                }
                else if (al.get(position).getMenu_name().equalsIgnoreCase("Invite/Referrals")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Invite_Referral_Fragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Invite_Referral_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("My Profile")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("UserProfileFragment")) {
                        drawerLayout.closeDrawer(right_drawer);
                    } else {
                        drawerLayout.closeDrawer(right_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new UserProfileFragment();
                        Bundle args = new Bundle();
                        args.putString("other_user_id", customer_id);
                        fragment.setArguments(args);
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }

            }
        });

        //...............Left Slider..................

        products_list = (ListView) findViewById(R.id.products_list);
        logo_imageView = (ImageView)(findViewById(R.id.logo_imageView));

        logo_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment f = getVisibleFragment();
                Log.e("Slider", f.getClass().getSimpleName());

                if (f.getClass().getSimpleName().equalsIgnoreCase("Home_Fragment")) {
                    drawerLayout.closeDrawer(left_drawer);
                } else {
                    drawerLayout.closeDrawer(left_drawer);
                    ft = Timeline.fm.beginTransaction();
                    fragment = new Home_Fragment();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            }
        });

        al = new ArrayList<SlideMenuModels>();
        al.add(new SlideMenuModels("HOME", R.drawable.home_selected_copy));
        al.add(new SlideMenuModels("ARCHIVE", R.drawable.archive));
        al.add(new SlideMenuModels("VIDEOS",  R.drawable.video));
        al.add(new SlideMenuModels("CHARACTERS", R.drawable.characters));
        al.add(new SlideMenuModels("NOTIFICATIONS", R.drawable.notification));
        al.add(new SlideMenuModels("SETTINGS", R.drawable.settings));
        al.add(new SlideMenuModels("HELP", R.drawable.help));
        al.add(new SlideMenuModels("LOGOUT", R.drawable.signout));

        adapter  = new SliderAdapter(context, activity, al);
        products_list.setAdapter(adapter);

        products_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (al.get(position).getMenu_name().equalsIgnoreCase("Home")) {

                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Home_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Home_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
//
//					fragment = new Home_Fragment();
//					if (fragment != null && fragment.isVisible()) {
//						// add your code here
//						menu.showContent();
//					}
//					else
//					{
//						ft = Timeline.fm.beginTransaction();
//						fragment = new Home_Fragment();
//						ft.replace(R.id.content_frame, fragment);
//						ft.addToBackStack(null);
//						ft.commit();
//					}

//					in = new Intent(context,PersonalInjuryActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Feature Posts")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Featured_Posts_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Featured_Posts_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
//					in = new Intent(context,PersonalInjuryActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Notifications")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("NotificationFragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new NotificationFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
//					in = new Intent(context,PersonalInjuryActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Archive")) {
                    drawerLayout.closeDrawer(left_drawer);
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Videos")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());
                    if (f.getClass().getSimpleName().equalsIgnoreCase("Videos_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Videos_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Store")) {
                    drawerLayout.closeDrawer(left_drawer);
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Hot Gags")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());
                    if (f.getClass().getSimpleName().equalsIgnoreCase("HotGagsFragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new HotGagsFragment();
                        ft.replace(R.id.content_frame, fragment);
                        //	ft.addToBackStack(null);
                        ft.commit();
                    }
//					in = new Intent(context,MainActivity.class);
//					context.startActivity(in);
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Settings")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Settings_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Settings_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Characters")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Characters_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Characters_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Comics")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("Comics_Fragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new Comics_Fragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("Help")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    if (f.getClass().getSimpleName().equalsIgnoreCase("HelpFragment")) {
                        drawerLayout.closeDrawer(left_drawer);
                    } else {
                        drawerLayout.closeDrawer(left_drawer);
                        ft = Timeline.fm.beginTransaction();
                        fragment = new HelpFragment();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                } else if (al.get(position).getMenu_name().equalsIgnoreCase("logout")) {
                    Fragment f = getVisibleFragment();
                    Log.e("Slider", f.getClass().getSimpleName());

                    CommonUtils.showCustomLogoutDialog1(context, activity);
                }
            }
        });


        //............Slider layouts..................
        menu_icon_layout = (LinearLayout) (findViewById(R.id.menu_icon_layout));
        header_textview = (TextView) (findViewById(R.id.header_textview));
        profile_pic_layout = (LinearLayout) (findViewById(R.id.profile_pic_layout));
        options_layout = (LinearLayout) (findViewById(R.id.options_layout));
        back_icon_layout = (LinearLayout)(findViewById(R.id.back_icon_layout));
        submenu_icon_layout = (LinearLayout)(findViewById(R.id.submenu_icon_layout));
        settings_layout = (LinearLayout)(findViewById(R.id.settings_layout));
        notification_layout = (LinearLayout)(findViewById(R.id.notification_layout));
        profile_imageView = (ImageView)(findViewById(R.id.profile_imageView));

        progress_lyt = (LinearLayout)(findViewById(R.id.progress_lyt));

        notification_counter_tv = (TextView)(findViewById(R.id.notification_counter_tv));
        notification_layout.setOnClickListener(this);

        Log.e("Timeline",profile_image);
        if(profile_image.equalsIgnoreCase(""))
        {
            Log.e("Timeline null",profile_image);
            profile_imageView.setImageResource(R.drawable.profile);
        }
        else {
            Log.e("Timeline not null",profile_image);
            Glide.with(context).load(profile_image).diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_imageView);
        }

        header_textview.setText(getResources().getString(R.string.timeline_header_textview));
        menu_icon_layout.setOnClickListener(this);
        options_layout.setOnClickListener(this);
        profile_pic_layout.setOnClickListener(this);
        back_icon_layout.setOnClickListener(this);
        submenu_icon_layout.setOnClickListener(this);
        settings_layout.setOnClickListener(this);

//        timeline_tutorial_imageView = (ImageView)(findViewById(R.id.timeline_tutorial_imageView));
//        timeline_tutorial_imageView.setOnClickListener(this);
//        if(CommonUtils.tutorial == 1)
//        {
//            timeline_tutorial_imageView.setVisibility(View.GONE);
//        }

        //..............Fragments Layouts.....................
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        hot_gags_imageView = (ImageView)(findViewById(R.id.hot_gags_imageView));
        videos_imageView = (ImageView)(findViewById(R.id.videos_imageView));
        home_imageView = (ImageView)(findViewById(R.id.home_imageView));
        search_imageView = (ImageView)(findViewById(R.id.search_imageView));
        cart_imageView = (ImageView)(findViewById(R.id.cart_imageView));

        hotgags_layout = (LinearLayout) findViewById(R.id.hotgags_layout);
        hotgags_layout.setOnClickListener(this);

        videos_layout = (LinearLayout) findViewById(R.id.videos_layout);
        videos_layout.setOnClickListener(this);

        home_layout = (LinearLayout) findViewById(R.id.home_layout);
        home_layout.setOnClickListener(this);

        search_layout = (LinearLayout) findViewById(R.id.search_layout);
        search_layout.setOnClickListener(this);

        cart_layout = (LinearLayout) findViewById(R.id.cart_layout);
        cart_layout.setOnClickListener(this);

        ft = fm.beginTransaction();

//        hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//        videos_imageView.setImageResource(R.drawable.video_tab);
//        home_imageView.setImageResource(R.drawable.home_tab);
//        search_imageView.setImageResource(R.drawable.search_tab);
//        cart_imageView.setImageResource(R.drawable.kart_tab);

        fragment = new Home_Fragment();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//
//            case R.id.submenu_icon_layout:
//
//                Log.e("clickable", "submenu layout");
//                if(menu.isMenuShowing() )
//                {
//                    Log.e("slidermenu","content onback");
//                    
//                    showViews();
//                }
//                break;

            case R.id.menu_icon_layout:
                Log.e("clickable", "menu layout");

                if (drawerLayout.isDrawerOpen(left_drawer)) {
                    drawerLayout.closeDrawer(left_drawer);

                }else
                {
                    drawerLayout.openDrawer(left_drawer);
                }
//
//                menu_icon_layout.setVisibility(View.GONE);
//                Log.e("clickable", "menu layout" + menu_icon_layout.getVisibility());
//
//                submenu_icon_layout.setVisibility(View.VISIBLE);
//                Log.e("clickable", "menu layout123" + submenu_icon_layout.getVisibility());

                //    menu.showMenu();
//                hideViews();

                break;

//            case R.id.timeline_tutorial_imageView:
//                CommonUtils.tutorial = 1;
//                timeline_tutorial_imageView.setVisibility(View.GONE);
//                break;

            case R.id.back_icon_layout:
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                }
                break;

            case R.id.options_layout:

                if (drawerLayout.isDrawerOpen(right_drawer)) {

                    drawerLayout.closeDrawer(right_drawer);
                }else
                {
                    drawerLayout.openDrawer(right_drawer);

                }
                break;

            case R.id.settings_layout:
                ft = Timeline.fm.beginTransaction();
                fragment = new Settings_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.notification_layout:
                ft = Timeline.fm.beginTransaction();
                fragment = new NotificationFragment();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.profile_pic_layout:
                ft = fm.beginTransaction();
                fragment = new UserProfileFragment();
                Bundle args = new Bundle();
                args.putString("other_user_id", customer_id);
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);

                ft.addToBackStack(null);
                ft.commit();
//                Intent in = new Intent(activity,EditProfileActivity.class);
//                startActivity(in);
                break;

            case R.id.home_layout:
                home_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
//                hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//                videos_imageView.setImageResource(R.drawable.video_tab);
//                home_imageView.setImageResource(R.drawable.home_tab);
//                search_imageView.setImageResource(R.drawable.search_tab);
//                cart_imageView.setImageResource(R.drawable.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Home_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.hotgags_layout:
                hotgags_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
//                hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab_copy);
//                videos_imageView.setImageResource(R.drawable.video_tab);
//                home_imageView.setImageResource(R.drawable.home_tab_copy);
//                search_imageView.setImageResource(R.drawable.search_tab);
//                cart_imageView.setImageResource(R.drawable.kart_tab);

                ft = fm.beginTransaction();
                fragment = new HotGagsFragment();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.videos_layout:
                videos_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
//                hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//                videos_imageView.setImageResource(R.drawable.video_tab_copy);
//                home_imageView.setImageResource(R.drawable.home_tab_copy);
//                search_imageView.setImageResource(R.drawable.search_tab);
//                cart_imageView.setImageResource(R.drawable.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Videos_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.search_layout:
                search_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
//                hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//                videos_imageView.setImageResource(R.drawable.video_tab);
//                home_imageView.setImageResource(R.drawable.home_tab_copy);
//                search_imageView.setImageResource(R.drawable.search_tab_copy);
//                cart_imageView.setImageResource(R.drawable.kart_tab);

                ft = fm.beginTransaction();
                fragment = new Search_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.cart_layout:
                cart_layout.setBackgroundColor(getResources().getColor(R.color.blue_header));
//                hot_gags_imageView.setImageResource(R.drawable.hot_gags_tab);
//                videos_imageView.setImageResource(R.drawable.video_tab);
//                home_imageView.setImageResource(R.drawable.home_tab_copy);
//                search_imageView.setImageResource(R.drawable.search_tab);
//                cart_imageView.setImageResource(R.drawable.kart_tab_copy);

                ft = fm.beginTransaction();
                fragment = new Cart_Fragment();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            default:
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        if(menu.isMenuShowing() || right_menu.isMenuShowing())
//        {
//            Log.e("slidermenu", "content onback");
//
//            right_menu.showContent();
//        }
//        else
//        {
//            super.onBackPressed();
//        }
//    }


    private void hideViews() {
//        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) Timeline.rightLowerButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        rightLowerButton.animate().translationY(Timeline.rightLowerButton.getHeight()+fabBottomMargin+100).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
//        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        rightLowerButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }


    //...................


    public Fragment getVisibleFragment(){
//		FragmentManager fragmentManager = activity.getSupportFragmentManager();
        List<Fragment> fragments = Timeline.fm.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


}