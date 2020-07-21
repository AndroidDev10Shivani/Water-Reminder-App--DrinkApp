package com.sample.drinkup;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    private Handler mainhandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlue));

        setContentView(R.layout.activity_main);
        new Thread(new LabelRunnable()).start();

        setupViewpager((ViewPager) findViewById(R.id.viewpager));

        tabLayout = findViewById(R.id.tab);
        tabLayout.setupWithViewPager((ViewPager) findViewById(R.id.viewpager));
        setupIcons();

    }
    private void setupIcons()
    {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_water_menu);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_settings_menu);
    }
    private void setupViewpager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), "Home");
        adapter.addFragment(new SettingFragment(), "Settings");
        viewPager.setAdapter(adapter);
    }

    public class ViewpagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewpagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
    private void createNotifiactionChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "channel";
            String desc = "Water reminder app";
            int important = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channelID", name, important);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    class LabelRunnable implements Runnable {
        @Override
        public void run() {
            mainhandler.post(new Runnable() {
                @Override
                public void run() {
                    createNotifiactionChannel();
                    Intent intent = new Intent(MainActivity.this, AlaertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                    long timeatbuttonclick = System.currentTimeMillis();
                    long timeseondsinmills = 1000 * 10;
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeatbuttonclick + timeseondsinmills, pendingIntent);
                }
            });
            try {
                Thread.sleep(7200000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



