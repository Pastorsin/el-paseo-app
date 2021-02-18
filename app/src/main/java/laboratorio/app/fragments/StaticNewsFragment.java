package laboratorio.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.StaticNewsAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.ListCallback;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.News;
import laboratorio.app.models.Pagination;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaticNewsFragment extends Fragment implements OnItemListener {
    public static final String ARG_OBJECT = "Noticias";

    private StaticNewsAdapter adapter;

    private APIService service = API.instance.getService();
    private List<News> newsList = new ArrayList<>();

    private ProgressBar progressBar;

    public StaticNewsFragment() {
        // Required empty public constructor
    }

    public static StaticNewsFragment newInstance() {
        StaticNewsFragment fragment = new StaticNewsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_static_news, container, false);

        RecyclerView newsView = view.findViewById(R.id.news_recycler_view);

        progressBar = view.findViewById(R.id.progress_bar);

        adapter = new StaticNewsAdapter(newsList, this::onItemClick);
        newsView.setAdapter(adapter);
        newsView.setLayoutManager(new LinearLayoutManager(getContext()));

        showProgressBar();

        fetchNews(view);

        return view;
    }

    private void fetchNews(View view) {

        service.getNews().enqueue(new Callback<Pagination<News>>() {
            @Override
            public void onResponse(Call<Pagination<News>> call, Response<Pagination<News>> response) {
                hideProgressBar();
                addNews(response.body().getPage());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Pagination<News>> call, Throwable t) {
                ((FragmentLoader) getContext()).replaceFragmentOnMainContainer(new ErrorFragment());
            }

        });
    }

    private void addNews(List<News> allNews) {
        newsList.clear();
        newsList.addAll(newsToShow(allNews));
    }

    private List<News> newsToShow(List<News> allNews) {
        return allNews.stream().collect(Collectors.toList());
    }

    @Override
    public void onItemClick(int position) {
        News news = newsList.get(position);

        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(news.getUrl()));

        try {
            getContext().startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
            Log.d("APLICACION NO ENCONTRADA: ", "No hay aplicaciones para realizar esta acción.");
        }

        getParentFragmentManager().popBackStack();
    }

    private void showProgressBar() {
        if (newsList.isEmpty())
            progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
    }
}