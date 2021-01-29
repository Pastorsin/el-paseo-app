package laboratorio.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.helpers.SingleLiveEvent;

public class FormViewModel extends ViewModel {
    public final SingleLiveEvent<Void> submitButtonPressed = new SingleLiveEvent<>();
    public final MutableLiveData<Boolean> isValid = new MutableLiveData<>();
}
