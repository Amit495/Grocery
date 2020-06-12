package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import aka.studios.shribalaji.adapter.CartAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityCartBinding;
import aka.studios.shribalaji.model.Cart;
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

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";
    private ActivityCartBinding binding;
    private ClientAPI clientAPI;
    private ApplicationPreference applicationPreference;

    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartArrayList;
    private LinearLayoutManager cartLinearLayoutManager;
    private int cartTotal = 0;
    private int total = 0;
    private String shipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(CartActivity.this);
        cartArrayList = new ArrayList<>();

        cartLinearLayoutManager = new LinearLayoutManager(CartActivity.this);
        binding.cartRecyclerView.setHasFixedSize(true);
        binding.cartRecyclerView.setLayoutManager(cartLinearLayoutManager);

        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

        if (!loggedIn) {
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            String mobile = applicationPreference.getData("mobile");
            getCartData(mobile);
        }

        binding.proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, ProceedActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getCartData(String mobile) {
        binding.loadingImageView.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getCart(mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        cartArrayList.clear();
                        binding.loadingImageView.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "CartListResults" + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject dataJson = jsonArray.getJSONObject(i);
                                Cart cart = new Cart(dataJson);
//                                price = price + (cart.getPrice() * cart.getQuantity());

                                cartArrayList.add(cart);

//                                int id = dataJson.getInt("id");
//                                int product_id = dataJson.getInt("product_id");
//                                String product_name = dataJson.getString("product_name");
//                                String product_code = dataJson.getString("product_code");
//                                String size = dataJson.getString("size");
//                                int price = dataJson.getInt("price");
//                                int quantity = dataJson.getInt("quantity");
//                                String mobile = dataJson.getString("mobile");

//                                cartArrayList.add(new Cart(id,
//                                        product_id,
//                                        product_name,
//                                        product_code,
//                                        size,
//                                        price,
//                                        quantity,
//                                        mobile));
                            }

                            if (cartArrayList.size() > 0) {
                                binding.emptyLinear.setVisibility(View.GONE);
                                binding.cartLinear.setVisibility(View.VISIBLE);
                                binding.cartProcessLinear.setVisibility(View.VISIBLE);
                                cartAdapter = new CartAdapter(CartActivity.this, CartActivity.this, cartArrayList);
                                cartAdapter.notifyDataSetChanged();
                                binding.cartRecyclerView.setAdapter(cartAdapter);
                            }
                            else {
                                binding.emptyLinear.setVisibility(View.VISIBLE);
                                binding.cartLinear.setVisibility(View.GONE);
                                binding.cartProcessLinear.setVisibility(View.GONE);
                                binding.cartItemsText.setText("0 Items");
                            }

                            String cartItems = String.valueOf(jsonObject.getInt("cartItems"));
                            cartTotal = jsonObject.getInt("cartTotal");
                            total = jsonObject.getInt("total");
                            binding.cartItemsText.setText(cartItems + " Items");
                            binding.cartItems.setText("Price Details (" + cartItems + " Items)");

                            JSONObject charges = jsonObject.getJSONObject("charges");
                            shipping = String.valueOf(charges.getInt("shipping"));

                            priceDetails();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.loadingImageView.setVisibility(View.GONE);
                binding.notFoundAnimation.setVisibility(View.VISIBLE);
            }
        });
    }

    public void getCartUpdate(int position, int quantity) {
        binding.updateProgress.setVisibility(View.VISIBLE);

        Cart cart = cartArrayList.get(position);

        int id = cart.getId();
        String product_code = cart.getProduct_code();
        String size = cart.getSize();
        int price = cart.getPrice();

        Log.d(TAG, "CartUpdate: " + quantity);

        Call<ResponseBody> call = clientAPI.cartUpdate(id, product_code, size, price, quantity);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.updateProgress.setVisibility(View.GONE);
                        binding.loadingImageView.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "CartUpdateResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            getCartData(applicationPreference.getData("mobile"));
                            priceDetails();
                        }
                        else {
                            getCartData(applicationPreference.getData("mobile"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.updateProgress.setVisibility(View.GONE);
                binding.loadingImageView.setVisibility(View.GONE);
                binding.notFoundAnimation.setVisibility(View.GONE);
            }
        });
    }

    private void priceDetails() {
//        Cart cart = cartArrayList.get(position);

//        price = price + (cart.getPrice() * cart.getQuantity());

        Log.d(TAG, "CartTotal: " + cartTotal);
        String cartTtl = String.valueOf(cartTotal);

        binding.cartTotal.setText("₹ " + cartTtl);
        String totalPrice = String.valueOf(total);
        binding.totalPrice.setText("₹ " + totalPrice);
        binding.cartAllTotal.setText("₹ " + totalPrice);
        binding.shippingPrice.setText("₹ " + shipping);
        binding.gstPrice.setText("₹ 0");
    }

    public void clearCartItem(int position) {
        binding.updateProgress.setVisibility(View.VISIBLE);
        Cart cart = cartArrayList.get(position);

        int id = cart.getId();
        int price = cart.getPrice();

        Call<ResponseBody> call = clientAPI.clearItem(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.updateProgress.setVisibility(View.GONE);
                        binding.loadingImageView.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "CartUpdateResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            getCartData(applicationPreference.getData("mobile"));
                            priceDetails();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.updateProgress.setVisibility(View.GONE);
                binding.loadingImageView.setVisibility(View.GONE);
                binding.notFoundAnimation.setVisibility(View.GONE);
            }
        });
    }
}
