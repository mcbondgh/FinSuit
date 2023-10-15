package app.config.sms;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Map;
import java.util.Objects;

public class HttpClientExample {
    public static void main(String[] args) throws Exception {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://jsonplaceholder.typicode.com/todos/1")
                .build();
        Gson gson = new Gson();
        try (Response response = client.newCall(request).execute()) {
            String responseBody = Objects.requireNonNull(response.body()).string();
            Map<String, Object> map = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>(){}.getType());
//            System.out.println(responseBody);
            String value = map.get("userId").toString();
            System.out.println(value);


        }



    }
}
