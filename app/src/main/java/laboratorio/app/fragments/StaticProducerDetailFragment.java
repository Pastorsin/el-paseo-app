package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;

import laboratorio.app.R;
import laboratorio.app.models.Address;
import laboratorio.app.models.Producer;


public class StaticProducerDetailFragment extends Fragment {

    private static final String PRODUCER_ARG = "producer_arg";

    private Serializable producerArg;

    private Producer producer;


    public StaticProducerDetailFragment() {
        // Required empty public constructor
    }

    public static StaticProducerDetailFragment newInstance(Producer aProducer) {
        StaticProducerDetailFragment fragment = new StaticProducerDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(PRODUCER_ARG, aProducer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            producerArg = getArguments().getSerializable(PRODUCER_ARG);
            producer = (Producer) producerArg;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_static_producer_detail, container, false);

        addName(view);
        addDescription(view);
        addEmail(view);
        addPhone(view);
        addAddress(view);

        return view;
    }

    public void addName(View view){
        TextView nameView = view.findViewById(R.id.static_detail_producer_name);
        nameView.setText(producer.getName());
    }

    public void addDescription(View view){
        TextView descriptionView = view.findViewById(R.id.static_detail_producer_description);
        descriptionView.setText(producer.getDescription());
    }

    public void addEmail(View view){
        TextView emailView = view.findViewById(R.id.static_detail_producer_email);
        emailView.setText(producer.getEmail());
    }

    public void addPhone(View view){
        TextView phoneView = view.findViewById(R.id.static_detail_producer_phone);
        phoneView.setText(producer.getPhone());
    }

    public void addAddress(View view){
        TextView addressView = view.findViewById(R.id.static_detail_producer_address);
        Address address = producer.getAddress();
        addressView.setText(address.getNumber() + address.getStreet());
    }

}