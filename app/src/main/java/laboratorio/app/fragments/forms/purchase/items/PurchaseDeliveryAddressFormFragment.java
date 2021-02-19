package laboratorio.app.fragments.forms.purchase.items;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.databinding.FragmentAddressFormBinding;
import laboratorio.app.fragments.forms.user.items.AddressFormFragment;
import laboratorio.app.viewmodels.AddressViewModel;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class PurchaseDeliveryAddressFormFragment extends AddressFormFragment {
    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        AddressViewModel viewModel = getViewModel();
        ((FragmentAddressFormBinding) binding).setViewmodel(viewModel);
    }

    @Override
    protected AddressViewModel getViewModel() {
        return getViewModelProvider().get(PurchaseViewModel.class).deliveryAddress;
    }
}
