package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.OrderItemsAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.model.OrderedItems;
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

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar ordersToolbar;
    private ClientAPI clientAPI;
    private ApplicationPreference applicationPreference;

    private TextView orderDate;
    private TextView orderId;
    private TextView orderTotal;
    private TextView orderItems;
    private TextView paymentMethod;
    private TextView fullName;
    private TextView address;
    private TextView landmark;
    private TextView city;
    private TextView pincode;
    private TextView state;
    private TextView mobile;
    private TextView proceedItems;
    private TextView cartTotal;
    private TextView gstPrice;
    private TextView shippingPrice;
    private TextView totalPrice;
    private TextView type;
    private ProgressBar itemsProgressBar;

    private ArrayList<OrderedItems> orderedItemsArrayList;
    private OrderItemsAdapter orderItemsAdapter;
    private RecyclerView itemsRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        ordersToolbar = (Toolbar) findViewById(R.id.ordersToolbar);
        setSupportActionBar(ordersToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(OrderDetailsActivity.this);

        orderedItemsArrayList = new ArrayList<>();

        orderDate = (TextView) findViewById(R.id.orderDate);
        orderId = (TextView) findViewById(R.id.orderId);
        orderTotal = (TextView) findViewById(R.id.orderTotal);
        orderItems = (TextView) findViewById(R.id.orderItems);
        paymentMethod = (TextView) findViewById(R.id.paymentMethod);
        fullName = (TextView) findViewById(R.id.fullName);
        address = (TextView) findViewById(R.id.address);
        landmark = (TextView) findViewById(R.id.landmark);
        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        state = (TextView) findViewById(R.id.state);
        mobile = (TextView) findViewById(R.id.mobile);
        proceedItems = (TextView) findViewById(R.id.proceedItems);
        cartTotal = (TextView) findViewById(R.id.cartTotal);
        gstPrice = (TextView) findViewById(R.id.gstPrice);
        shippingPrice = (TextView) findViewById(R.id.shippingPrice);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        type = (TextView) findViewById(R.id.type);
        itemsProgressBar = (ProgressBar) findViewById(R.id.itemsProgressBar);
        itemsRecyclerView = (RecyclerView) findViewById(R.id.itemsRecyclerView);

        linearLayoutManager = new LinearLayoutManager(OrderDetailsActivity.this);
        itemsRecyclerView.setHasFixedSize(true);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);

        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);

            getOrderDetails(id);
        }
    }

    private void getOrderDetails(int id) {
        itemsProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.orderDetails(id);
        call.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        orderedItemsArrayList.clear();
                        itemsProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject data = jsonObject.getJSONObject("results");

                            String odrDate = data.getString("created_at");
                            String ordId = data.getString("orderId");
                            String moble = data.getString("mobile");
                            String name = data.getString("name");
                            String add = data.getString("address");
                            String cty = data.getString("city");
                            String st = data.getString("state");
                            String pincde = data.getString("pincode");
                            String landmak = data.getString("landmark");
                            String typ = String.valueOf(data.getInt("type"));
                            String items = String.valueOf(data.getInt("items"));
                            String shippingCharges = String.valueOf(data.getInt("shippingCharges"));
                            String pymntMethod = data.getString("paymentMethod");
                            String grandTotal = String.valueOf(data.getInt("grandTotal"));

                            int ttl = data.getInt("grandTotal");
                            int shpmt = data.getInt("shippingCharges");
                            int itemTotal = ttl - shpmt;
                            String itemsTotal = String.valueOf(itemTotal);

                            orderDate.setText(odrDate);
                            orderId.setText(ordId);
                            orderTotal.setText("₹ " + grandTotal);
                            orderItems.setText("(" + items + " Items)");
                            paymentMethod.setText(pymntMethod);
                            fullName.setText(name);
                            address.setText(add);
                            landmark.setText(landmak);
                            city.setText(cty + "- ");
                            pincode.setText(pincde);
                            state.setText(st);
                            mobile.setText("+91 " + moble);

                            if (typ.equals("0")) {
                                type.setText("Home");
                            }
                            if (typ.equals("1")) {
                                type.setText("Work");
                            }
                            if (typ.equals("2")) {
                                type.setText("Other");
                            }

                            proceedItems.setText("Price Detials (" + items + " Items)");
                            cartTotal.setText("₹ " + itemsTotal);
                            gstPrice.setText("₹ 0");
                            shippingPrice.setText("₹ " + shippingCharges);
                            totalPrice.setText("₹ " + grandTotal);

                            JSONArray array = data.getJSONArray("orders");
                            for (int i=0; i<array.length(); i++) {
                                JSONObject orderData = array.getJSONObject(i);
                                OrderedItems orderedItems = new OrderedItems(orderData);
                                orderedItemsArrayList.add(orderedItems);
                            }
                            if (orderedItemsArrayList.size() > 0) {
                                orderItemsAdapter = new OrderItemsAdapter(OrderDetailsActivity.this, orderedItemsArrayList);
                                orderItemsAdapter.notifyDataSetChanged();
                                itemsRecyclerView.setAdapter(orderItemsAdapter);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                itemsProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
