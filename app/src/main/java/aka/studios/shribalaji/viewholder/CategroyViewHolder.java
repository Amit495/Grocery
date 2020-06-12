package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import aka.studios.shribalaji.R;
import aka.studios.shribalaji.interfaces.ItemClickListener;

public class CategroyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView categoryImageView;
    public TextView categoryTextView;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CategroyViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImageView = (ImageView) itemView.findViewById(R.id.categoryImageView);
        categoryTextView = (TextView) itemView.findViewById(R.id.categoryTextView);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
