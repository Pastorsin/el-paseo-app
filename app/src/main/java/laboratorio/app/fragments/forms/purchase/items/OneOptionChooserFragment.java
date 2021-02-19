package laboratorio.app.fragments.forms.purchase.items;

import android.view.View;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import laboratorio.app.helpers.FragmentLoader;

abstract public class OneOptionChooserFragment extends Fragment {
    protected Fragment lastFragmentLoaded;

    protected void loadFragment(Fragment fragment) {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragment(fragment, getLoadFragmentContainerId());

        removeLastFragmentLoaded();
        lastFragmentLoaded = fragment;
    }

    protected abstract int getLoadFragmentContainerId();

    protected void removeLastFragmentLoaded() {
        if (lastFragmentLoaded != null) {
            FragmentLoader loader = (FragmentLoader) getActivity();
            loader.removeFragment(lastFragmentLoaded);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeLastFragmentLoaded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLastFragmentLoaded();
    }

    protected void initRadioGroup(View view) {
        RadioGroup radioGroup = view.findViewById(getRadioGroupId());

        radioGroup.setOnCheckedChangeListener((group, checkedElement) -> {
            Fragment checkedFragment = getCheckedFragment(group, checkedElement);

            loadFragment(checkedFragment);
        });

        loadFragment(getInitialFragment());
    }

    protected abstract int getRadioGroupId();

    public abstract Fragment getCheckedFragment(RadioGroup group, int checkedElement);

    protected abstract Fragment getInitialFragment();

}
