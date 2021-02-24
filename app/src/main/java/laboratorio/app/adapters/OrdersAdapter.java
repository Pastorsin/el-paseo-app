package laboratorio.app.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import laboratorio.app.R;
import laboratorio.app.helpers.OnItemListener;
import laboratorio.app.models.Cart;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private final List<Cart> ordersList;
    private final OnItemListener onItemListener;

    public OrdersAdapter(List<Cart> ordersList, OnItemListener mOnItemListener) {
        this.ordersList = ordersList;
        this.onItemListener = mOnItemListener;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_delivery_orders_list_item, viewGroup, false);

        return new OrdersAdapter.ViewHolder(view, this.onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {
        Cart order = ordersList.get(position);
        if(order.hasSaleDate()){
            Date saleDate = order.getSaleDate();
            holder.getSaleDateInput().setText(getDate(saleDate));
            holder.getSaleTimeInput().setText(getTime(saleDate));
        }

        holder.getPriceInput().setText(getOrderPrice(order));

        holder.getDescriptionInput().setText(getOrderDescription(order));
    }

    @NotNull
    private String getOrderDescription(Cart order) {
        if (order.hasPosibleDeliveryDate()) {
            Date deliveryDate = order.getPosibleDeliveryDate();
            return String.format("Llega el %s a las %s", getDate(deliveryDate), getTime(deliveryDate));
        }

        return "Entrega a domicilio";
    }

    @NotNull
    private String getOrderPrice(Cart order) {
        return String.format("%.2f", order.getTotal());
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
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView saleDateInput;
        private final TextView saleTimeInput;
        private final TextView descriptionInput;
        private final TextView priceInput;
        private final OnItemListener onItemListener;


        public ViewHolder(@NonNull View view, OnItemListener onItemListener) {
            super(view);

            saleDateInput = view.findViewById(R.id.order_sale_date);
            saleTimeInput = view.findViewById(R.id.order_sale_time);
            descriptionInput = view.findViewById(R.id.order_description);
            priceInput = view.findViewById(R.id.order_price);

            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.onItemListener.onItemClick(getAdapterPosition());
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
