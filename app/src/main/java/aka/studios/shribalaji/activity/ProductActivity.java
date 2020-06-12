package aka.studios.shribalaji.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import aka.studios.shribalaji.R;
import aka.studios.shribalaji.adapter.ProductAdapter;
import aka.studios.shribalaji.adapter.ProductImagesSliderAdapter;
import aka.studios.shribalaji.common.ApplicationPreference;
import aka.studios.shribalaji.common.Common;
import aka.studios.shribalaji.common.SpInstance;
//import com.example.eatnow.databinding.ActivityMainBinding;
//import com.example.eatnow.databinding.ActivityProductBinding;
import aka.studios.shribalaji.model.Attribute;
import aka.studios.shribalaji.model.Image;
import aka.studios.shribalaji.model.Product;
import aka.studios.shribalaji.retrofit.ClientAPI;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";

//    private ActivityProductBinding binding;
    private ApplicationPreference applicationPreference;
    private ClientAPI clientAPI;

    private int CURRENT_BANNER_PAGE = 0;
    private int NUM_PAGES_BANNERS = 0;

    // [START Images Slider]
    private ProductImagesSliderAdapter productImagesSliderAdapter;
    private ArrayList<Image> imageArrayList;
    // [END Images Slider]

    // [START Product]
    private ProductAdapter productAdapter;
    private ArrayList<Product> productArrayList;
    private ArrayList<Attribute> attributeArrayList;
    private LinearLayoutManager productLayoutManager;
    // [END Product]

    private String url;
    private String categoryName;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private TextView productDetailName;
    private TextView productDetailsShortDesc;
    private TextView productPrice;
    private RecyclerView sizeRecyclerView;
    private ImageView expandIcon;
    private TextView productDescription;
    private TextView addToCart;
    private CardView detailCardView;
    private ViewPager detailViewPager;
    private DotsIndicator dotsIndicator;
    private ImageView detailImageView;
    private RelativeLayout detailRelative;
    private ProgressBar addToCartProgress;

    // [Add To Cart]
    private int cartProductId;
    private String cartProductName;
    private String cartProductCode;
    private String cartProductSize;
    private int cartProductPrice;
    private int cartProductQty;
    private String mobile;
    private int id;
    private String product = "";
    private BroadcastReceiver receiver = null;

    private TextView badge;
    private ImageView cartImg;

    private Toolbar toolbarProduct;
    private RecyclerView productRecyclerView;
    private LottieAnimationView notFoundAnimation;
    private GifImageView loadingImageView;
    private ProgressBar updateProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        toolbarProduct = (Toolbar) findViewById(R.id.toolbarProduct);
        setSupportActionBar(toolbarProduct);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        binding = ActivityProductBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);

        clientAPI = Common.getAPI();
        applicationPreference = SpInstance.getInstance(ProductActivity.this);
        productArrayList = new ArrayList<>();
        attributeArrayList = new ArrayList<>();
        imageArrayList = new ArrayList<>();

        mobile = applicationPreference.getData("mobile");

        productRecyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        notFoundAnimation = (LottieAnimationView) findViewById(R.id.notFoundAnimation);
        loadingImageView = (GifImageView) findViewById(R.id.loadingImageView);
        updateProgressBar = (ProgressBar) findViewById(R.id.updateProgressBar);

        // [Start Category]
        productLayoutManager = new LinearLayoutManager(ProductActivity.this);
        productRecyclerView.setHasFixedSize(true);
        productRecyclerView.setLayoutManager(productLayoutManager);
        // [End Category]

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        getProducts(product, id);

                        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

                        if (loggedIn) {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    }
                    else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        getProducts(product, id);

                        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

                        if (loggedIn) {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    }
                }
                else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (getIntent() != null) {
//            url = Integer.parseInt(getIntent().getStringExtra("url"));
            id = getIntent().getIntExtra("id", 0);
            categoryName = getIntent().getStringExtra("categoryName");

            getSupportActionBar().setTitle(categoryName);

            Log.d(TAG, "URL: " + id);

            getProducts(product, id);



//            binding.searchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    getProducts(query, id);
//                    return false;
//                }
//
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    getProducts(newText, id);
//                    return false;
//                }
//            });

//            getProducts(url);

            boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);

            if (loggedIn) {
                String mobile = applicationPreference.getData("mobile");
                getCartData(mobile);
            }
        }
    }

    private void getProducts(String product, int id) {
        updateProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = clientAPI.getProductSearch(product, id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        productArrayList.clear();
                        updateProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        Log.d(TAG, "ProductsResults: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i=0; i<jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                Product product = new Product(data);
                                productArrayList.add(product);
                            }
                            if (productArrayList.size() > 0) {
                                notFoundAnimation.setVisibility(View.GONE);
                                productRecyclerView.setVisibility(View.VISIBLE);
                                productAdapter = new ProductAdapter(ProductActivity.this, ProductActivity.this, productArrayList);
                                productAdapter.notifyDataSetChanged();
                                productRecyclerView.setAdapter(productAdapter);
                            }
                            else {
                                notFoundAnimation.setVisibility(View.VISIBLE);
                                productRecyclerView.setVisibility(View.GONE);
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                updateProgressBar.setVisibility(View.GONE);
                notFoundAnimation.setVisibility(View.VISIBLE);
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
                        String results = response.body().string();
                        Log.d(TAG, "CartItems: " + results);
                        JSONObject jsonObject = new JSONObject(results);
                        if (!jsonObject.getBoolean("error")) {
                            String cartItems = String.valueOf(jsonObject.getInt("cartItems"));

                            if (jsonObject.getInt("cartItems") > 0) {
//                                binding.badge.setVisibility(View.VISIBLE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        binding.badge.setText(cartItems);
                                        badge.setText(cartItems);
                                        badge.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                            else {
                                badge.setVisibility(View.GONE);
                            }
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



//    public void getProductInfo(int position) {
//        Product product = productArrayList.get(position);
//
//        cartProductName = product.getProduct_name();
//
//        builder = new AlertDialog.Builder(ProductActivity.this);
//
//        LayoutInflater inflater = getLayoutInflater();
//        View productDetails = inflater.inflate(R.layout.product_detail_layout, null);
//        productDetails.setBackgroundResource(android.R.color.transparent);
//
//        productDetailName = (TextView) productDetails.findViewById(R.id.productDetailName);
//        productDetailsShortDesc = (TextView) productDetails.findViewById(R.id.productDetailsShortDesc);
//        productPrice = (TextView) productDetails.findViewById(R.id.productPrice);
//        sizeRecyclerView = (RecyclerView) productDetails.findViewById(R.id.sizeRecyclerView);
//        expandIcon = (ImageView) productDetails.findViewById(R.id.expandIcon);
//        productDescription = (TextView) productDetails.findViewById(R.id.productDescription);
//        addToCart = (TextView) productDetails.findViewById(R.id.addToCart);
//        detailCardView = (CardView) productDetails.findViewById(R.id.detailCardView);
//        detailViewPager = (ViewPager) productDetails.findViewById(R.id.detailViewPager);
//        dotsIndicator = (DotsIndicator) productDetails.findViewById(R.id.dotsIndicator);
//        detailImageView = (ImageView) productDetails.findViewById(R.id.detailImageView);
//        detailRelative = (RelativeLayout) productDetails.findViewById(R.id.detailRelative);
//        addToCartProgress = (ProgressBar) productDetails.findViewById(R.id.addToCartProgress);
//
//        sizeRecyclerView.setHasFixedSize(true);
//        sizeRecyclerView.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));
//
//        builder.setView(productDetails);
//
//        productDetailName.setText(product.getProduct_name());
//        productDetailsShortDesc.setText(product.getProduct_semi_desc());
//        productDescription.setText(product.getDescription());
//        expandIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (productDescription.getVisibility() == View.GONE) {
//                    TransitionManager.beginDelayedTransition(detailCardView, new AutoTransition());
//                    productDescription.setVisibility(View.VISIBLE);
//                    expandIcon.setBackgroundResource(R.drawable.ic_arrow_up);
//                } else {
//                    TransitionManager.beginDelayedTransition(detailCardView, new AutoTransition());
//                    productDescription.setVisibility(View.GONE);
//                    expandIcon.setBackgroundResource(R.drawable.ic_arrow_down);
//                }
//            }
//        });
//
//        addToCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
//
//                if (!loggedIn) {
//                    Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    dialog.dismiss();
//                }
//                else {
//                    addToCartItem();
//                }
//            }
//        });
//
//        Call<ResponseBody> attAall = clientAPI.getProductAttributes(product.getId());
//        attAall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    try {
//                        attributeArrayList.clear();
//                        String results = response.body().string();
//                        Log.d("TAG", "Attributes: " + results);
//                        JSONObject jsonObject = new JSONObject(results);
//                        if (!jsonObject.getBoolean("error")) {
//                            JSONArray resultArray = jsonObject.getJSONArray("results");
//                            for (int i=0; i<resultArray.length(); i++) {
//                                JSONObject dataJson = resultArray.getJSONObject(i);
//
//                                int id = dataJson.getInt("id");
//                                int product_id = Integer.parseInt(dataJson.getString("product_id"));
//                                String sku = dataJson.getString("sku");
//                                String size = dataJson.getString("size");
//                                int price = Integer.parseInt(dataJson.getString("price"));
//                                int stock = Integer.parseInt(dataJson.getString("stock"));
//
//                                attributeArrayList.add(new Attribute(id,
//                                        product_id,
//                                        sku,
//                                        size,
//                                        price,
//                                        stock));
//                            }
//
//                            AttributeAdapter attributeAdapter = new AttributeAdapter(ProductActivity.this, ProductActivity.this, attributeArrayList);
//                            sizeRecyclerView.setAdapter(attributeAdapter);
//                        }
//
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//
//        Call<ResponseBody> imgCall = clientAPI.getProductImages(product.getId());
//        imgCall.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    try {
//                        imageArrayList.clear();
//                        String results = response.body().string();
//                        Log.d("TAG", "Images: " + results);
//                        JSONObject jsonObject = new JSONObject(results);
//                        if (!jsonObject.getBoolean("error")) {
//                            JSONArray resultArray = jsonObject.getJSONArray("results");
//                            for (int i=0; i<resultArray.length(); i++) {
//                                JSONObject dataJson = resultArray.getJSONObject(i);
//
//                                int id = dataJson.getInt("id");
//                                int product_id = Integer.parseInt(dataJson.getString("product_id"));
//                                String image = dataJson.getString("image");
//
//                                imageArrayList.add(new Image(id,
//                                        product_id,
//                                        image));
//                            }
//
//                            if (imageArrayList.size() > 0) {
//                                ProductImagesSliderAdapter productImagesSliderAdapter = new ProductImagesSliderAdapter(ProductActivity.this, imageArrayList);
//                                detailViewPager.setAdapter(productImagesSliderAdapter);
//                                dotsIndicator.setViewPager(detailViewPager);
//                                detailImageView.setVisibility(View.GONE);
//                            }
//                            else {
//                                detailImageView.setVisibility(View.VISIBLE);
//                                detailRelative.setVisibility(View.GONE);
//                                Picasso.get().load(Common.BASE_URL + product.getImage()).into(detailImageView);
//                            }
//                        }
//
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//
//        dialog = builder.create();
//        dialog.show();
//
////        builder.show();
//    }
//
//    @SuppressLint("SetTextI18n")
//    public void getSizeSelection(int postion) {
//        Attribute attribute = attributeArrayList.get(postion);
//
//        cartProductId = attribute.getProduct_id();
//        cartProductCode = attribute.getSku();
//        cartProductSize = attribute.getSize();
//        cartProductPrice = attribute.getPrice();
//
//        Call<ResponseBody> call = clientAPI.cartItemById(mobile, cartProductId, cartProductSize);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    try {
//                        String results = response.body().string();
//                        Log.d(TAG, "CartByID: " + results);
//                        JSONObject jsonObject = new JSONObject(results);
//                        if (!jsonObject.getBoolean("error")) {
//                            addToCart.setText("Go To Cart");
//                            addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
//                            addToCart.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(ProductActivity.this, CartActivity.class);
//                                    startActivity(intent);
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//                        else {
//                            addToCart.setText("Add");
//                            addToCart.setBackgroundColor(Color.parseColor("#F44336"));
//                        }
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//            }
//        });
//
//        productPrice.setText("₹ " + attribute.getPrice());
//    }
//
//    private void addToCartItem() {
//        Log.d(TAG, "AddToCart: " + cartProductId);
//        Log.d(TAG, "AddToCart: " + cartProductName);
//        Log.d(TAG, "AddToCart: " + cartProductCode);
//        Log.d(TAG, "AddToCart: " + cartProductSize);
//        Log.d(TAG, "AddToCart: " + cartProductPrice);
//        Log.d(TAG, "AddToCart: " + "1");
//        Log.d(TAG, "AddToCart: " + "8685816400");
//
//        addToCartProgress.setVisibility(View.VISIBLE);
//
//        Call<ResponseBody> call = clientAPI.addToCart(cartProductId,
//                cartProductName, cartProductCode, cartProductSize, cartProductPrice, 1, mobile);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.code() == 200) {
//                    try {
//                        addToCartProgress.setVisibility(View.GONE);
//                        String results = response.body().string();
//                        addToCart.setText("Go To Cart");
//                        addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                addToCartProgress.setVisibility(View.GONE);
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        View view = menu.findItem(R.id.mainCart).getActionView();
        badge = (TextView) view.findViewById(R.id.badge);
        cartImg = (ImageView) view.findViewById(R.id.cart_icon);

        cartImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }

    public void addCart(int position) {
        Product product = productArrayList.get(position);

        boolean loggedIn = applicationPreference.getBooleanData(Common.LOGGED_IN);
        if (!loggedIn) {
            Intent intent = new Intent(ProductActivity.this, LoginActivity.class);
            startActivity(intent);
            dialog.dismiss();
        }
        else {
            Call<ResponseBody> call = clientAPI.addToCart(product.getProduct_id(), product.getProduct_name(),
                    product.getSku(), product.getSize(), product.getPrice(), 1, mobile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        try {
//                            addToCartProgress.setVisibility(View.GONE);
                            String results = response.body().string();
//                            addToCart.setText("Go To Cart");
//                            addToCart.setBackgroundColor(Color.parseColor("#4CAF50"));
                            Toast.makeText(ProductActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        } catch (IOException e) {
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

    public void updateItem(int position, int quantity) {
        Product products = productArrayList.get(position);

        updateProgressBar.setVisibility(View.VISIBLE);

        Call<ResponseBody> incCall = clientAPI.updateCartItem(mobile, products.getProduct_id(), products.getSize(), quantity);
        incCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        updateProgressBar.setVisibility(View.GONE);
                        String results = response.body().string();
                        JSONObject incJson = new JSONObject(results);
                        if (!incJson.getBoolean("error")) {
                            JSONObject data = incJson.getJSONObject("results");

                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
//                            getProducts(product, id);
                        }
                        else {
                            String mobile = applicationPreference.getData("mobile");
                            getCartData(mobile);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                updateProgressBar.setVisibility(View.GONE);
            }
        });
    }
}
