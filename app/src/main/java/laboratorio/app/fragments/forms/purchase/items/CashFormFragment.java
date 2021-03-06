package laboratorio.app.fragments.forms.purchase.items;

import androidx.databinding.ViewDataBinding;

import android.view.View;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentCashFormBinding;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;
import laboratorio.app.models.UserCart;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class CashFormFragment extends ItemMultiSteperFormFragment {

    private UserCart userCart = UserCart.instance;

    private PurchaseViewModel purchaseViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cash_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentCashFormBinding cashFormBinding = (FragmentCashFormBinding) binding;

        purchaseViewModel = provider.get(PurchaseViewModel.class);
        cashFormBinding.setViewmodel(purchaseViewModel);
    }

    @Override
    protected void initValidators(View view) {
        EditText cashInput = view.findViewById(R.id.cash_input);
        validator.addValidation(cashInput, cashValidator(), cashError());
    }

    private String cashError() {
        return String.format(getString(R.string.cash_error_format), userCart.getTotal());
    }

    @NotNull
    private SimpleCustomValidation cashValidator() {
        return cashValueText -> {
            if (cashValueText.isEmpty())
                return false;

            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

            try {
                double cashValue = format.parse(cashValueText).doubleValue();
                return cashValue >= userCart.getTotal();
            } catch (ParseException e) {
                return false;
            }
        };
    }
}