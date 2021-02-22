package laboratorio.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

import laboratorio.app.R;
import laboratorio.app.helpers.IntentManager;
import laboratorio.app.models.Address;
import laboratorio.app.models.Producer;


public class StaticProducerDetailFragment extends Fragment {

    private static final String PRODUCER_ARG = "producer_arg";

    private Serializable producerArg;

    private Producer producer;
    private IntentManager intentManager;


    public StaticProducerDetailFragment() {
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

        this.intentManager = new IntentManager(this);

        addName(view);
        addDescription(view);
        addEmail(view);
        addPhone(view);
        addAddress(view);
        addVideo(getContext(), view);

        return view;
    }



    public void addName(View view) {
        TextView nameView = view.findViewById(R.id.static_detail_producer_name);
        nameView.setText(producer.getName());
    }

    public void addDescription(View view) {
        TextView descriptionView = view.findViewById(R.id.static_detail_producer_description);
        descriptionView.setText(producer.getDescription());
    }

    public void addEmail(View view) {
        TextView emailView = view.findViewById(R.id.static_detail_producer_email);
        if (producer.hasEmail() && !producer.getEmail().isEmpty()) {
            emailView.setText(producer.getEmail());
            intentManager.setEmailIntent(emailView);
        } else {
            emailView.setText(getEmptyFieldMessage());
        }
    }

    public void addPhone(View view) {
        TextView phoneView = view.findViewById(R.id.static_detail_producer_phone);
        if (producer.hasPhoneNumber()) {
            phoneView.setText(producer.getPhone());
            intentManager.setPhoneIntent(phoneView);
        } else {
            phoneView.setText(getEmptyFieldMessage());
        }
    }

    public void addAddress(View view) {
        TextView addressView = view.findViewById(R.id.static_detail_producer_address);
        if (producer.hasAddress()) {
            Address address = producer.getAddress();
            addressView.setText(address.fullAddress());
        } else {
            addressView.setText(getEmptyFieldMessage());
        }
    }

    public void addVideo(Context context, View view) {
        TextView textView = view.findViewById(R.id.static_detail_producer_video);
        String videoId = producer.getYouTubeVideoId();
        if (producer.hasVideo()) {
            textView.setText("Ver video");
            textView.setOnClickListener(openVideo(context, videoId));

        } else {
            textView.setText(getEmptyFieldMessage());
            int color_value = Color.parseColor("#FF757575");
            textView.setTextColor(color_value);
        }
    }

    public View.OnClickListener openVideo(Context context, String videoId) {
        return view -> {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoId));
            try {
                context.startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                context.startActivity(webIntent);
            }
            getParentFragmentManager().popBackStack();

        };
    }

    public String getEmptyFieldMessage() {
        return "No posee";
    }
}