package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.ProductDetailActivity;
import aka.studios.shribalaji.interfaces.ItemClickListener;
import aka.studios.shribalaji.model.Attribute;
import aka.studios.shribalaji.viewholder.AttributeViewHolder;

import java.util.ArrayList;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeViewHolder> {

    private Context context;
    private ProductDetailActivity activity;
    private ArrayList<Attribute> attributeArrayList;

    int currentPosition = 0;

    public AttributeAdapter(Context context, ProductDetailActivity activity, ArrayList<Attribute> attributeArrayList) {
        this.context = context;
        this.activity = activity;
        this.attributeArrayList = attributeArrayList;
    }

    @NonNull
    @Override
    public AttributeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_size_layout, parent, false);
        return new AttributeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttributeViewHolder holder, int position) {
        Attribute attribute = attributeArrayList.get(position);

        String size = attribute.getSize();
        holder.productSize.setText(size);

        if (currentPosition == position) {
            activity.getDetails(position);
            holder.productSize.setTextColor(Color.WHITE);
            holder.productSize.setBackground(context.getResources().getDrawable(R.drawable.select_rectangle));
        }
        else {
            holder.productSize.setTextColor(Color.BLACK);
            holder.productSize.setBackground(context.getResources().getDrawable(R.drawable.white_rectangle));
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                currentPosition = position;
                activity.getDetails(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return attributeArrayList.size();
    }
}
