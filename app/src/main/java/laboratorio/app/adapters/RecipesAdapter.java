package laboratorio.app.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import laboratorio.app.R;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<laboratorio.app.adapters.RecipesAdapter.ViewHolder> {
    private List<Recipe> localDataSet;
    private OnItemListener mOnItemListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final ImageView imageView;
        private final OnItemListener onItemListener;

        public ViewHolder(View view, OnItemListener onItemListener) {
            super(view);

            textView = (TextView) view.findViewById(R.id.recipe_title_textView);

            imageView = (ImageView) view.findViewById(R.id.recipe_imageView);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView(){ return imageView;}

        @Override
        public void onClick(View view) {
            this.onItemListener.onItemClick(getAdapterPosition());
        }
    }


    public RecipesAdapter(List<Recipe> dataSet, OnItemListener mOnItemListener) {
        this.localDataSet = dataSet;
        this.mOnItemListener = mOnItemListener;
    }

    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_product_recipe_list_item, viewGroup, false);

        return new RecipesAdapter.ViewHolder(view, this.mOnItemListener);
    }

    @Override
    public void onBindViewHolder(RecipesAdapter.ViewHolder viewHolder, final int position) {
        Recipe recipe = localDataSet.get(position);

        setTitle(viewHolder, recipe);
        setImage(viewHolder, recipe);
    }

    public void setTitle(RecipesAdapter.ViewHolder viewHolder, Recipe recipe){
        viewHolder.getTextView().setText(recipe.getTitle());
    }

    public void setImage(RecipesAdapter.ViewHolder viewHolder, Recipe recipe){
        if(recipe.hasImage()) {
            viewHolder.getImageView().setImageBitmap(recipe.getImage());
        }else{
            viewHolder.getImageView().setImageResource(R.drawable.ic_no_image_el_paseo);
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
