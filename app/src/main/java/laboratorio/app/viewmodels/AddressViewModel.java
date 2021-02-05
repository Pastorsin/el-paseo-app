package laboratorio.app.viewmodels;


import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.models.Address;

public class AddressViewModel extends ViewModel {
    public final MutableLiveData<String> street = new MutableLiveData<>();
    public final MutableLiveData<String> number = new MutableLiveData<>();
    public final MutableLiveData<String> neighborhood = new MutableLiveData<>();
    public final MutableLiveData<String> apartament = new MutableLiveData<>();
    public final MutableLiveData<String> betweenStreets = new MutableLiveData<>();
    public final MutableLiveData<String> description = new MutableLiveData<>();
    public final MutableLiveData<String> floor = new MutableLiveData<>();

    public final MutableLiveData<Ubication> ubication = new MutableLiveData<>(new Ubication());
    public final MutableLiveData<String> lastAddressSearch = new MutableLiveData<>();

    public final MutableLiveData[] fields = {street, number, apartament, betweenStreets,
            description, floor, ubication};

    public AddressViewModel() {
        super();
        reset();
    }

    public Address getAddress() {
        return new Address(apartament.getValue(),
                number.getValue(),
                betweenStreets.getValue(),
                description.getValue(),
                floor.getValue(),
                ubication.getValue().getLatitude(),
                ubication.getValue().getLongitude(),
                street.getValue());
    }

    public String getFullAddressName() {

        return String.format("%s %s %s",
                getFieldValue(street),
                getFieldValue(number),
                getFieldValue(neighborhood));
    }

    @NotNull
    public String getFieldValue(MutableLiveData<String> field) {
        String value = field.getValue();
        return value == null ? "" : value;

    }

    public boolean isValidForm() {
        return getFullAddressName().equals(lastAddressSearch.getValue()) &&
                isUbicationDefined();
    }

    public void init(Address address) {
        street.setValue(
                address.getStreet());
        number.setValue(
                address.getNumber());
        apartament.setValue(
                address.getApartament());
        betweenStreets.setValue(
                address.getBetweenStreets());
        description.setValue(
                address.getDescription());
        floor.setValue(
                address.getFloor());
        ubication.setValue(
                new Ubication(address.getLatitude(), address.getLongitude()));
    }

    public void reset() {
        street.postValue(null);
        number.postValue(null);
        neighborhood.postValue(null);
        apartament.postValue(null);
        betweenStreets.postValue(null);
        description.postValue(null);
        floor.postValue(null);
        ubication.postValue(new Ubication());
    }

    public boolean isUbicationDefined() {
        return ubication != null && ubication.getValue().isDefined();
    }
}
