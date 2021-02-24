package laboratorio.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import laboratorio.app.R;
import laboratorio.app.models.CartProduct;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder> {

    private List<CartProduct> detail;

    public ProductOrderAdapter(List<CartProduct> detail) {
        this.detail = detail;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView productNameInput;
        private final TextView productQuantityInput;
        private final TextView productPriceInput;

        public ViewHolder(View view) {
            super(view);

            productNameInput = view.findViewById(R.id.order_product_name);
            productQuantityInput = view.findViewById(R.id.order_product_quantity);
            productPriceInput = view.findViewById(R.id.order_product_price);
        }

        public TextView getProductNameInput() {
            return productNameInput;
        }

        public TextView getProductQuantityInput() {
            return productQuantityInput;
        }

        public TextView getProductPriceInput() {
            return productPriceInput;
        }

    }

    @NonNull
    @Override
    public ProductOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_product_list_item, viewGroup, false);

        return new ProductOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOrderAdapter.ViewHolder holder, int position) {
        CartProduct productDetail = detail.get(position);
        holder.getProductNameInput().setText(productDetail.getProduct().getTitle());
        holder.getProductQuantityInput().setText("" + productDetail.getQuantity());
        holder.getProductPriceInput().setText(String.format("%.2f", productDetail.getTotalCartProductPrice()));
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }
}
