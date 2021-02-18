package laboratorio.app.fragments.forms.purchase.items;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import laboratorio.app.R;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;


public class ProductExtraInfoFormFragment extends ItemMultiSteperFormFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_extra_info_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider viewModel) {

    }

    @Override
    protected void initValidators(View view) {

    }
}