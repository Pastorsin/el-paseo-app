package laboratorio.app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import laboratorio.app.R;
import laboratorio.app.activities.DeliveryActivity;
import laboratorio.app.activities.MainActivity;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.auth.NoUserLoggedException;

public class DeliveryProfileFragment extends Fragment {

    public DeliveryProfileFragment() {
        // Required empty public constructor
    }

    public static DeliveryProfileFragment newInstance() {
        DeliveryProfileFragment fragment = new DeliveryProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_profile, container, false);

        initLogoutButton(view);

        return view;
    }
    private void initLogoutButton(View view) {
        Button logoutButton = view.findViewById(R.id.delivery_logout_button);
        logoutButton.setOnClickListener(buttonView -> {
            try {
                ApiSession.instance.logout(getContext());
                Log.d("LOGOUT", "User logged out successfully");

                // TODO: Consider if fragment still live cause exist SingleEvents into FormVM
                Intent mainActivity = new Intent(getContext(), MainActivity.class);
                startActivity(mainActivity);
                getActivity().finish();

            } catch (NoUserLoggedException e) {
                Log.e("LOGOUT", "No user logged");
                e.printStackTrace();
            }
        });
    }
}