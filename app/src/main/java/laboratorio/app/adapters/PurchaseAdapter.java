package laboratorio.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import laboratorio.app.R;
import laboratorio.app.models.AvailableNode;
import laboratorio.app.models.Cart;
import laboratorio.app.models.User;
import laboratorio.app.viewmodels.PurchaseListViewModel;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {
    private final List<Cart> purchaseList;
    private final PurchaseListViewModel viewmodel;

    public PurchaseAdapter(List<Cart> purchaseList, PurchaseListViewModel viewmodel) {
        this.purchaseList = purchaseList;
        this.viewmodel = viewmodel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.purchase_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart purchase = purchaseList.get(position);
        Date saleDate = purchase.getSaleDate();

        holder.getSaleDateInput().setText(getDate(saleDate));

        holder.getSaleTimeInput().setText(getTime(saleDate));

        holder.getPriceInput().setText(getPurchasePrice(purchase));

        holder.getDescriptionInput().setText(getPurchaseDescription(purchase));
    }

    @NotNull
    private String getPurchaseDescription(Cart purchase) {
        if (!purchase.isDeliverable()) {
            AvailableNode node = purchase.getNodeDate();
            return String.format("Retirar en %s el %s desde %s a %s", node.getNode().getName(),
                    getDate(node.getDay()), node.getDateTimeFrom(), node.getDateTimeTo());
        }

        if (purchase.hasPosibleDeliveryDate()) {
            Date deliveryDate = purchase.getPosibleDeliveryDate();
            return String.format("Llega el %s a las %s", getDate(deliveryDate), getTime(deliveryDate));
        }

        return "Entrega a domicilio";
    }

    @NotNull
    private String getPurchasePrice(Cart purchase) {
        return String.format("%.2f", purchase.getTotal());
    }

    @NotNull
    private String getTime(Date date) {
        return DateFormat.getTimeInstance().format(date);
    }

    @NotNull
    private String getDate(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

    @Override
    public int getItemCount() {
        return purchaseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView saleDateInput;
        private final TextView saleTimeInput;
        private final TextView descriptionInput;
        private final TextView priceInput;


        public ViewHolder(@NonNull View view) {
            super(view);

            view.setOnClickListener(this::onClick);

            saleDateInput = view.findViewById(R.id.purchase_sale_date);
            saleTimeInput = view.findViewById(R.id.purchase_sale_time);
            descriptionInput = view.findViewById(R.id.purchase_description);
            priceInput = view.findViewById(R.id.purchase_price);
        }

        private void onClick(View view) {
            Cart purchaseClicked = purchaseList.get(getAdapterPosition());
            viewmodel.targetPurchase.setValue(purchaseClicked);
        }

        public TextView getPriceInput() {
            return priceInput;
        }

        public TextView getDescriptionInput() {
            return descriptionInput;
        }

        public TextView getSaleTimeInput() {
            return saleTimeInput;
        }

        public TextView getSaleDateInput() {
            return saleDateInput;
        }
    }
}
