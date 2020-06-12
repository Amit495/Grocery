package aka.studios.shribalaji.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientAPI {
    // [Get all banners]
    @GET("banners")
    Call<ResponseBody> getBanners();

    // [Get all banners]
    @GET("advtbanners")
    Call<ResponseBody> advtbanners();

    // [Get all categories main]
    @GET("categories-main")
    Call<ResponseBody> getCategoriesMain();

    // [Get all categories top]
    @GET("categories-random")
    Call<ResponseBody> getCategoriesTop();

    // [Get all categories]
    @GET("categories")
    Call<ResponseBody> getCategories();

    // [Get all products]
    @GET("products")
    Call<ResponseBody> getProducts();

    // [Get all products]
    @GET("top-products")
    Call<ResponseBody> getTopProducts();

    // [Get products by url]
    @GET("products/{url}")
    Call<ResponseBody> getProductByUrl(@Path("url") String url);

    // [Get product info by id]
    @GET("products-details/{id}")
    Call<ResponseBody> getProductInfo(@Path("id") int id);

    // [Get product attributes by id]
    @GET("products-attributes/{id}")
    Call<ResponseBody> getProductAttributes(@Path("id") int id);

    // [Get product images by id]
    @GET("products-images/{id}")
    Call<ResponseBody> getProductImages(@Path("id") int id);

    // [Get products search]
    @FormUrlEncoded
    @POST("search-products")
    Call<ResponseBody> getProductSearch(@Field("product") String product,
                                        @Field("category_id") int category_id);

    // [Add To Cart]
    @FormUrlEncoded
    @POST("add-to-cart")
    Call<ResponseBody> addToCart(@Field("product_id") int product_id,
                                 @Field("product_name") String product_name,
                                 @Field("product_code") String product_code,
                                 @Field("size") String size,
                                 @Field("price") int price,
                                 @Field("quantity") int quantity,
                                 @Field("mobile") String mobile);

    // [Get cart]
    @GET("get-cart/{mobile}")
    Call<ResponseBody> getCart(@Path("mobile") String mobile);

    // [Get cart item by id]
    @GET("cart-id/{mobile}/{id}/{size}")
    Call<ResponseBody> cartItemById(@Path("mobile") String mobile,
                                    @Path("id") int id,
                                    @Path("size") String size);

    // [Cart update]
    @FormUrlEncoded
    @POST("get-cart")
    Call<ResponseBody> cartUpdate(@Field("id") int id,
                                  @Field("product_code") String product_code,
                                  @Field("size") String size,
                                  @Field("price") int price,
                                  @Field("quantity") int quantity);

    // [Add To Cart]
    @FormUrlEncoded
    @POST("update-cart-qty")
    Call<ResponseBody> updateCartItem(@Field("mobile") String mobile,
                                     @Field("product_id") int product_id,
                                     @Field("size") String size,
                                     @Field("quantity") int quantity);

    // [clear cart]
    @GET("clear-cart-item/{id}")
    Call<ResponseBody> clearItem(@Path("id") int id);

    // [Add To User]
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> addUser(@Field("mobile") String mobile,
                               @Field("fcmUID") String fcmUID,
                               @Field("fcmToken") String fcmToken);

    // [Get address]
    @GET("address/{user_id}")
    Call<ResponseBody> getAddresses(@Path("user_id") int user_id);

    // [Add address]
    @FormUrlEncoded
    @POST("add-address")
    Call<ResponseBody> addAddresses(@Field("user_id") int user_id,
                                    @Field("first_name") String first_name,
                                    @Field("last_name") String last_name,
                                    @Field("mobile") String mobile,
                                    @Field("pincode") String pincode,
                                    @Field("address") String address,
                                    @Field("landmark") String landmark,
                                    @Field("city") String city,
                                    @Field("state") String state,
                                    @Field("country") String country,
                                    @Field("type") int type);

    // [Edit address]
    @FormUrlEncoded
    @POST("edit-address")
    Call<ResponseBody> editAddresses(@Field("id") int id,
                                    @Field("first_name") String first_name,
                                    @Field("last_name") String last_name,
                                    @Field("pincode") String pincode,
                                    @Field("address") String address,
                                    @Field("landmark") String landmark,
                                    @Field("type") int type);

    // [Get orders]
    @GET("delete-address/{id}")
    Call<ResponseBody> clearAddress(@Path("id") int id);

    // [Place order]
    @FormUrlEncoded
    @POST("place-order")
    Call<ResponseBody> placeOrder(@Field("orderId") String orderId,
                                  @Field("user_id") int user_id,
                                  @Field("mobile") String mobile,
                                  @Field("name") String name,
                                  @Field("address") String address,
                                  @Field("city") String city,
                                  @Field("state") String state,
                                  @Field("pincode") String pincode,
                                  @Field("landmark") String landmark,
                                  @Field("type") int type,
                                  @Field("items") int items,
                                  @Field("shippingCharges") String shippingCharges,
                                  @Field("orderStatus") int orderStatus,
                                  @Field("paymentMethod") String paymentMethod,
                                  @Field("grandTotal") int grandTotal);

    // [Get orders]
    @GET("get-orders/{id}")
    Call<ResponseBody> getOrders(@Path("id") int id);

    // [Get order details]
    @GET("order-details/{id}")
    Call<ResponseBody> orderDetails(@Path("id") int id);

    // [Get charges]
    @GET("paytm-credentials")
    Call<ResponseBody> paytmCredentials();

    // [Get charges]
    @GET("charges")
    Call<ResponseBody> getCharges();

    // [Get abouts]
    @GET("abouts")
    Call<ResponseBody> getAbouts();
}
