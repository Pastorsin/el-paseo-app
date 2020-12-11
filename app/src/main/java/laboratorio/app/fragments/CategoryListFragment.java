package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.CategoryAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListFragment extends Fragment {

    protected APIService service = API.instance.getService();

    protected List<Category> categories = new ArrayList<>();
    private CategoryAdapter adapter;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchCategories() {
        service.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                List<Category> categoriesResponse = response.body();

                addCategories(categoriesResponse);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                FragmentLoader loader = (FragmentLoader) getContext();
                Fragment fragment = new ErrorFragment();

                loader.loadFragment(fragment);
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

        fetchCategories();

        return view;
    }
}