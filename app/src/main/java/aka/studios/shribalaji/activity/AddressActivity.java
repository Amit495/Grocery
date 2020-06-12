package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import aka.studios.shribalaji.adapter.AddressAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityAddressBinding;
import aka.studios.shribalaji.model.Address;
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

public class AddressActivity extends AppCompatActivity {

    private static final String TAG = "AddressActivity";
    ActivityAddressBinding binding;
    ApplicationPreference applicationPreference;
    ClientAPI clientAPI;

    // [START Product]
    private AddressAdapter addressAdapter;
    private ArrayList<Address> addressArrayList;
    private LinearLayoutManager addressLayoutManager;
    // [END Product]

    private String mobile;
    private int user_id;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_address);

        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(AddressActivity.this);
        addressArrayList = new ArrayList<>();

        // [Start Address]
        addressLayoutManager = new LinearLayoutManager(AddressActivity.this);
        binding.addressRecyclerView.setHasFixedSize(true);
        binding.addressRecyclerView.setLayoutManager(addressLayoutManager);
        // [End Address]

        binding.addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
            }
        });

        user_id = applicationPreference.getIntData(Common.USER_ID);
        getAddressData(user_id);
    }

    private void getAddressData(int user_id) {
        binding.loadingImageView.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getAddresses(user_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        addressArrayList.clear();
                        binding.loadingImageView.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "AddressResult: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                int id = data.getInt("id");
                                int user_id = data.getInt("user_id");
                                String first_name = data.getString("first_name");
                                String last_name = data.getString("last_name");
                                String mobile = data.getString("mobile");
                                String pincode = data.getString("pincode");
                                String address = data.getString("address");
                                String landmark = data.getString("landmark");
                                String city = data.getString("city");
                                String state = data.getString("state");
                                String country = data.getString("country");
                                int type = data.getInt("type");

                                addressArrayList.add(new Address(id,
                                        user_id,
                                        first_name,
                                        last_name,
                                        mobile,
                                        pincode,
                                        address,
                                        landmark,
                                        city,
                                        state,
                                        country,
                                        type));
                            }
                            if (addressArrayList.size() > 0) {
                                addressAdapter = new AddressAdapter(AddressActivity.this, AddressActivity.this, addressArrayList);
                                addressAdapter.notifyDataSetChanged();
                                binding.addressRecyclerView.setAdapter(addressAdapter);
                                binding.notFoundAnimation.setVisibility(View.GONE);
                            }
                            else {
                                binding.addressRecyclerView.setVisibility(View.GONE);
                                binding.notFoundAnimation.setVisibility(View.VISIBLE);
                            }
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

    public void selectedAddress(int position) {
        Address address = addressArrayList.get(position);
        pos = position;
//        getJSONData(position);
    }

    public void editAddress(int pos) {
        Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
        intent.putExtra("response", getJSONData(pos).toString());
        startActivity(intent);
    }

    public JSONObject getJSONData(int position) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", addressArrayList.get(position).getId());
            jsonObject.put("user_id", addressArrayList.get(position).getUser_id());
            jsonObject.put("first_name", addressArrayList.get(position).getFirst_name());
            jsonObject.put("last_name", addressArrayList.get(position).getLast_name());
            jsonObject.put("mobile", addressArrayList.get(position).getMobile());
            jsonObject.put("pincode", addressArrayList.get(position).getPincode());
            jsonObject.put("address", addressArrayList.get(position).getAddress());
            jsonObject.put("landmark", addressArrayList.get(position).getLandmark());
            jsonObject.put("city", addressArrayList.get(position).getCity());
            jsonObject.put("state", addressArrayList.get(position).getState());
            jsonObject.put("country", addressArrayList.get(position).getCountry());
            jsonObject.put("type", addressArrayList.get(position).getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void removeAddress(int pos) {
        Address address = addressArrayList.get(pos);

        Call<ResponseBody> call = clientAPI.clearAddress(address.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            getAddressData(user_id);
                            Toast.makeText(AddressActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
}
