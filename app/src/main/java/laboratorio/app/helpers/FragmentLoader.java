package laboratorio.app.helpers;

import androidx.fragment.app.Fragment;

public interface FragmentLoader {
    void replaceFragmentOnMainContainer(Fragment fragment);
    void replaceFragment(Fragment fragment, int layout);
    void replaceFragment(Fragment fragment, int layout, String backStack);
}
