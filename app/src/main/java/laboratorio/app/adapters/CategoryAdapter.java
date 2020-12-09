package laboratorio.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import laboratorio.app.R;
import laboratorio.app.activities.CategoryListActivity;
import laboratorio.app.activities.ProductListActivity;
import laboratorio.app.models.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, 0, categories);
    }

    private void setName(Category category, View view) {
        TextView nameView = (TextView) view.findViewById(R.id.category_list_item_name);
        String name = category.getName();
        nameView.setText(name.toUpperCase());
    }

    private void setImage(Category category, View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.category_image_list_item);

        if (category.hasImage()) {
            Bitmap bitmap = category.getImage().bitmap();
            imageView.setImageBitmap(bitmap);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_list_item,
                    parent,
                    false
            );
        }

        Category category = getItem(position);

        setName(category, convertView);
        setImage(category, convertView);

        convertView.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ProductListActivity.class);
            intent.putExtra("CATEGORY", category);

            getContext().startActivity(intent);
        });

        return convertView;
    }

}
