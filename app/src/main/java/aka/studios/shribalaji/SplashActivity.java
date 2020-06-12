package aka.studios.shribalaji;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import aka.studios.shribalaji.activity.MainActivity;
import aka.studios.shribalaji.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Thread startScreen = new Thread()
        {
            public void run()
            {
                try{
                    sleep(5000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    Intent loginIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        };
        startScreen.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
