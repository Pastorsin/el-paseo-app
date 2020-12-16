package laboratorio.app.helpers;

import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import androidx.fragment.app.Fragment;
import laboratorio.app.fragments.EmptyListFragment;
import laboratorio.app.fragments.ErrorFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCallback<T extends List<?>> implements Callback<T> {

    private ProgressBar progressBar;
    private T list;
    private View view;
    private FragmentLoader fragmentLoader;

    private final Fragment errorFragment = new ErrorFragment();
    private final Fragment emptyListFragment = new EmptyListFragment();

    public ListCallback(ProgressBar progressBar, T list, View view, FragmentLoader fragmentLoader) {
        this.progressBar = progressBar;
        this.list = list;
        this.view = view;
        this.fragmentLoader = fragmentLoader;

        showProgressBar();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        hideProgressBar();

        if (isEmptyList(response.body())) {
            this.onEmptyResponse();
            return;
        }
    }

    private void onEmptyResponse() {
        hideProgressBar();
        showFragment(emptyListFragment);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        hideProgressBar();
        showFragment(errorFragment);
   }

    private void showFragment(Fragment fragment) {
        fragmentLoader.replaceFragmentOnMainContainer(fragment);
    }

    private void hideProgressBar() {
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        if (list.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
    }

    public boolean isEmptyList(T list) {
        return false;
    }

}
