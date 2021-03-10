package laboratorio.app.helpers;

import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import laboratorio.app.fragments.EmptyListFragment;
import laboratorio.app.fragments.ErrorFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCallback<T> implements Callback<T> {

    private ProgressBar progressBar;
    private View view;
    private FragmentLoader fragmentLoader;

    private final Fragment errorFragment = new ErrorFragment();
    private final Fragment emptyListFragment = new EmptyListFragment();

    public BaseCallback(ProgressBar progressBar, View view, FragmentLoader fragmentLoader){
        this.progressBar = progressBar;
        this.view = view;
        this.fragmentLoader = fragmentLoader;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        hideProgressBar();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        hideProgressBar();
        showFragment(errorFragment);
    }

    protected void onEmptyResponse() {
        hideProgressBar();
        showFragment(emptyListFragment);
    }

    protected void showFragment(Fragment fragment) {
        fragmentLoader.replaceFragmentOnMainContainer(fragment);
    }

    protected void hideProgressBar() {
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }

    protected void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

}
