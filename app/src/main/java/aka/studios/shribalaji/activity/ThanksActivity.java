package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import aka.studios.shribalaji.R;

public class ThanksActivity extends AppCompatActivity {

    private TextView orderDate;
    private TextView orderID;
    private TextView priceText;

    private String orderId;
    private String date;
    private int price;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks);

        orderID = (TextView) findViewById(R.id.orderId);
        orderDate = (TextView) findViewById(R.id.orderDate);
        priceText = (TextView) findViewById(R.id.price);

        if (getIntent() != null) {
            orderId = getIntent().getStringExtra("orderId");
            date = getIntent().getStringExtra("date");
            price = getIntent().getIntExtra("price", 0);

            String id = orderId;
            String time = date;
            String total = String.valueOf(price);

            orderID.setText("#" + id);
            orderDate.setText(time);
            priceText.setText("₹ " + total);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ThanksActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
