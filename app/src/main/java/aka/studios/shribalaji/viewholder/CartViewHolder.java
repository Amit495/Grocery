package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;

public class CartViewHolder extends RecyclerView.ViewHolder {

    public ImageView clearItem;
    public ImageView decrease_item;
    public ImageView increase_item;
    public TextView cartItemName;
    public TextView cartItemCode;
    public TextView cartItemPrice;
    public TextView cartItemSize;
    public TextView item_qty;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        clearItem = (ImageView) itemView.findViewById(R.id.clearItem);
        decrease_item = (ImageView) itemView.findViewById(R.id.decrease_item);
        increase_item = (ImageView) itemView.findViewById(R.id.increase_item);
        cartItemName = (TextView) itemView.findViewById(R.id.cartItemName);
        cartItemCode = (TextView) itemView.findViewById(R.id.cartItemCode);
        cartItemPrice = (TextView) itemView.findViewById(R.id.cartItemPrice);
        cartItemSize = (TextView) itemView.findViewById(R.id.cartItemSize);
        item_qty = (TextView) itemView.findViewById(R.id.item_qty);

    }
}
