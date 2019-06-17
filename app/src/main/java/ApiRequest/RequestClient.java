package ApiRequest;


import android.arch.lifecycle.BuildConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import majorpull.com.majorpull.util.DebugLog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 3/10/17.
 */

public class RequestClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, final String Accesstoken) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS);

      //  if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
       // }
        if (Accesstoken != null) {


            DebugLog.log(1, "Retrofit", "Accesstoken" + Accesstoken);
//                Log.v(1, "Retrofit", "Accesstoken" + Accesstoken);

            builder.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header(ApiClass.getmApiClass().AUTHORIZATION, "Bearer" + Accesstoken)
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        return retrofit;

    }
}