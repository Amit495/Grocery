package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;


public class OrderItemsViewHolder extends RecyclerView.ViewHolder {

    public TextView itemName;
    public TextView price;
    public TextView qty;
    public TextView size;

    public OrderItemsViewHolder(@NonNull View itemView) {
        super(itemView);

        itemName = (TextView) itemView.findViewById(R.id.itemName);
        price = (TextView) itemView.findViewById(R.id.price);
        qty = (TextView) itemView.findViewById(R.id.qty);
        size = (TextView) itemView.findViewById(R.id.size);
    }
}
