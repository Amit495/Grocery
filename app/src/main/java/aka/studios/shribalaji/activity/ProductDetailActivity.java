package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.AttributeAdapter;
import aka.studios.shribalaji.adapter.ProductDetailSliderAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityProductDetailBinding;
import aka.studios.shribalaji.model.Attribute;
import aka.studios.shribalaji.model.Image;
import aka.studios.shribalaji.retrofit.ClientAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String TAG = "ProductDetailActivity";
    private ActivityProductDetailBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    // [START Banner]
    private ProductDetailSliderAdapter productImagesSliderAdapter;
    private ArrayList<Image> productImagesArrayList;
    // [END Banner]

    // [START Product Attribute]
    private AttributeAdapter attributeAdapter;
    private ArrayList<Attribute> attributeArrayList;
    private GridLayoutManager gridLayoutManager;
    // [END Product Attribute]

    private int productId;
    private int cartPrice;
    private int cartQuantity;
    private String product_name;
    private String product_code;
    private String cartColor;
    private String cartSize;
    private int cartProductId;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_product_detail);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(ProductDetailActivity.this);
        mobile = applicationPreference.getData("mobile");

        productImagesArrayList = new ArrayList<>();
        attributeArrayList = new ArrayList<>();

        gridLayoutManager = new GridLayoutManager(ProductDetailActivity.this, 2);
        binding.sizeRecyclerView.setHasFixedSize(true);
        binding.sizeRecyclerView.setLayoutManager(gridLayoutManager);

        if (getIntent() != null) {
            productId = getIntent().getIntExtra("productId", 0);

            getProductDetails(productId);
        }

        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

        if (loggedIn) {
            String mobile = applicationPreference.getData("mobile");
            getCartData(mobile);
        }

        binding.cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

                if (loggedIn) {
                    Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ProductDetailActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProductDetails(int productId) {
        binding.loadProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getProductInfo(productId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.loadProgressBar.setVisibility(View.GONE);
                        binding.detailCardView.setVisibility(View.VISIBLE);
                        String results = response.body().string();
                        Log.d(TAG, "DestailsResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject resultsJson = jsonObject.getJSONObject("results");

                            product_name = resultsJson.getString("product_name");
                            String description = resultsJson.getString("description");
                            String imageMain = resultsJson.getString("image");
                            binding.productName.setText(product_name);

                            binding.expandIcon.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (binding.productDescription.getVisibility() == View.GONE) {
                                        TransitionManager.beginDelayedTransition(binding.detailCardView, new AutoTransition());
                                        binding.productDescription.setVisibility(View.VISIBLE);
                                        binding.productDescription.setText(description);
                                        binding.expandIcon.setBackgroundResource(R.drawable.ic_arrow_up);
                                    } else {
                                        TransitionManager.beginDelayedTransition(binding.detailCardView, new AutoTransition());
                                        binding.productDescription.setVisibility(View.GONE);
                                        binding.expandIcon.setBackgroundResource(R.drawable.ic_arrow_down);
                                    }
                                }
                            });

                            JSONArray imagesArray = resultsJson.getJSONArray("images");
                            for (int i=0; i<imagesArray.length(); i++) {
                                JSONObject imageData = imagesArray.getJSONObject(i);

                                int id = imageData.getInt("id");
                                int product_id = Integer.parseInt(imageData.getString("product_id"));
                                String image = imageData.getString("image");

                                productImagesArrayList.add(new Image(id,
                                        product_id,
                                        image));
                            }
                            if (productImagesArrayList.size() > 0) {
                                binding.productImagesViewPager.setVisibility(View.VISIBLE);
                                binding.dotsIndicator.setVisibility(View.VISIBLE);
                                binding.productImg.setVisibility(View.GONE);
                                productImagesSliderAdapter = new ProductDetailSliderAdapter(ProductDetailActivity.this, productImagesArrayList);
                                binding.productImagesViewPager.setAdapter(productImagesSliderAdapter);
                                binding.dotsIndicator.setViewPager(binding.productImagesViewPager);
                            }
                            else {
                                binding.productImagesViewPager.setVisibility(View.GONE);
                                binding.dotsIndicator.setVisibility(View.GONE);
                                binding.productImg.setVisibility(View.VISIBLE);
                                Picasso.get().load(Common.BASE_URL + imageMain).into(binding.productImg);
                            }

                            JSONArray attributeArray = resultsJson.getJSONArray("attributes");
                            for (int i=0; i<attributeArray.length(); i++) {
                                JSONObject attributeData = attributeArray.getJSONObject(i);

                                int id = attributeData.getInt("id");
                                int product_id = attributeData.getInt("product_id");
                                String sku = attributeData.getString("sku");
                                String size = attributeData.getString("size");
                                int price = attributeData.getInt("price");
                                int old_price = attributeData.getInt("oldPrice");
                                int stock = attributeData.getInt("stock");

                                attributeArrayList.add(new Attribute(id,
                                        product_id,
                                        sku,
                                        size,
                                        price,
                                        old_price,
                                        stock));
                            }
                            attributeAdapter = new AttributeAdapter(ProductDetailActivity.this, ProductDetailActivity.this, attributeArrayList);
                            attributeAdapter.notifyDataSetChanged();
                            binding.sizeRecyclerView.setAdapter(attributeAdapter);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loadProgressBar.setVisibility(View.GONE);
                binding.detailCardView.setVisibility(View.GONE);
            }
        });
    }

    private void getCartData(String mobile) {
        Call<ResponseBody> call = clientAPI.getCart(mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            String cartItems = String.valueOf(jsonObject.getInt("cartItems"));

                            if (jsonObject.getInt("cartItems") > 0) {
//                                binding.badge.setVisibility(View.VISIBLE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        binding.badge.setText(cartItems);
                                        binding.badge.setText(cartItems);
                                        binding.badge.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            else {
                                binding.badge.setVisibility(View.GONE);
                            }
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

    @SuppressLint("SetTextI18n")
    public void getDetails(int position) {
        Attribute attribute = attributeArrayList.get(position);
        String price = String.valueOf(attribute.getPrice());
        String oldPrice = String.valueOf(attribute.getOldPrice());

        float old_price = attribute.getOldPrice();
        float new_price = attribute.getPrice();
        float diff = old_price - new_price;
        float percent = diff / old_price * 100;
        int percentage = (int) percent;
        String priceOff = String.valueOf(percentage);

        binding.productNewPrice.setText("₹ " + price);
        binding.productOldPrice.setText("₹ " + oldPrice);
        binding.percentOff.setText(priceOff + "% OFF");
        binding.productOldPrice.setPaintFlags(binding.productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if (oldPrice.equals("0")) {
            binding.productOldPrice.setVisibility(View.GONE);
            binding.percentOff.setVisibility(View.GONE);
        }
        else {
            binding.productOldPrice.setVisibility(View.VISIBLE);
            binding.percentOff.setVisibility(View.VISIBLE);
        }

        cartPrice = attribute.getPrice();
        cartSize = attribute.getSize();
        cartProductId = attribute.getProduct_id();

        Log.d(TAG, "ADDEDRES: " + attribute.getProduct_id());
        Log.d(TAG, "ADDEDRES: " + product_name);
        Log.d(TAG, "ADDEDRES: " + attribute.getSku());
        Log.d(TAG, "ADDEDRES: " + attribute.getSize());
        Log.d(TAG, "ADDEDRES: " + attribute.getPrice());
        Log.d(TAG, "ADDEDRES: " + mobile);

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
                if (!loggedIn) {
                    Intent intent = new Intent(ProductDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Call<ResponseBody> call = clientAPI.addToCart(attribute.getProduct_id(), product_name,
                            attribute.getSku(), attribute.getSize(), attribute.getPrice(), 1, mobile);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                try {
//                            addToCartProgress.setVisibility(View.GONE);
                                    String results = response.body().string();
                                    JSONObject jsonObject = new JSONObject(results);
                                    if (!jsonObject.getBoolean("error")) {
                                        Toast.makeText(ProductDetailActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        String mobile = applicationPreference.getData("mobile");
                                        getCartData(mobile);
                                    }
//                            addToCart.setText("Go To Cart");
//                            addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
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
            }
        });
    }
}
