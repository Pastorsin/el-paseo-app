package laboratorio.app.fragments.forms.purchase.items;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import laboratorio.app.R;
import laboratorio.app.adapters.ProductPurchaseAdapter;
import laboratorio.app.databinding.FragmentPurchaseDetailFormBinding;
import laboratorio.app.fragments.forms.user.items.ItemMultiSteperFormFragment;
import laboratorio.app.viewmodels.PurchaseViewModel;

public class PurchaseDetailFormFragment extends ItemMultiSteperFormFragment {

    PurchaseViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.products_list);
        recyclerView.setAdapter(new ProductPurchaseAdapter(viewModel.getPurchaseDetail()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_purchase_detail_form;
    }

    @Override
    protected void setViewmodelsToDataBinding(ViewDataBinding binding, ViewModelProvider provider) {
        viewModel = provider.get(PurchaseViewModel.class);
        ((FragmentPurchaseDetailFormBinding) binding).setViewmodel(viewModel);
    }

    @Override
    protected void initValidators(View view) {

    }
}