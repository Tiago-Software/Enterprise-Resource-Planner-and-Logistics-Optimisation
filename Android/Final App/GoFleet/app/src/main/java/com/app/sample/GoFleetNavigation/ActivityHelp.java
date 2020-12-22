package com.app.sample.GoFleetNavigation;

import android.content.Context;
import android.content.Intent;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.app.sample.GoFleetNavigation.data.Tools;
import com.app.sample.GoFleetNavigation.model.Order;

public class ActivityHelp extends AppCompatActivity {

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext, btnPrev;

    private Boolean Login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Login = (Boolean) getIntent().getSerializableExtra("Login");

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnPrev = (Button) findViewById(R.id.btn_prev);

        // add few more layouts if you want
        layouts = new int[]{
                R.layout.help_slide_1,
                R.layout.help_slide_15,
                R.layout.help_slide_16,
                R.layout.help_slide_2,
                R.layout.help_slide_3,
                R.layout.help_slide_4,
                R.layout.help_slide_8,
                R.layout.help_slide_5,
                R.layout.help_slide_6,
                R.layout.help_slide_7,
                R.layout.help_slide_17,
                R.layout.help_slide_9,
                R.layout.help_slide_10,
                R.layout.help_slide_11,
                R.layout.help_slide_12,
                R.layout.help_slide_13,
                R.layout.help_slide_14,
                R.layout.help_slide_18,};

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent
        Tools.changeStatusBarColor(this);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((Login != null) && (Login == true))
                {
                    Intent intent = new Intent(ActivityHelp.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                    Login = false;
                }
                else
                {
                    launchActivityMain();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(-1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchActivityMain();
                }
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else
                {
                    if((Login != null) && (Login == true))
                    {
                        Intent intent = new Intent(ActivityHelp.this, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                        Login = false;
                    }
                    else
                    {
                        launchActivityMain();
                    }

                }
            }
        });

    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.darkOverlaySoft));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.whiteOverlay));
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchActivityMain() {
        startActivity(new Intent(ActivityHelp.this, ActivityMain.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if (position == layouts.length - 1) {
                btnNext.setText("GOT IT");
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText("NEXT");
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
