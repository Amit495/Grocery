package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityAddAddressBinding;
import aka.studios.shribalaji.retrofit.ClientAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {

    private static final String TAG = "AddAddressActivity";
    private ActivityAddAddressBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    private int user_id;
    private String mobile;
    private int type = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_address);

        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(AddAddressActivity.this);
        mobile = applicationPreference.getData("mobile");
        user_id = applicationPreference.getIntData(Common.USER_ID);

        binding.mobile.setText("+91 " + mobile);

        binding.saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.firstName.getText().toString().trim();
                String lastName = binding.lastName.getText().toString().trim();
                String mobileText = binding.mobile.getText().toString().trim();
                String pincode = binding.pincode.getText().toString().trim();
                String address = binding.address.getText().toString().trim();
                String landmark = binding.landmark.getText().toString().trim();
                String city = binding.city.getText().toString().trim();
                String state = binding.state.getText().toString().trim();
                String country = binding.country.getText().toString().trim();

                if (binding.home.isChecked()) {
                    type = 0;
                }
                if (binding.work.isChecked()) {
                    type = 1;
                }
                if (binding.other.isChecked()) {
                    type = 2;
                }

                if (firstName.isEmpty()) {
                    binding.firstName.setError("Field is required");
                    binding.firstName.requestFocus();
                    return;
                }
                if (lastName.isEmpty()) {
                    binding.lastName.setError("Field is required");
                    binding.lastName.requestFocus();
                    return;
                }
                if (mobile.isEmpty()) {
                    binding.mobile.setError("Field is required");
                    binding.mobile.requestFocus();
                    return;
                }
                if (pincode.isEmpty()) {
                    binding.pincode.setError("Field is required");
                    binding.pincode.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    binding.address.setError("Field is required");
                    binding.address.requestFocus();
                    return;
                }
                if (landmark.isEmpty()) {
                    binding.landmark.setError("Field is required");
                    binding.landmark.requestFocus();
                    return;
                }
                if (city.isEmpty()) {
                    binding.city.setError("Field is required");
                    binding.city.requestFocus();
                    return;
                }
                if (state.isEmpty()) {
                    binding.state.setError("Field is required");
                    binding.state.requestFocus();
                    return;
                }
                if (country.isEmpty()) {
                    binding.country.setError("Field is required");
                    binding.country.requestFocus();
                    return;
                }
                addAddress(user_id, firstName, lastName, mobile, pincode, address, landmark, city, state, country, type);
            }
        });
    }

    private void addAddress(int user_id, String firstName, String lastName, String mobile, String pincode,
                            String address, String landmark, String city, String state, String country, int type) {
        binding.addLinear.setAlpha(0.5f);
        binding.addProgressBar.setVisibility(View.VISIBLE);

        Log.d(TAG, "AddressAdd: " + user_id);

        Call<ResponseBody> call = clientAPI.addAddresses(user_id, firstName, lastName, mobile, pincode,
                address, landmark, city, state, country, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.addProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(AddAddressActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                binding.addProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
