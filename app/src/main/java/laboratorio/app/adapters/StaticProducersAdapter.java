package laboratorio.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import laboratorio.app.R;
import laboratorio.app.fragments.ProductDetailFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.Producer;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class StaticProducersAdapter extends RecyclerView.Adapter<StaticProducersAdapter.ViewHolder> {

        private List<Producer> localDataSet;
        private OnItemListener mOnItemListener;


        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;
            private final ImageView imageView;
            private final OnItemListener onItemListener;

            public ViewHolder(View view, OnItemListener onItemListener) {
                super(view);
                // Define click listener for the ViewHolder's View

                textView = (TextView) view.findViewById(R.id.producer_name);

                imageView = (ImageView) view.findViewById(R.id.producer_image);
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


        public StaticProducersAdapter(List<Producer> dataSet, OnItemListener mOnItemListener) {
            this.localDataSet = dataSet;
            this.mOnItemListener = mOnItemListener;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            // Create a new view, which defines the UI of the list item
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_static_producers_list_item, viewGroup, false);

            return new ViewHolder(view, this.mOnItemListener);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {

            // Get element from your dataset at this position and replace the
            // contents of the view with that element
            viewHolder.getTextView().setText(localDataSet.get(position).getName());


            if(localDataSet.get(position).hasMainImage()) {
                Bitmap bitmap = localDataSet.get(position).getImages().get(0).bitmap();
                viewHolder.getImageView().setImageBitmap(bitmap);
            }else{
                //modificar imagen a ic_menu_gallery
                viewHolder.getImageView().setImageResource(R.drawable.ic_el_paseo);
            }

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return localDataSet.size();
        }

}
