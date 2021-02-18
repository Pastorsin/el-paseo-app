package laboratorio.app.fragments;

import android.app.DownloadManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.CategoryAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.ListCallback;
import laboratorio.app.models.Category;
import retrofit2.Call;
import retrofit2.Response;

public class CategoryListFragment extends Fragment {

    protected APIService service = API.instance.getService();

    protected List<Category> categories = new ArrayList<>();
    private CategoryAdapter adapter;

    private ProgressBar progressBar;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchCategories(View view) {
        service.getCategories().enqueue(new ListCallback<List<Category>>(
                progressBar, categories, view, (FragmentLoader) getContext()) {

            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                super.onResponse(call, response);
                addCategories(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean isEmptyList(List<Category> list) {
                return categoriesToShow(list).isEmpty();
            }
        });
    }

    private void addCategories(List<Category> categoriesResponse) {
        categories.clear();
        categories.addAll(categoriesToShow(categoriesResponse));
    }

    private List<Category> categoriesToShow(List<Category> allCategories) {
        return allCategories.stream()
                .filter(category -> !category.isDeleted())
                .collect(Collectors.toList());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        adapter = new CategoryAdapter(getContext(), categories);

        GridView categoriesView = view.findViewById(R.id.categories_list);
        categoriesView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progress_bar);

        fetchCategories(categoriesView);

        return view;
    }
}