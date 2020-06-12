package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.CartAdapter;
import aka.studios.shribalaji.adapter.ProceedAddressAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityProceedBinding;
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

public class ProceedActivity extends AppCompatActivity {

    private static final String TAG = "ProceedActivity";
    private ActivityProceedBinding binding;
    private ClientAPI clientAPI;
    private ApplicationPreference applicationPreference;

    private CartAdapter cartAdapter;
//    private ArrayList<Cart> cartArrayList;
    private ProceedAddressAdapter addressAdapter;
    private ArrayList<Address> addressArrayList;
    private LinearLayoutManager addressLayoutManager;
    private int cartTotal = 0;
    private int total = 0;
    private String shipping;
    private int pos;

    private AlertDialog.Builder builder;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_proceed);

        binding = ActivityProceedBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(ProceedActivity.this);
        addressArrayList = new ArrayList<>();

        // [Start Address]
        addressLayoutManager = new LinearLayoutManager(ProceedActivity.this);
        binding.addressRecyclerView.setHasFixedSize(true);
        binding.addressRecyclerView.setLayoutManager(addressLayoutManager);
        // [End Address]

        mobile = applicationPreference.getData("mobile");
        getCartData(mobile);

        int user_id = applicationPreference.getIntData(Common.USER_ID);
        getAddressData(user_id);

        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continuePlace();
            }
        });

        binding.addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ProceedActivity.this, AddAddressActivity.class);
//                startActivity(intent);
                addAddressPopup();
            }
        });
    }

    private void getAddressData(int user_id) {
        binding.loadingImageView.setVisibility(View.VISIBLE);
        Log.d(TAG, "AddressResult: " + user_id);

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
                                addressAdapter = new ProceedAddressAdapter(ProceedActivity.this, ProceedActivity.this, addressArrayList);
                                addressAdapter.notifyDataSetChanged();
                                binding.addressRecyclerView.setAdapter(addressAdapter);
                                binding.cartLinear.setVisibility(View.VISIBLE);
                                binding.cartProcessLinear.setVisibility(View.VISIBLE);
                            }
                            else {
                                binding.addressRecyclerView.setVisibility(View.GONE);
                                binding.cartProcessLinear.setVisibility(View.GONE);
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
//                        cartArrayList.clear();
//                        binding.loadingImageView.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "CartListResults" + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {

                            String cartItems = String.valueOf(jsonObject.getInt("cartItems"));
                            cartTotal = jsonObject.getInt("cartTotal");
                            total = jsonObject.getInt("total");
                            binding.proceedItems.setText("Price Details (" + cartItems + " Items)");

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
//                binding.loadingImageView.setVisibility(View.GONE);
                binding.notFoundAnimation.setVisibility(View.VISIBLE);
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
        binding.proceedAllTotal.setText("₹ " + totalPrice);
        binding.shippingPrice.setText("₹ " + shipping);
        binding.gstPrice.setText("₹ 0");
    }

    public void selectedAddress(int position) {
        Address address = addressArrayList.get(position);
        pos = position;
        getJSONData(position);
    }

    private void continuePlace() {
        Intent intent = new Intent(ProceedActivity.this, PaymentActivity.class);
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

    private void addAddressPopup() {
        builder = new AlertDialog.Builder(ProceedActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View addAddress = inflater.inflate(R.layout.add_address_popup_layout, null);

        EditText firstName = (EditText) addAddress.findViewById(R.id.firstName);
        EditText lastName = (EditText) addAddress.findViewById(R.id.lastName);
        EditText mble = (EditText) addAddress.findViewById(R.id.mobile);
        EditText pincode = (EditText) addAddress.findViewById(R.id.pincode);
        EditText address = (EditText) addAddress.findViewById(R.id.address);
        EditText landmark = (EditText) addAddress.findViewById(R.id.landmark);
        EditText city = (EditText) addAddress.findViewById(R.id.city);
        EditText state = (EditText) addAddress.findViewById(R.id.state);
        EditText country = (EditText) addAddress.findViewById(R.id.country);
        RadioButton home = (RadioButton) addAddress.findViewById(R.id.home);
        RadioButton work = (RadioButton) addAddress.findViewById(R.id.work);
        RadioButton other = (RadioButton) addAddress.findViewById(R.id.other);

        mble.setText(mobile);

        builder.setView(addAddress);

        builder.setPositiveButton("Add Address", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(firstName.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                    firstName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(lastName.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                    lastName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mble.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Mobile", Toast.LENGTH_SHORT).show();
                    mble.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pincode.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                    pincode.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(address.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                    address.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(landmark.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Landmark", Toast.LENGTH_SHORT).show();
                    landmark.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(city.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                    city.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(state.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter State", Toast.LENGTH_SHORT).show();
                    state.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(country.getText().toString()))
                {
                    Toast.makeText(ProceedActivity.this, "Enter Country", Toast.LENGTH_SHORT).show();
                    country.requestFocus();
                    return;
                }

                int type = 0;
                if (home.isChecked()) {
                    type = 0;
                }
                if (work.isChecked()) {
                    type = 1;
                }
                if (other.isChecked()) {
                    type = 2;
                }

                Call<ResponseBody> call = clientAPI.addAddresses(applicationPreference.getIntData(Common.USER_ID), firstName.getText().toString(),
                        lastName.getText().toString(), mobile, pincode.getText().toString(),
                        address.getText().toString(), landmark.getText().toString(), city.getText().toString(),
                        state.getText().toString(), country.getText().toString(), type);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            try {
                                String results = response.body().string();
                                JSONObject jsonObject = new JSONObject(results);
                                if (!jsonObject.getBoolean("error")) {
                                    dialog.dismiss();
                                    int user_id = applicationPreference.getIntData(Common.USER_ID);
                                    getAddressData(user_id);
                                    recreate();
                                    Toast.makeText(ProceedActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                            int user_id = applicationPreference.getIntData(Common.USER_ID);
                            getAddressData(user_id);
                            Toast.makeText(ProceedActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
