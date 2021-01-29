package laboratorio.app.viewmodels;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.models.Address;

public class AddressViewModel extends ViewModel {
    public final MutableLiveData<String> street = new MutableLiveData<>();
    public final MutableLiveData<String> number = new MutableLiveData<>();
    public final MutableLiveData<String> neighborhood = new MutableLiveData<>();
    public final MutableLiveData<String> apartament = new MutableLiveData<>();
    public final MutableLiveData<String> between_streets = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> floor = new MutableLiveData<>();

    public final MutableLiveData<String> addressRequest = new MutableLiveData<>();
    public final MutableLiveData<android.location.Address> addressResponse = new MutableLiveData<>();

    public Address getAddress() {
        return new Address(apartament.getValue(),
                number.getValue(),
                between_streets.getValue(),
                description.getValue(),
                floor.getValue(),
                addressResponse.getValue().getLatitude(),
                addressResponse.getValue().getLongitude(),
                street.getValue());
    }

    public String getFullAddressName() {
        return String.format("%s %s %s",
                street.getValue(),
                number.getValue(),
                neighborhood.getValue());
    }

    public boolean isValidForm() {
        return getFullAddressName().equals(addressRequest.getValue()) &&
                addressResponse.getValue() != null;
    }
}
