package laboratorio.app.fragments.forms.purchase;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.auth.ApiSession;
import laboratorio.app.fragments.ErrorFragment;
import laboratorio.app.fragments.SuccessPurchaseFragment;
import laboratorio.app.fragments.forms.MultiStepperFormFragment;
import laboratorio.app.helpers.FragmentLoader;
import laboratorio.app.models.UserCart;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class PurchaseFragment extends MultiStepperFormFragment {
    final private static int TOTAL_STEPS = 4;
    private PurchaseViewModel viewmodel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        initViewModels();

        return view;
    }

    private void initViewModels() {
        viewmodel = new ViewModelProvider(requireActivity()).get(PurchaseViewModel.class);
        ApiSession.instance.getUserLogged(getContext()).observe(
                getViewLifecycleOwner(), user -> {
                    if (user == null)
                        onUserLoggedError();
                    else
                        viewmodel.initCreate(UserCart.instance, user);
                });
    }

    private void onUserLoggedError() {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragmentOnMainContainer(new ErrorFragment());
        Toast.makeText(getContext(), R.string.not_user_logged_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getMenuId() {
        return R.menu.menu_purchase_stepform;
    }

    @Override
    public int getNavGraphId() {
        return R.menu.purchase_nav_stepper;
    }

    @Override
    public void onCompleted() {
        saveCart();
    }

    private void saveCart() {
        ApiSession.instance.getToken(getContext(), accountManagerFuture -> {
            try {
                String token = accountManagerFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);

                viewmodel.postCart(token).observe(getViewLifecycleOwner(), cart -> {
                    if (cart == null) {
                        onPurchaseError();
                        Log.e("CART", "Request failed for complete the buy");
                    } else {
                        onSuccessPurchase();
                        Log.d("CART", "Buy completed succesfully");
                    }
                });

            } catch (AuthenticatorException | OperationCanceledException | IOException e) {
                onPurchaseError();
                Log.e("CART", "No user logged for complete the buy", e);
            }
        });
    }

    private void onPurchaseError() {
        Toast.makeText(getContext(), "Ha ocurrido un error, intente de nuevo porfavor", Toast.LENGTH_SHORT).show();
    }

    private void onSuccessPurchase() {
        FragmentLoader loader = (FragmentLoader) getActivity();
        loader.replaceFragmentOnMainContainer(new SuccessPurchaseFragment());
        viewmodel.resetAll();
    }

    @Override
    public int getTotalSteps() {
        return TOTAL_STEPS;
    }
}
