package laboratorio.app.viewmodels;

import com.zaferayan.creditcard.model.CreditCard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PurchaseViewModel extends ViewModel {
    public final MutableLiveData<String> cashValue = new MutableLiveData<>();
    public final MutableLiveData<CreditCard> creditCard = new MutableLiveData<>();
    public final MutableLiveData<Boolean> isCashChecked = new MutableLiveData<>(true);
}
