package laboratorio.app.activities;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

abstract public class ListActivity<T> extends AppCompatActivity {

    protected APIService service = API.instance.getService();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getCall().enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(Call<List<T>> call, Response<List<T>> response) {
                List<T> allElements = response.body();
                List<T> elementsToShow = elementsToShow(allElements);

                showList(elementsToShow);
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                showError();
            }
        });
    }

    protected abstract void showList(List<T> elementsToShow);

    private void showError() {
        Intent intent = new Intent(this, ErrorActivity.class);
        startActivity(intent);
    }

    protected abstract List<T> elementsToShow(List<T> allElements);

    abstract protected Call<List<T>> getCall();
}
