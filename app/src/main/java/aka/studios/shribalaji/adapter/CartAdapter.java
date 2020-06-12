package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.CartActivity;
import aka.studios.shribalaji.model.Cart;
import aka.studios.shribalaji.viewholder.CartViewHolder;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private Context context;
    private CartActivity activity;
    private ArrayList<Cart> cartArrayList;

    public CartAdapter(Context context, CartActivity activity, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.activity = activity;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartArrayList.get(position);

        String price = String.valueOf(cart.getPrice());
        String qty = String.valueOf(cart.getQuantity());

        holder.cartItemName.setText(cart.getProduct_name());
        holder.cartItemCode.setText(cart.getProduct_code());
        holder.cartItemPrice.setText("₹ " + price);
        holder.item_qty.setText(cart.getQuantity() + "");
        holder.cartItemSize.setText("Size: " + cart.getSize());
//        Picasso.get().load(Common.BASE_URL + category.getImage()).into(holder.categoryImageView);

        holder.clearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.clearCartItem(position);
                notifyDataSetChanged();
            }
        });

        holder.decrease_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.getCartUpdate(position, holder.qtyBtn.getNumber());
                activity.getCartUpdate(position, cart.getQuantity() - 1);
                notifyDataSetChanged();
            }
        });

        holder.increase_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.getCartUpdate(position, holder.qtyBtn.getNumber());
                activity.getCartUpdate(position, cart.getQuantity() + 1);
                notifyDataSetChanged();
            }
        });

//        activity.priceDetails(position);

//        holder.setItemClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int position, boolean isLongClick) {
//                activity.getCartUpdate(position, holder.qtyBtn.getNumber());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }
}
