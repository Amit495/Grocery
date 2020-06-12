package aka.studios.shribalaji.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.interfaces.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView productImageView;
    public ImageView minus;
    public ImageView plus;
    public TextView qtyNum;
    public TextView productName;
    public TextView productNewPrice;
    public TextView productOldPrice;
    public TextView percentOff;
    public TextView productQty;
    public LinearLayout addBtn;
    public LinearLayout qtyLin;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productImageView = (ImageView) itemView.findViewById(R.id.productImageView);
        minus = (ImageView) itemView.findViewById(R.id.minus);
        plus = (ImageView) itemView.findViewById(R.id.plus);
        qtyNum = (TextView) itemView.findViewById(R.id.qtyNum);
        productName = (TextView) itemView.findViewById(R.id.productName);
        productNewPrice = (TextView) itemView.findViewById(R.id.productNewPrice);
        productOldPrice = (TextView) itemView.findViewById(R.id.productOldPrice);
        percentOff = (TextView) itemView.findViewById(R.id.percentOff);
        productQty = (TextView) itemView.findViewById(R.id.productQty);
        addBtn = (LinearLayout) itemView.findViewById(R.id.addBtn);
        qtyLin = (LinearLayout) itemView.findViewById(R.id.qtyLin);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
