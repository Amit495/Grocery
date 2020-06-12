package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
import aka.studios.shribalaji.databinding.ActivityAccountBinding;
import aka.studios.shribalaji.retrofit.ClientAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    private static final String TAG = "AccountActivity";
    private ActivityAccountBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    private String mobile;
    private String about_us;
    private String contact_us;
    private String privacy_policy;
    private String terms;
    private String developers;

    private String version = "1.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_account);

        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        applicationPreference = SpInstance.getInstance(AccountActivity.this);
        clientAPI = Common.getAPI();
        mobile = applicationPreference.getData("mobile");

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = packageInfo.versionName;
        binding.versionText.setText("v" + version);

        if (!mobile.isEmpty()) {
            binding.mobileText.setText("+91 "+ mobile);
        }

        binding.ordersRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, MyOrdersActivity.class);
                startActivity(intent);
            }
        });

        binding.addressRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        getAbouts();
    }

    private void getAbouts() {
        Call<ResponseBody> call = clientAPI.getAbouts();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        String results = response.body().string();
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONObject resultJson = jsonObject.getJSONObject("results");

                            about_us = resultJson.getString("about_us");
                            contact_us = resultJson.getString("contact_us");
                            privacy_policy = resultJson.getString("privacy_policy");
                            terms = resultJson.getString("terms");
                            developers = resultJson.getString("developers");

                            binding.aboutRel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent aboutUs = new Intent(Intent.ACTION_VIEW, Uri.parse(about_us));
                                    startActivity(aboutUs);
                                }
                            });

                            binding.contactRel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent contactUs = new Intent(Intent.ACTION_VIEW, Uri.parse(contact_us));
                                    startActivity(contactUs);
                                }
                            });

                            binding.termsRel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent termsCon = new Intent(Intent.ACTION_VIEW, Uri.parse(terms));
                                    startActivity(termsCon);
                                }
                            });

                            binding.privacyRel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent policy = new Intent(Intent.ACTION_VIEW, Uri.parse(privacy_policy));
                                    startActivity(policy);
                                }
                            });

                            binding.developerRel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent develop = new Intent(Intent.ACTION_VIEW, Uri.parse(developers));
                                    startActivity(develop);
                                }
                            });
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
