package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import laboratorio.app.R;
import laboratorio.app.adapters.StaticProducersAdapter;
import laboratorio.app.controllers.API;
import laboratorio.app.controllers.APIService;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.helpers.ListCallback;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.Producer;
import retrofit2.Call;
import retrofit2.Response;

public class StaticProducersFragment extends Fragment implements OnItemListener {
    public static final String ARG_OBJECT = "Productores";

    private StaticProducersAdapter adapter;

    private APIService service = API.instance.getService();
    private List<Producer> producersList = new ArrayList<>();

    private ProgressBar progressBar;

    public StaticProducersFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_producers, container, false);

        RecyclerView producersView = view.findViewById(R.id.home_recycler_view);

        progressBar = view.findViewById(R.id.progress_bar);

        adapter = new StaticProducersAdapter(producersList, this::onItemClick);
        producersView.setAdapter(adapter);
        producersView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchProducers(view);

        return view;
    }

    private void fetchProducers(View view) {

        service.getProducers().enqueue(new ListCallback<List<Producer>>(
                progressBar, producersList, view, (FragmentLoader) getContext()) {
            @Override
            public void onResponse(Call<List<Producer>> call, Response<List<Producer>> response) {
                super.onResponse(call, response);
                addProducers(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean isEmptyList(List<Producer> producersResponse) {
                return producersToShow(producersResponse).isEmpty();
            }
        });
    }

    private void addProducers(List<Producer> allProducers) {
        producersList.clear();
        producersList.addAll(producersToShow(allProducers));
    }

    private List<Producer> producersToShow(List<Producer> allProducers) {
        return allProducers.stream().collect(Collectors.toList());
    }

    @Override
    public void onItemClick(int position) {
        Producer producer = producersList.get(position);
        FragmentLoader loader = (FragmentLoader) getContext();
        Fragment fragment = StaticProducerDetailFragment.newInstance(producer);

        loader.replaceFragmentOnMainContainer(fragment);
    }
}