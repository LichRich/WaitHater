package com.mally.ch02.waitHater.ui.BusInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.mally.ch02.waitHater.R;

public class RouteInfoActivity extends AppCompatActivity {

    /*
     *
     * TAG
     *
     * */
    private final String TAG = "Selected Route Info";
    /*
     *
     * Layout Components
     *
     * */
    private Context mContext;
    private TabLayout mTabLayout;
    private RouteInfoViewPager mViewPager;
    private RouteInfoPagerAdapter mAdapter;
    private static int currentPosition = 0;
    /*
     *
     * getIntent from AllRoutesFragment
     *
     * */
    private static Intent intent;
    public static Intent getSRIntent() {
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
        mContext = getApplicationContext();

        mTabLayout = (TabLayout) findViewById(R.id.layout_tab);
        intent = getIntent();

        mTabLayout.addTab(mTabLayout.newTab().setText("상행"));
        mTabLayout.addTab(mTabLayout.newTab().setText("하행"));

        /*
         *
         * use the adapter on view pager.
         *
         * */
        mViewPager = (RouteInfoViewPager) findViewById(R.id.pager_routeInfo);
        mAdapter = new RouteInfoPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                currentPosition = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}

class RouteInfoViewPager extends ViewPager {

    public RouteInfoViewPager(@NonNull Context context) {
        super(context);
    }
    public RouteInfoViewPager(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if(h > height) {
                height = h;
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}