package laboratorio.app.fragments.forms.purchase.items;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentDeliveryMethodFormBinding;
import laboratorio.app.fragments.forms.user.items.DeliveryAddressFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.viewmodels.PurchaseViewModel;


public class DeliveryMethodFormFragment extends OneOptionChooserFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDeliveryMethodFormBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_method_form, container, false);

        PurchaseViewModel purchaseViewModel = new ViewModelProvider(requireActivity()).get(PurchaseViewModel.class);
        binding.setViewmodel(purchaseViewModel);

        View view = binding.getRoot();

        super.initRadioGroup(view);

        return view;
    }

    @Override
    protected int getLoadFragmentContainerId() {
        return R.id.delivery_method_container;
    }

    @Override
    protected int getRadioGroupId() {
        return R.id.delivery_method_radio_group;
    }

    @Override
    public Fragment getCheckedFragment(RadioGroup group, int checkedElement) {
        return (checkedElement == R.id.node_check) ?
                new NodeFormFragment() :
                new PurchaseDeliveryAddressFormFragment();
    }

    @Override
    protected Fragment getInitialFragment() {
        return new NodeFormFragment();
    }

}