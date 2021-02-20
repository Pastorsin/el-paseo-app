package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

import laboratorio.app.R;
import laboratorio.app.adapters.PagerAdapter;
import laboratorio.app.adapters.StaticNewsAdapter;

public class HomeFragment extends Fragment {

    private ViewPager2 homeViewPager;
    private PagerAdapter pagerAdapter;

    private ArrayList<String> tabNames = new ArrayList();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapter(this);
        homeViewPager = view.findViewById(R.id.home_view_pager);
        homeViewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.home_tab);

        this.getTabViewNames();

        new TabLayoutMediator(tabLayout, homeViewPager,
                (tab, position) -> tab.setText(tabNames.get(position))
        ).attach();

    }

    public void getTabViewNames(){
        tabNames.add(StaticWelcomeFragment.ARG_OBJECT);
        tabNames.add(StaticProducersFragment.ARG_OBJECT);
        tabNames.add(StaticAboutFragment.ARG_OBJECT);
        tabNames.add(StaticNewsFragment.ARG_OBJECT);
    }
}