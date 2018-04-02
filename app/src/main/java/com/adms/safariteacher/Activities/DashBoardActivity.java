package com.adms.safariteacher.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.adms.safariteacher.Fragment.AddFamilyFragment;
import com.adms.safariteacher.Fragment.AddSessionFragment;
import com.adms.safariteacher.Fragment.OldFamilyListFragment;
import com.adms.safariteacher.Fragment.SessionFragment;
import com.adms.safariteacher.Fragment.StudentAttendanceFragment;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.Util;
import com.facebook.internal.Utility;

public class DashBoardActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    Context mContex;

    private static final String TAG_Session = "Session";
    private static final String TAG_Add_Session = "Add Session";
    //    private static final String TAG_Add_Family = "Family List";
    private static final String TAG_Student_Attendance = "Student Attendance";

    public static String CURRENT_TAG = TAG_Session;
//    public static String CURRENT_TAG = TAG_Calendar;
//    public static String CURRENT_TAG = TAG_Student_Attendance;

    public static int navItemIndex = 0;
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();
        mContex = DashBoardActivity.this;
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.teacher_name_txt);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website_txt);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.profile_image);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SessionFragment()).addToBackStack(null).commit();
            getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        }

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();


//        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadNavHeader() {
        // name, website
        txtName.setText("ADMS");
        txtWebsite.setText("test@adms.net");
        imgProfile.setImageResource(R.drawable.person_placeholder);

    }

    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
//        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
//            drawer.closeDrawers();
//
//            return;
//        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.addToBackStack(SessionFragment.class.getName());
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                SessionFragment sessionFragment = new SessionFragment();
                return sessionFragment;
            case 1:
                AddSessionFragment addSessionFragment = new AddSessionFragment();
                Bundle args = new Bundle();
                args.putString("flag", "Add");
                addSessionFragment.setArguments(args);
                return addSessionFragment;
//            case 2:
//                new AlertDialog.Builder(new ContextThemeWrapper(mContex, R.style.AppTheme))
//                        .setCancelable(false)
//                        .setTitle("Logout")
//                        .setIcon(mContex.getResources().getDrawable(R.drawable.logo))
//                        .setMessage("Are you sure you want to logout?")
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                Util.setPref(mContex, "coachID", "");
//                                Util.setPref(mContex, "SessionID", "");
//                                Util.setPref(mContex, "FamilyID", "");
//                                Intent intentLogin = new Intent(DashBoardActivity.this, LoginActivity.class);
//                                startActivity(intentLogin);
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                // do nothing
//
//                            }
//                        })
//                        .setIcon(R.drawable.logo)
//                        .show();

            default:
                return new SessionFragment();
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.session:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_Session;
                        break;
                    case R.id.add_session:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_Add_Session;
                        break;
                    case R.id.logout:
                        navItemIndex = 2;
                        new AlertDialog.Builder(new ContextThemeWrapper(mContex, R.style.AppTheme))
                                .setCancelable(false)
                                .setTitle("Logout")
                                .setIcon(mContex.getResources().getDrawable(R.drawable.safari))
                                .setMessage("Are you sure you want to logout?")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Util.setPref(mContex, "coachID", "");
                                        Util.setPref(mContex, "SessionID", "");
                                        Util.setPref(mContex, "FamilyID", "");
                                        Util.setPref(mContex, "coachTypeID", "");
                                        Util.setPref(mContex, "sessionDetailID", "");
                                        Intent intentLogin = new Intent(DashBoardActivity.this, LoginActivity.class);
                                        startActivity(intentLogin);
                                        finish();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing

                                    }
                                })
                                .setIcon(R.drawable.safari)
                                .show();
                        break;
//                    case R.id.student_attendance:
//                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_Student_Attendance;
//                        break;
                    default:
                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_Session;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_Session;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    public void setActionBar(int session, String flag) {
        if (session == 1 && flag.equalsIgnoreCase("edit")) {
            getSupportActionBar().setTitle("Edit Session");
        } else if (session == 1 && flag.equalsIgnoreCase("add")) {
            getSupportActionBar().setTitle("Add Session");
        } else if (session == 10 && flag.equalsIgnoreCase("false")) {
            getSupportActionBar().setTitle("Add Family");
        } else if (session == 11 && flag.equalsIgnoreCase("false")) {
            getSupportActionBar().setTitle("Add Child");
        } else if (session == 12 && flag.equalsIgnoreCase("false")) {
            getSupportActionBar().setTitle("Payment");
        } else if (session == 13 && flag.equalsIgnoreCase("false")) {
            getSupportActionBar().setTitle("Family List");
        } else {
            getSupportActionBar().setTitle(activityTitles[session]);
        }
    }


//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.add_session) {
//
//        }
//
//
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
