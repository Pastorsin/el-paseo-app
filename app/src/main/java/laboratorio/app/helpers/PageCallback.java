package laboratorio.app.helpers;

import android.view.View;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import java.util.List;

import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.models.Pagination;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageCallback<T extends Pagination<?>> extends BaseCallback<T> {

    private List list;

    public PageCallback(ProgressBar progressBar, List list, View view, FragmentLoader fragmentLoader) {
        super(progressBar, view, fragmentLoader);
        this.list = list;
        showProgressBar();
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        super.onResponse(call,response);

        if(isEmptyPage(response.body())){
            this.onEmptyResponse();
            return;
        }
    }

    @Override
    protected void showProgressBar() {
        if (list.isEmpty())
            super.showProgressBar();
    }

    public boolean isEmptyPage(T pagination){
        return pagination.getPage().isEmpty();
    }

}
