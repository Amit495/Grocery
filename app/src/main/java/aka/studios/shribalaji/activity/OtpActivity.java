package aka.studios.shribalaji.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityOtpBinding;
import aka.studios.shribalaji.retrofit.ClientAPI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private static final String TAG = "OtpActivity";
    private ActivityOtpBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;
    private String mobile;
    private String verificationId;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_otp);

        binding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        applicationPreference  = SpInstance.getInstance(OtpActivity.this);
        clientAPI = Common.getAPI();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        if (getIntent() != null) {
            mobile = getIntent().getStringExtra("mobile");
            verificationId = getIntent().getStringExtra("verificationId");

            binding.showMobileTextView.setText(getString(R.string.otp) + " +91-" + mobile);

            binding.otp1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.otp1.getText().toString().length() > 0) {
                        binding.otp2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.otp2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.otp2.getText().toString().length() > 0) {
                        binding.otp3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.otp3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.otp3.getText().toString().length() > 0) {
                        binding.otp4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.otp4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.otp4.getText().toString().length() > 0) {
                        binding.otp5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            binding.otp5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (binding.otp5.getText().toString().length() > 0) {
                        binding.otp6.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String otp1 = binding.otp1.getText().toString();
                    String otp2 = binding.otp2.getText().toString();
                    String otp3 = binding.otp3.getText().toString();
                    String otp4 = binding.otp4.getText().toString();
                    String otp5 = binding.otp5.getText().toString();
                    String otp6 = binding.otp6.getText().toString();
                    String otp = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;

//                    Toast.makeText(OtpActivity.this, otp, Toast.LENGTH_SHORT).show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
            });
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
//                            Intent mainIntent = new Intent(OtpActivity.this, MainActivity.class);
//                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(mainIntent);
//                            finish();
                            String fcmUID = user.getUid();
                            String fcmToken = FirebaseInstanceId.getInstance().getToken();

                            applicationPreference.setData("fcmToken", fcmToken);
                            applicationPreference.setData("fcmUID", user.getUid());

                            binding.loadingImageView.setVisibility(View.VISIBLE);
                            binding.otpLin.setVisibility(View.GONE);

                            Call<ResponseBody> call = clientAPI.addUser(mobile, fcmUID, fcmToken);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        try {
                                            binding.loadingImageView.setVisibility(View.GONE);
                                            String results = response.body().string();
                                            JSONObject jsonObject = new JSONObject(results);
                                            if (!jsonObject.getBoolean("error")) {
                                                JSONObject resultJson = jsonObject.getJSONObject("results");

                                                int id = resultJson.getInt("id");
                                                String mobile = resultJson.getString("mobile");
                                                applicationPreference.setIntData(Common.USER_ID, id);
                                                applicationPreference.setData("mobile", mobile);
                                                applicationPreference.setBooleanData(Common.LOGGED_IN, true);

                                                Intent setupIntent = new Intent(OtpActivity.this, MainActivity.class);
                                                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(setupIntent);
                                                finish();
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
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                binding.invalidOTPTextView.setText("Invalid OTP");
                                binding.otpLin.setVisibility(View.VISIBLE);
                                binding.loadingImageView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
}
