package laboratorio.app.viewmodels;

import android.location.Address;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddressViewModel extends ViewModel {
    public final MutableLiveData<String> street = new MutableLiveData<>();
    public final MutableLiveData<String> neighborhood = new MutableLiveData<>();

    public final MutableLiveData<String> addressRequest = new MutableLiveData<>();
    public final MutableLiveData<Address> addressResponse = new MutableLiveData<>();
}
