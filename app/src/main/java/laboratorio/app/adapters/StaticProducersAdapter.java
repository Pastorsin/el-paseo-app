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
import laboratorio.app.models.Producer;

public class StaticProducersAdapter extends RecyclerView.Adapter<StaticProducersAdapter.ViewHolder> {

        private List<Producer> localDataSet;
        private OnItemListener mOnItemListener;


        public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private final TextView textView;
            private final ImageView imageView;
            private final OnItemListener onItemListener;

            public ViewHolder(View view, OnItemListener onItemListener) {
                super(view);

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

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.fragment_static_producers_list_item, viewGroup, false);

            return new ViewHolder(view, this.mOnItemListener);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int position) {
            Producer producer = localDataSet.get(position);

            setName(viewHolder, producer);
            setImage(viewHolder, producer);
        }

        public void setName(ViewHolder viewHolder, Producer producer){
            viewHolder.getTextView().setText(producer.getName());
        }

        public void setImage(ViewHolder viewHolder, Producer producer){
            if(producer.hasMainImage()) {
                Bitmap bitmap = producer.getMainImage().bitmap();
                viewHolder.getImageView().setImageBitmap(bitmap);
            }else{
                viewHolder.getImageView().setImageResource(R.drawable.ic_no_image_el_paseo);
                //int color_value = Color.parseColor("#e09f3e");
                //ImageViewCompat.setImageTintList(viewHolder.getImageView(), ColorStateList.valueOf(color_value));
            }
        }

        @Override
        public int getItemCount() {
            return localDataSet.size();
        }

}
