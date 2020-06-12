package aka.studios.shribalaji.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.ProductActivity;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.interfaces.ItemClickListener;
import aka.studios.shribalaji.model.Category;
import aka.studios.shribalaji.viewholder.CategroyViewHolder;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategroyViewHolder> {

    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoryAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategroyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        return new CategroyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategroyViewHolder holder, int position) {
        Category category = categoryArrayList.get(position);

        holder.categoryTextView.setText(category.getName());
        Picasso.get().load(Common.BASE_URL + category.getImage()).into(holder.categoryImageView);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("url", category.getUrl());
                intent.putExtra("id", category.getId());
                intent.putExtra("categoryName", category.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }
}
