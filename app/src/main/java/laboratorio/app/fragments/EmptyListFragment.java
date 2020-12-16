package laboratorio.app.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import laboratorio.app.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmptyListFragment extends Fragment {

    public EmptyListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_list, container, false);
    }
}