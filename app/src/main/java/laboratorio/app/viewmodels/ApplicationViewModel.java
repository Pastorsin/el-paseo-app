package laboratorio.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import laboratorio.app.helpers.SingleLiveEvent;

public class ApplicationViewModel extends ViewModel {
    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    public final SingleLiveEvent<Void> isError = new SingleLiveEvent<>();
}
