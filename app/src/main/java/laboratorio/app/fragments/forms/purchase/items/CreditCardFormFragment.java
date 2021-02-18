package laboratorio.app.fragments.forms.purchase.items;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.zaferayan.creditcard.model.CreditCard;
import com.zaferayan.creditcard.view.CreditCardView;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.databinding.FragmentCreditCardFormBinding;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;
import laboratorio.app.viewmodels.PurchaseViewModel;


public class CreditCardFormFragment extends ItemMultiSteperFormFragment {

    @NotNull
    private EditText[] getInputs(CreditCardView creditCard) {
        return new EditText[]{
                creditCard.etCcExpireMonth, creditCard.etCcExpireYear, creditCard.etCcName,
                creditCard.etCcNumber1, creditCard.etCcNumber2, creditCard.etCcNumber3,
                creditCard.etCcNumber4, creditCard.etCvv
        };
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_credit_card_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        FragmentCreditCardFormBinding creditCardFormBinding = (FragmentCreditCardFormBinding) binding;

        PurchaseViewModel purchaseViewModel = provider.get(PurchaseViewModel.class);
        creditCardFormBinding.setViewmodel(purchaseViewModel);

        CreditCardView creditCardView = creditCardFormBinding.ccView;

        // Methods to solve two-data binding problem for third views
        CreditCard creditCardModel = purchaseViewModel.creditCard.getValue();
        setInitialInputs(creditCardView, creditCardModel);
        watchCardInputs(purchaseViewModel, creditCardView);
    }

    private void setInitialInputs(CreditCardView creditCardView, CreditCard creditCardModel) {
        if (creditCardModel != null) {
            setNumbersInputs(creditCardView, creditCardModel);
            creditCardView.etCcName.setText(creditCardModel.getName());
            creditCardView.etCcExpireMonth.setText(creditCardModel.getExpirationMonth());
            creditCardView.etCcExpireYear.setText(creditCardModel.getExpirationYear());
            creditCardView.etCvv.setText(creditCardModel.getCvv());
        }
    }

    private void setNumbersInputs(CreditCardView creditCardView, CreditCard creditCardModel) {
        EditText[] numberInputs = {
                creditCardView.etCcNumber1, creditCardView.etCcNumber2,
                creditCardView.etCcNumber3, creditCardView.etCcNumber4
        };

        String number = creditCardModel.getNumber();

        for (int i = 0; i < numberInputs.length; i++) {
            if (number.length() >= i * 4)
                numberInputs[i].setText(number.substring(i * 4));
        }
    }

    private void watchCardInputs(PurchaseViewModel purchaseViewModel, CreditCardView creditCardView) {
        for (EditText input : getInputs(creditCardView))
            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    purchaseViewModel.creditCard.setValue(creditCardView.getCreditCardInfo());
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
    }

    @Override
    protected void initValidators(View view) {
        CreditCardView creditCardView = view.findViewById(R.id.ccView);
        for (EditText input : getInputs(creditCardView))
            validator.addValidation(input, StringUtils::isNotBlank, getString(R.string.empty_field_error));
    }
}