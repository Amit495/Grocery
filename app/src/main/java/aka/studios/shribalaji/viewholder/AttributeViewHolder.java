package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.interfaces.ItemClickListener;

public class AttributeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productSize;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public AttributeViewHolder(@NonNull View itemView) {
        super(itemView);

        productSize = (TextView) itemView.findViewById(R.id.productSize);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
