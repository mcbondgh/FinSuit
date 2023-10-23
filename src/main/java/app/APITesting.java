package app;

import okhttp3.*;

public class APITesting {

    public static void main(String[] args) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://api.paystack.co/";

        RequestBody requestBody = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(url).post(requestBody).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {

            }
        }catch (Exception e) {e.printStackTrace();};


    }


}
