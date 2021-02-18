package laboratorio.app.fragments.forms.purchase.items;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import laboratorio.app.R;
import laboratorio.app.helpers.FragmentLoader;


public class DeliveryMethodFormFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivery_method_form, container, false);

        initRadioGroup(view);

        return view;

    }

    private void initRadioGroup(View view) {
        RadioGroup checkboxs = view.findViewById(R.id.delivery_method_checkboxs_layout);

        checkboxs.setOnCheckedChangeListener((radioGroup, elementChecked) -> {
            Fragment fragmentChecked = (elementChecked == R.id.node_check) ?
                    new NodeFormFragment() :
                    new DeliveryFormFragment();

            loadFragment(fragmentChecked);
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragment(fragment, R.id.delivery_method_container);
    }
}