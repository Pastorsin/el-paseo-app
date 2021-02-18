package laboratorio.app.fragments.forms.purchase.items;

import androidx.databinding.ViewDataBinding;

import android.view.View;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentCashFormBinding;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;
import laboratorio.app.models.Cart;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class CashFormFragment extends ItemMultiSteperFormFragment {

    private Cart cart = Cart.instance;

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
        return String.format(getString(R.string.cash_error_format), cart.getTotal());
    }

    @NotNull
    private SimpleCustomValidation cashValidator() {
        return cashValueText -> {
            if (cashValueText.isEmpty())
                return false;

            Double cashValue = Double.valueOf(cashValueText);
            return cashValue >= cart.getTotal();
        };
    }
}