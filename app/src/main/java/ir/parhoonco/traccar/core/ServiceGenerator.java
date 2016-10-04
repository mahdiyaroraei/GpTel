package ir.parhoonco.traccar.core;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;

import java.io.IOException;
import java.lang.annotation.Annotation;

import ir.parhoonco.traccar.core.model.api.Error;
import ir.parhoonco.traccar.ui.dialog.ErrorDialog;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ir.parhoonco.traccar.ui.fragment.MapFragment.dialog;

/**
 * Created by mao on 9/3/2016.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "http://148.251.245.14:8082/api/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null , null);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password , final Context context) {
        if (username != null && password != null) {
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    ApplicationLoader.isInternetAvailable();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    final Response response = chain.proceed(request);
                    final ResponseBody responseBody = response.body();
                    final MediaType mediaType = responseBody.contentType();
                    final String body = responseBody.string();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (response.code() != 200 && response.code() != 204){
                                ErrorDialog dialog = new ErrorDialog();

                                Converter<ResponseBody, Error> errorConverter =
                                        ApplicationLoader.retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                                try {
                                    Error error = errorConverter.convert(ResponseBody.create(mediaType, body));
                                    dialog.showDialogMessage((Activity)context, error.getMessage());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();

                    return response.newBuilder()
                            .body(ResponseBody.create(mediaType, body))
                            .build();
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}