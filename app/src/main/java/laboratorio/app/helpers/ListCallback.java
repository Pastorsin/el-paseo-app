package laboratorio.app.helpers;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import androidx.fragment.app.Fragment;
import laboratorio.app.fragments.EmptyListFragment;
import laboratorio.app.fragments.ErrorFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListCallback<T extends List<?>> extends BaseCallback<T> {

    private T list;

    public ListCallback(ProgressBar progressBar, T list,  View view, FragmentLoader fragmentLoader) {
        super(progressBar, view, fragmentLoader);
        this.list = list;
        showProgressBar();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        super.onResponse(call,response);
        
        if (isEmptyList(response.body())) {
            this.onEmptyResponse();
            return;
        }
    }

    @Override
    protected void showProgressBar() {
        if (list.isEmpty())
            super.showProgressBar();
    }

    public boolean isEmptyList(T list) {
        return list.isEmpty();
    }

}
