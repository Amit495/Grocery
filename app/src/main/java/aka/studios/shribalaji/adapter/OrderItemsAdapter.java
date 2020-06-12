package aka.studios.shribalaji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.model.OrderedItems;
import aka.studios.shribalaji.viewholder.OrderItemsViewHolder;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsViewHolder> {

    private Context context;
    private ArrayList<OrderedItems> orderedItemsArrayList;

    public OrderItemsAdapter(Context context, ArrayList<OrderedItems> orderedItemsArrayList) {
        this.context = context;
        this.orderedItemsArrayList = orderedItemsArrayList;
    }

    @NonNull
    @Override
    public OrderItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new OrderItemsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderItemsViewHolder holder, int position) {
        OrderedItems orderedItems = orderedItemsArrayList.get(position);

        String name = orderedItems.getProduct_name();
        String qty = String.valueOf(orderedItems.getProduct_qty());
        String size = orderedItems.getProduct_size();
        String price = String.valueOf(orderedItems.getProduct_price());

        holder.itemName.setText(name);
        holder.price.setText("₹ " + price);
        holder.qty.setText(qty);
        holder.size.setText(size);
    }

    @Override
    public int getItemCount() {
        return orderedItemsArrayList.size();
    }
}
