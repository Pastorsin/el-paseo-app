package laboratorio.app.fragments.forms.user.items;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.databinding.FragmentAddressFormBinding;
import laboratorio.app.viewmodels.AddressViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class DeliveryAddressFormFragment extends AddressFormFragment {
    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider viewModelProvider) {
        AddressViewModel viewModel = getViewModel();
        ((FragmentAddressFormBinding) binding).setViewmodel(viewModel);
    }

    @Override
    protected AddressViewModel getViewModel() {
        return getViewModelProvider().get(UserViewModel.class).deliveryAddress;
    }
}
