package com.example.LandmarkDetection;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import me.relex.circleindicator.CircleIndicator3;

public class Specific extends AppCompatActivity {
    static final int ACTIVITY_CODE = 3245;
    static final String TAG = "DHARMIK";
    static final int NUM_PAGES = 5;
    ViewPager2 viewPager;
    PageAdapter pagerAdapter;
    Landmark landmark;
    CircleIndicator3 dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);

        landmark = (Landmark) getIntent().getSerializableExtra("specificLandmark");
        dots = findViewById(R.id.id_specific_dots);
        viewPager = findViewById(R.id.id_specific_VP2);
        pagerAdapter = new PageAdapter(this);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(new MyPageTrans());

        dots.setViewPager(viewPager);
        pagerAdapter.registerAdapterDataObserver(dots.getAdapterDataObserver());

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }






    public class PageAdapter extends FragmentStateAdapter {
        public PageAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return new PageFragment(landmark, position);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}
