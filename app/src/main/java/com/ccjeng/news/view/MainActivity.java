package com.ccjeng.news.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ccjeng.news.R;
import com.ccjeng.news.utils.Analytics;
import com.ccjeng.news.utils.Constant;
import com.ccjeng.news.utils.Network;
import com.ccjeng.news.utils.PreferenceSetting;
import com.ccjeng.news.utils.Version;
import com.ccjeng.news.view.base.BaseActivity;
import com.ccjeng.news.view.base.News;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mopub.mobileads.MoPubView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();
    private Analytics ga;

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.pager)
    ViewPager pager;

    @Bind(R.id.navigation)
    NavigationView navigation;

    @Bind(R.id.drawerlayout)
    DrawerLayout drawerLayout;

    private static final int DIALOG_WELCOME = 1;
    private static final int DIALOG_UPDATE = 2;
    private MoPubView moPubView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ga = new Analytics();
        ga.trackerPage(this);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        setSupportActionBar(toolbar);

        setSwipeBackEnable(false);

        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(pager);

        navDrawer();

        PreferenceSetting.getPreference(this);
        changeTheme(savedInstanceState);

        //todo use dailog fragment
        if (Version.isNewInstallation(this)) {
            this.showDialog(DIALOG_WELCOME);
        } else
        if (Version.newVersionInstalled(this)) {
            this.showDialog(DIALOG_UPDATE);
        }

        //default tab selection
        TabLayout.Tab tab = tabs.getTabAt(News.getPrefDefaultTab());
        if (tab != null) {
            tab.select();
        }
        moPubView = (MoPubView) findViewById(R.id.adview);
        Network.AdView(moPubView, Constant.Ad_MoPub_Main);
    }

    @Override
    public void onDestroy() {
        if (moPubView != null) {
            moPubView.destroy();
        }

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceSetting.getPreference(this);
    }


    private long lastMillis;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - lastMillis) > 2000) {
            Toast.makeText(this, R.string.quit_tip, Toast.LENGTH_SHORT).show();
            lastMillis = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { getString(R.string.tab_tw), getString(R.string.tab_hk),  getString(R.string.tab_sg)};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return TabFragment.newInstance(position);
        }

    }

    private void navDrawer() {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
                else
                    menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.navSetting:
                        startActivity(new Intent(MainActivity.this, Preference.class));
                        break;
                    case R.id.navAbout:
                        new LibsBuilder()
                                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                .withAboutIconShown(true)
                                .withAboutVersionShown(true)
                                .withAboutAppName(getString(R.string.app_name))
                                .withActivityTitle(getString(R.string.about))
                                .withAboutDescription(getString(R.string.license))
                                .start(MainActivity.this);
                        break;
                    case R.id.navSuggest:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=com.ccjeng.news")));
                        break;

                }
                return false;
            }
        });

        //change navigation drawer item icons
        navigation.getMenu().findItem(R.id.navSetting).setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_settings)
                .color(Color.GRAY)
                .sizeDp(24));

        navigation.getMenu().findItem(R.id.navAbout).setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_information)
                .color(Color.GRAY)
                .sizeDp(24));

        navigation.getMenu().findItem(R.id.navSuggest).setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_thumb_up)
                .color(Color.GRAY)
                .sizeDp(24));


        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar
                ,R.string.app_name, R.string.app_name){

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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    protected final Dialog onCreateDialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(new IconicsDrawable(this)
                .icon(CommunityMaterial.Icon.cmd_information)
                .color(Color.GRAY)
                .sizeDp(24));

        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, null);

        //final Context context = this;

        switch (id) {
            case DIALOG_WELCOME:
                builder.setTitle(getResources().getString(R.string.welcome_title));
                builder.setMessage(getResources().getString(R.string.welcome_message));
                builder.setNeutralButton(getText(R.string.setting),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface d, final int which) {
                                Intent i = new Intent(MainActivity.this, Preference.class);
                                startActivity(i);
                            }
                        });
                break;
            case DIALOG_UPDATE:
                builder.setTitle(getString(R.string.changelog_title));
                final String[] changes = getResources().getStringArray(R.array.updates);
                final StringBuilder buf = new StringBuilder();
                for (int i = 0; i < changes.length; i++) {
                    buf.append("\n\n");
                    buf.append(changes[i]);
                }
                builder.setMessage(buf.toString().trim());
                break;
        }
        return builder.create();
    }

    //Change Day/Night Theme
    private void changeTheme(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (News.getPrefBGColor().equals("#FFFFFF")) {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO);

            } else {
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES);
            }
            recreate();
        }
    }
}
