package com.example.abilashr.saikailashooty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

public class EventFragment extends Fragment {

    FragmentTabHost fragmentTabHost;

    public EventFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        fragmentTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        TabHost.TabSpec tabSpecUpcoming = fragmentTabHost.newTabSpec(getResources().getString(R.string.upcoming)).setIndicator(getResources().getString(R.string.upcoming), null);
        fragmentTabHost.addTab(tabSpecUpcoming, UpcomingFragment.class, null);
        TabHost.TabSpec tabSpecPast = fragmentTabHost.newTabSpec(getResources().getString(R.string.past)).setIndicator(getResources().getString(R.string.past), null);
        fragmentTabHost.addTab(tabSpecPast, PastFragment.class, null);

        upcomingEventSelected();
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(fragmentTabHost.getCurrentTab() == 0) {
                    upcomingEventSelected();
                } else {
                    pastEventSelected();
                }
            }
        });
        return rootView;
    }

    private void pastEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.upcoming_tab_selector_not_selected);
    }

    private void upcomingEventSelected() {
        fragmentTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_selector_selected);
        fragmentTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.past_tab_selector_not_selected);
    }
}
