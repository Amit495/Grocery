package aka.studios.shribalaji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.activity.ProductActivity;
import aka.studios.shribalaji.activity.ProductDetailActivity;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.interfaces.ItemClickListener;
import aka.studios.shribalaji.model.Product;
import aka.studios.shribalaji.retrofit.ClientAPI;
import aka.studios.shribalaji.viewholder.ProductViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    ClientAPI clientAPI;
    ApplicationPreference applicationPreference;
    int minteger;

    private Context context;
    private ProductActivity activity;
    private ArrayList<Product> productArrayList;

    public ProductAdapter(Context context, ProductActivity activity, ArrayList<Product> productArrayList) {
        this.context = context;
        this.activity = activity;
        this.productArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(context);

        String price = String.valueOf(product.getPrice());
        String oldPrice = String.valueOf(product.getOldPrice());
        String size = product.getSize();

        float old_price = product.getOldPrice();
        float new_price = product.getPrice();
        float diff = old_price - new_price;
        float percent = diff / old_price * 100;
        int percentage = (int) percent;
        String priceOff = String.valueOf(percentage);

        holder.productNewPrice.setText("₹ " + price);
        holder.productOldPrice.setText("₹ " + oldPrice);
        holder.percentOff.setText(priceOff + "% OFF");
        holder.productOldPrice.setPaintFlags(holder.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.productName.setText(product.getProduct_name());
        holder.productQty.setText(size);

        Picasso.get().load(Common.BASE_URL + product.getImage()).into(holder.productImageView);

        if (oldPrice.equals("0")) {
            holder.productOldPrice.setVisibility(View.GONE);
            holder.percentOff.setVisibility(View.GONE);
        }
        else {
            holder.productOldPrice.setVisibility(View.VISIBLE);
            holder.percentOff.setVisibility(View.VISIBLE);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
//                activity.getProductInfo(position);
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId", product.getProduct_id());
                context.startActivity(intent);
            }
        });

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.addCart(position);
                notifyDataSetChanged();
            }
        });

        String mobile = applicationPreference.getData("mobile");
        Call<ResponseBody> call = clientAPI.cartItemById(mobile, product.getProduct_id(), product.getSize());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        Log.d("ITEm", "REsultCrat: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject data = jsonObject.getJSONObject("results");

                            minteger = data.getInt("quantity");
                            String qty = String.valueOf(data.getInt("quantity"));
                            holder.qtyNum.setText(qty);
                            holder.addBtn.setVisibility(View.GONE);
                            holder.qtyLin.setVisibility(View.VISIBLE);
                        }
                        else {
                            holder.addBtn.setVisibility(View.VISIBLE);
                            holder.qtyLin.setVisibility(View.GONE);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.updateItem(position, minteger + 1);
                notifyDataSetChanged();

                String mobile = applicationPreference.getData("mobile");
                Call<ResponseBody> call = clientAPI.cartItemById(mobile, product.getProduct_id(), product.getSize());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            try {
                                String results = response.body().string();
                                Log.d("ITEm", "REsultCrat: " + results);
                                JSONObject jsonObject = new JSONObject(results);
                                if (!jsonObject.getBoolean("error")) {
                                    JSONObject data = jsonObject.getJSONObject("results");

                                    activity.updateItem(position, data.getInt("quantity") + 1);
                                    notifyDataSetChanged();

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.updateItem(position, minteger - 1);
                notifyDataSetChanged();

                String mobile = applicationPreference.getData("mobile");
                Call<ResponseBody> call = clientAPI.cartItemById(mobile, product.getProduct_id(), product.getSize());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            try {
                                String results = response.body().string();
                                Log.d("ITEm", "REsultCrat: " + results);
                                JSONObject jsonObject = new JSONObject(results);
                                if (!jsonObject.getBoolean("error")) {
                                    JSONObject data = jsonObject.getJSONObject("results");

                                    activity.updateItem(position, data.getInt("quantity") - 1);
                                    notifyDataSetChanged();

                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }
}
