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

public class PayMethodFormFragment extends OneOptionChooserFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPayMethodFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pay_method_form, container, false);

        PurchaseViewModel purchaseViewModel = new ViewModelProvider(requireActivity()).get(PurchaseViewModel.class);
        binding.setViewmodel(purchaseViewModel);

        View view = binding.getRoot();

        super.initRadioGroup(view);

        return view;
    }

    @Override
    protected int getLoadFragmentContainerId() {
        return R.id.pay_method_container;
    }

    @Override
    protected int getRadioGroupId() {
        return R.id.pay_method_checkboxs_layout;
    }

    @Override
    public Fragment getCheckedFragment(RadioGroup group, int checkedElement) {
        return (checkedElement == R.id.cash_check) ?
                new CashFormFragment() :
                new CreditCardFormFragment();
    }

    @Override
    protected Fragment getInitialFragment() {
        return new CashFormFragment();
    }
}