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
import laboratorio.app.models.News;

public class StaticNewsAdapter extends RecyclerView.Adapter<StaticNewsAdapter.ViewHolder> {

    private List<News> localDataSet;
    private OnItemListener mOnItemListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleView;
        private final TextView subtitleView;
        private final TextView textView;
        private final ImageView imageView;
        private final OnItemListener onItemListener;

        public ViewHolder(View view, OnItemListener onItemListener) {
            super(view);

            titleView = (TextView) view.findViewById(R.id.news_title_textView);

            subtitleView = (TextView) view.findViewById(R.id.news_subtitle_textView);

            textView = (TextView) view.findViewById(R.id.news_text_textView);

            imageView = (ImageView) view.findViewById(R.id.news_imageView);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        public TextView getTitleView(){return titleView;}

        public TextView getSubtitleView() {
            return subtitleView;
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

    public StaticNewsAdapter(List<News> dataSet, OnItemListener mOnItemListener) {
        this.localDataSet = dataSet;
        this.mOnItemListener = mOnItemListener;
    }

    @Override
    public StaticNewsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_static_news_list_item, viewGroup, false);

        return new StaticNewsAdapter.ViewHolder(view, this.mOnItemListener);
    }

    @Override
    public void onBindViewHolder(StaticNewsAdapter.ViewHolder viewHolder, final int position) {
        News news = localDataSet.get(position);

        setTitle(viewHolder, news);
        setSubtitle(viewHolder, news);
        setText(viewHolder, news);
        setImage(viewHolder, news);
    }

    public void setTitle(StaticNewsAdapter.ViewHolder viewHolder, News news){
        viewHolder.getTitleView().setText(news.getTitle());
    }

    public void setSubtitle(StaticNewsAdapter.ViewHolder viewHolder, News news){
        viewHolder.getSubtitleView().setText(news.getSubtitle());
    }

    public void setText(StaticNewsAdapter.ViewHolder viewHolder, News news){
        viewHolder.getTextView().setText(news.getText());
    }

        public void setImage(StaticNewsAdapter.ViewHolder viewHolder, News news){
        if(news.hasImage()) {
            Bitmap bitmap = news.getImage().bitmap();
            viewHolder.getImageView().setImageBitmap(bitmap);
        }else{
            viewHolder.getImageView().setImageResource(R.drawable.ic_no_image_el_paseo);
        }
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
