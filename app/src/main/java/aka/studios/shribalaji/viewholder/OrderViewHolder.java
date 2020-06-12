package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.interfaces.ItemClickListener;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderDate;
    public TextView orderId;
    public TextView orderTotal;
    public TextView orderItems;
    public TextView viewItems;
    public TextView orderStatus;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderDate = (TextView) itemView.findViewById(R.id.orderDate);
        orderId = (TextView) itemView.findViewById(R.id.orderId);
        orderTotal = (TextView) itemView.findViewById(R.id.orderTotal);
        orderItems = (TextView) itemView.findViewById(R.id.orderItems);
        viewItems = (TextView) itemView.findViewById(R.id.viewItems);
        orderStatus = (TextView) itemView.findViewById(R.id.orderStatus);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
