package aka.studios.shribalaji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.OrderDetailsActivity;
import aka.studios.shribalaji.interfaces.ItemClickListener;
import aka.studios.shribalaji.model.Order;
import aka.studios.shribalaji.viewholder.OrderViewHolder;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orderArrayList;

    public OrderAdapter(Context context, ArrayList<Order> orderArrayList) {
        this.context = context;
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new OrderViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderArrayList.get(position);

        String orderDate = order.getCreated_at();
        String orderId = order.getOrderId();
        String orderTotal = String.valueOf(order.getGrandTotal());
        String orderItems = String.valueOf(order.getItems());
        String orderStatus = order.getOrderStatus();

        if (orderStatus.equals("1")) {
            holder.orderStatus.setText("Placed");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.placed_rectangle));
        }
        if (orderStatus.equals("2")) {
            holder.orderStatus.setText("Pending");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.pending_rectangle));
        }
        if (orderStatus.equals("3")) {
            holder.orderStatus.setText("Canceled");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.reject_rectangle));
        }
        if (orderStatus.equals("4")) {
            holder.orderStatus.setText("In Process");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.process_rectangle));
        }
        if (orderStatus.equals("5")) {
            holder.orderStatus.setText("Shipping");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.shipping_rectangle));
        }
        if (orderStatus.equals("6")) {
            holder.orderStatus.setText("Delivered");
            holder.orderStatus.setTextColor(Color.WHITE);
            holder.orderStatus.setBackground(context.getResources().getDrawable(R.drawable.green_rectangle));
        }

        holder.orderDate.setText(orderDate);
        holder.orderId.setText(orderId);
        holder.orderTotal.setText("₹ " + orderTotal);
        holder.orderItems.setText("(" + orderItems + " Items)");

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("id", order.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();
    }
}
