package laboratorio.app.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import laboratorio.app.fragments.StaticAboutFragment;
import laboratorio.app.fragments.StaticWelcomeFragment;
import laboratorio.app.fragments.StaticProducersFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new StaticWelcomeFragment();
            case 1:
                return new StaticProducersFragment();
            case 2:
                return new StaticAboutFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

