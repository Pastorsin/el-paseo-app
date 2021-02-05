package laboratorio.app.fragments.forms;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.databinding.FragmentAddressFormBinding;
import laboratorio.app.viewmodels.AddressViewModel;
import laboratorio.app.viewmodels.UserViewModel;

public class ResidencyAddressFormFragment extends AddressFormFragment {
    @Override
    protected ViewModel setViewmodelToDataBinding(ViewDataBinding binding, ViewModelProvider viewModelProvider) {
        AddressViewModel viewModel = viewModelProvider.get(UserViewModel.class).residencyAddress;
        ((FragmentAddressFormBinding) binding).setViewmodel(viewModel);

        return viewModel;
    }

    @Override
    protected AddressViewModel getViewModel() {
        return getViewModelProvider().get(UserViewModel.class).residencyAddress;
    }
}
