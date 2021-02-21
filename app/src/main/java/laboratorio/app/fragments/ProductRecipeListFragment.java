package laboratorio.app.fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import laboratorio.app.R;
import laboratorio.app.adapters.RecipesAdapter;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.Recipe;

public class ProductRecipeListFragment extends Fragment implements OnItemListener {

    private RecipesAdapter adapter;
    private List<Recipe> recipesList = new ArrayList<>();

    private static final String NAME_ARG = "product_name";

    private String product_name;

    public static ProductRecipeListFragment newInstance(String aProduct_name) {
        ProductRecipeListFragment fragment = new ProductRecipeListFragment();
        Bundle args = new Bundle();
        args.putString(NAME_ARG, aProduct_name);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductRecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product_name = getArguments().getString(NAME_ARG);
        }
        fillRecipeList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(product_name.equals("Albahaca fresca")){
            View view = inflater.inflate(R.layout.fragment_product_recipe_list, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recipes_recycler_view);

            adapter = new RecipesAdapter(recipesList, this::onItemClick);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            return view;
        }else{
            return inflater.inflate(R.layout.fragment_empty_list,container,false);
        }
    }

    public void fillRecipeList(){
        Recipe recipe1 = new Recipe();
        recipe1.setTitle("Tomates con albahaca");
        recipe1.setText(" Corta los tomates en rodajas o en lonchas finas.\n" +
                "Rocia con vinagre, aceite, sal y pimienta negra.\n" +
                "Trocea las hojas de albahaca y reparte por la superficie.\n" +
                "Sirve y guarda en lugar fresco. ");
        recipe1.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.tomates_albahaca));
        Recipe recipe2 = new Recipe();
        recipe2.setTitle("Pesto de albahaca");
        recipe2.setText("Delicioso pesto de albahaca para tus comidas italianas. Es una receta tradicional que llena de sabor todos los platillos!\n"+
                "Mezcla el parmesano, albahaca, polvo de nuez, ajo, sal y pimienta en un bowl.\n" +
                "Agrega el aceite de oliva hasta que se mezcle todo muy bien.\n" +
                "Guarda en un frasco de vidrio y deja reposar por 30 minutos.\n" +
                "Mezcla con pastas y/o platillos." );
        recipe2.setImage(BitmapFactory.decodeResource(getResources(), R.drawable.salsa_pesto));
        recipesList.add(recipe1);
        recipesList.add(recipe2);
    }

    @Override
    public void onItemClick(int position) {
        Recipe recipe = recipesList.get(position);
        FragmentLoader loader = (FragmentLoader) getContext();
        Fragment fragment = RecipeDetailFragment.newInstance(recipe);

        loader.replaceFragmentOnMainContainer(fragment);
    }
}