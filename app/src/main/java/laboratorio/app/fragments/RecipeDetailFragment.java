package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import laboratorio.app.R;
import laboratorio.app.models.Recipe;


public class RecipeDetailFragment extends Fragment {

    private static final String RECIPE_ARG = "recipe_arg";

    private Serializable recipeArg;

    private Recipe recipe;


    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(Recipe aRecipe) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ARG, aRecipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeArg = getArguments().getSerializable(RECIPE_ARG);
            recipe = (Recipe) recipeArg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        addTitle(view);
        addInstructions(view);
        addImage(view);

        return view;
    }

    private void addTitle(View view){
        TextView title_textView = view.findViewById(R.id.recipe_detail_title_textView);
        title_textView.setText(recipe.getTitle());
    }

    private void addInstructions(View view){
        TextView instructions_textView = view.findViewById(R.id.recipe_detail_instructions_textView);
        instructions_textView.setText(recipe.getText());
    }

    private void addImage(View view){
        ImageView recipe_imageView = view.findViewById(R.id.recipe_detail_imageView);
        recipe_imageView.setImageBitmap(recipe.getImage());
    }
}