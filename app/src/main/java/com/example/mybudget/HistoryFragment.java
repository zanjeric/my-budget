package com.example.mybudget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class HistoryFragment extends Fragment {

    private static final int NUM_PAGES = 3;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), this.getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        return view;
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm, Lifecycle lc) {
            super(fm, lc);
        }

        @Override
        public Fragment createFragment(int position) {
            return new HistorySlideFragment();
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}
