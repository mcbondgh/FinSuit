package app.config.sms;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class HttpClientExample {
    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://kwegyiraggrey.com/admission")
                .build();
        Gson gson = new Gson();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
             gson.toJson(responseBody);
            System.out.println(gson);

//            System.out.println(responseBody);
        }
    }
}
