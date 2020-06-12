package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityEditAddressBinding;
import aka.studios.shribalaji.retrofit.ClientAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity {

    private static final String TAG = "EditAddressActivity";
    private ActivityEditAddressBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    String response;
    private int type = 0;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_address);

        binding = ActivityEditAddressBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(EditAddressActivity.this);

        if (getIntent() != null) {
            response = getIntent().getStringExtra("response");

            try {
                JSONObject jsonObject = new JSONObject(response);
                id = jsonObject.getInt("id");
                int user_id = jsonObject.getInt("user_id");
                String firstName = jsonObject.getString("first_name");
                String lastName = jsonObject.getString("last_name");
                String mobile = jsonObject.getString("mobile");
                String pincode = jsonObject.getString("pincode");
                String address = jsonObject.getString("address");
                String landmark = jsonObject.getString("landmark");
                String city = jsonObject.getString("city");
                String state = jsonObject.getString("state");
                String country = jsonObject.getString("country");
                int type = jsonObject.getInt("type");

                binding.firstName.setText(firstName);
                binding.lastName.setText(lastName);
                binding.mobile.setText(mobile);
                binding.pincode.setText(pincode);
                binding.address.setText(address);
                binding.landmark.setText(landmark);
                binding.city.setText(city);
                binding.state.setText(state);
                binding.country.setText(country);

                if (type == 0) {
                    binding.home.setChecked(true);
                }
                if (type == 1) {
                    binding.work.setChecked(true);
                }
                if (type == 2) {
                    binding.other.setChecked(true);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        binding.saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = binding.firstName.getText().toString().trim();
                String lastName = binding.lastName.getText().toString().trim();
                String pincode = binding.pincode.getText().toString().trim();
                String address = binding.address.getText().toString().trim();
                String landmark = binding.landmark.getText().toString().trim();

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
                editAddress(id, firstName, lastName, pincode, address, landmark, type);
            }
        });
    }

    private void editAddress(int id, String firstName, String lastName, String pincode,
                             String address, String landmark, int type) {
        binding.addLinear.setAlpha(0.5f);
        binding.addProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.editAddresses(id, firstName, lastName, pincode,
                address, landmark, type);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        binding.addProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            Intent intent = new Intent(EditAddressActivity.this, AddressActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(EditAddressActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
