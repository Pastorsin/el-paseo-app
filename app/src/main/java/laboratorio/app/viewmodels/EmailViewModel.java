package laboratorio.app.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {
    public final MutableLiveData<String> email = new MutableLiveData<>();

}
