package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityPaymentBinding;
import aka.studios.shribalaji.retrofit.ClientAPI;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.android.volley.Request.Method.POST;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    private ActivityPaymentBinding binding;
    private ClientAPI clientAPI;
    private ApplicationPreference applicationPreference;

    private String response;

    private int cartTotal = 0;
    private int total = 0;
    private String shipping;

    private int user_id;
    private String fullName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String landmark;
    private int addressType;
    private String country;
    private String mobile;
    private String paymentMethod;
    private int items;

    private String orderId = String.valueOf(System.currentTimeMillis());
//    int random = (int)(Math.random() * 1000 + 1);
//    private String orderId;
//    private String sub1;
//    private String sub2;

    private String url;
    private String merchantKey;
    private String merchantID;
    private String website;
    private String industryType;
    private String channelWeb;
    private String channelApp;
    private String transactionURL;
    private String transactionStatusUrl;
    private String callbackUrl;

    private String checksum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);

        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(PaymentActivity.this);
        mobile = applicationPreference.getData("mobile");

        if (getIntent() != null) {
            response = getIntent().getStringExtra("response");

            try {
                JSONObject jsonObject = new JSONObject(response);

                int id = jsonObject.getInt("id");
                user_id = jsonObject.getInt("user_id");
//                jsonObject.getString("first_name");
//                jsonObject.getString("last_name");
                String mobileAdd = jsonObject.getString("mobile");
                pincode = jsonObject.getString("pincode");
                address = jsonObject.getString("address");
                landmark = jsonObject.getString("landmark");
                city = jsonObject.getString("city");
                state = jsonObject.getString("state");
                country = jsonObject.getString("country");
                addressType = jsonObject.getInt("type");
                String type = String.valueOf(jsonObject.getInt("type"));
                fullName = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");

                binding.fullName.setText(fullName);

                if (type.equals("0")) {
                    binding.type.setText("Home");
                }
                if (type.equals("1")) {
                    binding.type.setText("Work");
                }
                if (type.equals("2")) {
                    binding.type.setText("Other");
                }

                binding.landmark.setText(landmark);
                binding.city.setText(city + "- ");
                binding.pincode.setText(pincode);
                binding.mobile.setText(mobileAdd);

//                sub1 = mobile.substring(0, 4);
//                sub2 = mobile.substring(5, 9);
//                orderId = sub2 + random + sub1;
//                Log.d(TAG, "OrderId: " + paymentMethod);
//                Log.d(TAG, "OrderId: " + orderId);
//                Log.d(TAG, "OrderId: " + sub1);
//                Log.d(TAG, "OrderId: " + sub2);

                binding.payNowBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (binding.COD.isChecked()) {
                            paymentMethod = "COD";
                            payNow(orderId, user_id, mobile, fullName, address, city, state, pincode, landmark, addressType, items, shipping, 1, paymentMethod, total);
                        }
                        if (binding.Paytm.isChecked()) {
                            paymentMethod = "Paytm";
                            paytmPay();
                        }
                        Log.d(TAG, "OrderId: " + paymentMethod);
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getCartData(mobile);
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

                            items = jsonObject.getInt("cartItems");
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
//                binding.notFoundAnimation.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint("SetTextI18n")
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

    private void payNow(String orderId, int user_id, String mobile, String fullName, String address, String city,
                        String state, String pincode, String landmark, int addressType, int items, String shipping,
                        int place_order, String paymentMethod, int grandTotal) {

        binding.placeProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.placeOrder(orderId, user_id, mobile,
                fullName, address, city, state, pincode, landmark,
                addressType, items, shipping, place_order, paymentMethod, grandTotal);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.placeProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "PlaceResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject data = jsonObject.getJSONObject("results");

                            Intent intent = new Intent(PaymentActivity.this, ThanksActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("date", data.getString("created_at"));
                            intent.putExtra("price", data.getInt("grandTotal"));
                            startActivity(intent);
                            finish();
                            Toast.makeText(PaymentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.placeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void paytmPay() {
        binding.placeProgressBar.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = clientAPI.paytmCredentials();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.placeProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject data = jsonObject.getJSONObject("results");

                            merchantKey = data.getString("merchantKey");
                            merchantID = data.getString("merchantID");
                            website = data.getString("website");
                            industryType = data.getString("industryType");
                            channelWeb = data.getString("channelWeb");
                            channelApp = data.getString("channelApp");
                            transactionURL = data.getString("transactionURL");
                            transactionStatusUrl = data.getString("transactionStatusUrl");
                            callbackUrl = data.getString("callbackUrl");
                            url = data.getString("url");

                            generatCheckSum();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.placeProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void generatCheckSum() {
        binding.placeProgressBar.setVisibility(View.VISIBLE);

        Map<String, String> params = new HashMap<>();
        params.put("MID", merchantID);
        params.put("ORDER_ID", orderId);
        params.put("CUST_ID", String.valueOf(user_id));
        params.put("INDUSTRY_TYPE_ID", industryType);
        params.put("CHANNEL_ID", channelApp);
        params.put("TXN_AMOUNT", String.valueOf(total));
        params.put("WEBSITE", website);
        params.put("CALLBACK_URL", callbackUrl);
        params.put("MOBILE_NO", mobile);

        JSONObject param = new JSONObject(params);
        JsonObjectRequest objectRequest = new JsonObjectRequest(POST, url, param, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                checksum=response.optString("CHECKSUMHASH");

                if (checksum.trim().length()!=0)
                {
                    onStartTransaction();
                    binding.placeProgressBar.setVisibility(View.GONE);
                }

                Log.e("getresponse", String.valueOf(response));
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(PaymentActivity.this).add(objectRequest);
    }

    public void onStartTransaction()
    {
        final String totalAmount = String.valueOf(total);

        PaytmPGService Service = PaytmPGService.getProductionService();

        Map<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID", merchantID);

        paramMap.put( "ORDER_ID", orderId);
        paramMap.put( "CUST_ID", String.valueOf(user_id));
        paramMap.put( "MOBILE_NO" , mobile);
        paramMap.put( "CHANNEL_ID", channelApp);
        paramMap.put( "TXN_AMOUNT", String.valueOf(total));
        paramMap.put( "WEBSITE", website);
        paramMap.put( "INDUSTRY_TYPE_ID", industryType);
        paramMap.put( "CALLBACK_URL", callbackUrl);
        paramMap.put( "CHECKSUMHASH" , checksum);
        PaytmOrder Order = new PaytmOrder((HashMap<String, String>) paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(PaymentActivity.this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                String status = inResponse.getString("STATUS");

                if (status.equals("TXN_FAILURE"))
                {
                    Toast.makeText(PaymentActivity.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
                }

                else if (status.equals("TXN_SUCCESS"))
                {
                    payNow(orderId, user_id, mobile, fullName, address, city, state, pincode, landmark, addressType, items, shipping, 1, paymentMethod, total);
                }
            }
            public void networkNotAvailable() {}
            public void clientAuthenticationFailed(String inErrorMessage) {}
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }
}
