package mx.edu.uth.ws;
import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class WebApiAdapter {
    private static WebApiService API_SERVICE;
    private static  final String BASE_URL ="https://jeconnoob.com/ws_app_restful/ws/";
    public static WebApiService getApiService() {
        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (API_SERVICE == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

            API_SERVICE = retrofit.create(WebApiService.class);
        }
        return API_SERVICE;
    }
}
