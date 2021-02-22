package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import laboratorio.app.R;
import laboratorio.app.helpers.IntentManager;

public class StaticWelcomeFragment extends Fragment {

    public static final String ARG_OBJECT = "Bienvenido";


    public StaticWelcomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_static_welcome, container, false);

        IntentManager intentManager = new IntentManager(this);

        TextView phoneTextView = view.findViewById(R.id.welcome_phone_number);
        intentManager.setPhoneIntent(phoneTextView);

        TextView emailTextView = view.findViewById(R.id.welcome_email_textView);
        intentManager.setEmailIntent(emailTextView);

        TextView instagramTextView = view.findViewById(R.id.welcome_ig_textView);
        intentManager.setInstagramIntent(instagramTextView);

        return view;
    }


}