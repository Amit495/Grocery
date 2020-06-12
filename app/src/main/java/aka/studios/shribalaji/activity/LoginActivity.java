package aka.studios.shribalaji.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityLoginBinding;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private ApplicationPreference applicationPreference;
    private String mobile;
    private String countryCode;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        applicationPreference = SpInstance.getInstance(LoginActivity.this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        binding.generateOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = binding.mobileEditText.getText().toString();
                countryCode = binding.countryCodePicker.getSelectedCountryCodeWithPlus();

                if (mobile.isEmpty())
                {
                    binding.mobileEditText.setError("Mobile Number Required");
                    binding.mobileEditText.requestFocus();
                    return;
                }
                if (mobile.length() < 10)
                {
                    binding.mobileEditText.setError("Mobile Number Invalid");
                    binding.mobileEditText.requestFocus();
                    return;
                }

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        countryCode + mobile,
                        60,
                        TimeUnit.SECONDS,
                        LoginActivity.this,
                        mCallbacks
                );
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                Intent otpIntent = new Intent(LoginActivity.this, OtpActivity.class);
                otpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                otpIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                otpIntent.putExtra("mobile", mobile);
                otpIntent.putExtra("verificationId", verificationId);
                startActivity(otpIntent);
                finish();
                // ...

                applicationPreference.setData("mobile", mobile);
            }
        };
    }
}
