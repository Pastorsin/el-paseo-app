package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.ProductAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.Category;
import laboratorio.app.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    private static final String CATEGORY_ARG = "category_arg";

    private Serializable categoryArg;

    private Category category;
    private List<Product> products = new ArrayList<>();

    private ProductAdapter adapter;

    private APIService service = API.instance.getService();

    public ProductListFragment() {
        // Required empty public constructor
    }

    public static ProductListFragment newInstance(Category aCategory) {
        ProductListFragment fragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putSerializable(CATEGORY_ARG, aCategory);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryArg = getArguments().getSerializable(CATEGORY_ARG);
            category = (Category) categoryArg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        ListView productsView = view.findViewById(R.id.products_list);

        adapter = new ProductAdapter(getContext(), products);
        productsView.setAdapter(adapter);

        fetchProducts();

        return view;
    }

    private void fetchProducts() {
        service.getProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();
                addProducts(products);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                FragmentLoader loader = (FragmentLoader) getContext();
                Fragment fragment = new ErrorFragment();

                loader.replaceFragmentOnMainContainer(fragment);
            }
        });

    }

    private void addProducts(List<Product> allProducts) {
        products.clear();
        products.addAll(productsToShow(allProducts));
    }

    private List<Product> productsToShow(List<Product> allProducts) {
        return allProducts.stream()
                .filter(product -> product.hasCategory(category) && product.hasStock())
                .collect(Collectors.toList());
    }
}