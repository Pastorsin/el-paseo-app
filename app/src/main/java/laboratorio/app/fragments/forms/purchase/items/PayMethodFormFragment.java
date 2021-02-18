package laboratorio.app.fragments.forms.purchase.items;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentPayMethodFormBinding;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.viewmodels.FormViewModel;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class PayMethodFormFragment extends Fragment {

    Fragment lastFragmentLoaded;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPayMethodFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_method_form, container, false);

        PurchaseViewModel purchaseViewModel = new ViewModelProvider(requireActivity()).get(PurchaseViewModel.class);
        binding.setViewmodel(purchaseViewModel);

        View view = binding.getRoot();

        initRadioGroup(view);

        return view;
    }

    private void initRadioGroup(View view) {
        RadioGroup radioGroup = view.findViewById(R.id.pay_method_checkboxs_layout);

        radioGroup.setOnCheckedChangeListener((group, checkedElement) -> {
            Fragment checkedFragment = (checkedElement == R.id.cash_check) ?
                    new CashFormFragment() :
                    new CreditCardFormFragment();

            loadFragment(checkedFragment);
        });

        loadFragment(new CashFormFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragment(fragment, R.id.pay_method_container);

        removeLastFragmentLoaded();
        lastFragmentLoaded = fragment;
    }

    private void removeLastFragmentLoaded() {
        if (lastFragmentLoaded != null) {
            FragmentLoader loader = (FragmentLoader) getActivity();
            loader.removeFragment(lastFragmentLoaded);
        }
    }
}