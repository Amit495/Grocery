package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.OrderAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
//import com.example.eatnow.databinding.ActivityMyOrdersBinding;
import aka.studios.shribalaji.model.Order;
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

public class MyOrdersActivity extends AppCompatActivity {

//    private ActivityMyOrdersBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    private Toolbar ordersToolbar;
    private RecyclerView ordersRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ProgressBar ordersProgressBar;

    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderArrayList;
    private TextView noOrdersText;

    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        ordersToolbar = (Toolbar) findViewById(R.id.ordersToolbar);
        setSupportActionBar(ordersToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

//        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(MyOrdersActivity.this);

        orderArrayList = new ArrayList<>();

        ordersRecyclerView = (RecyclerView) findViewById(R.id.ordersRecyclerView);
        ordersProgressBar = (ProgressBar) findViewById(R.id.ordersProgressBar);
        noOrdersText = (TextView) findViewById(R.id.noOrdersText);

        linearLayoutManager = new LinearLayoutManager(MyOrdersActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        ordersRecyclerView.setHasFixedSize(true);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);

        user_id = applicationPreference.getIntData(Common.USER_ID);
        getOrdersData(user_id);
    }

    private void getOrdersData(int user_id) {
        ordersProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getOrders(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        orderArrayList.clear();
                        ordersProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                Order order = new Order(data);
                                orderArrayList.add(order);
                            }
                            if (orderArrayList.size() > 0) {
                                noOrdersText.setVisibility(View.GONE);
                                ordersRecyclerView.setVisibility(View.VISIBLE);
                                orderAdapter = new OrderAdapter(MyOrdersActivity.this, orderArrayList);
                                orderAdapter.notifyDataSetChanged();
                                ordersRecyclerView.setAdapter(orderAdapter);
                            }
                            else {
                                noOrdersText.setVisibility(View.VISIBLE);
                                ordersRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                noOrdersText.setVisibility(View.VISIBLE);
                ordersRecyclerView.setVisibility(View.GONE);
            }
        });
    }
}
