package app.config.sms;

import java.util.Timer;
import java.util.TimerTask;

public class HttpClientExample {
    public static void main(String[] args) throws Exception {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/todos/1")
//                .build();
//        Gson gson = new Gson();
//        try (Response response = client.newCall(request).execute()) {
//            String responseBody = Objects.requireNonNull(response.body()).string();
//            Map<String, Object> map = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>(){}.getType());
////            System.out.println(responseBody);
//            String value = map.get("userId").toString();

            Timer timer = new Timer();
            String[] numbers = {"1", "4", "43", "33", "2"};
            TimerTask timerTask = new TimerTask() {
                final int size = numbers.length;
                int count = 0;
                @Override
                public void run() {
                    if (count != size) {
                        System.out.println(numbers[count]);
                        count++;
                }else {
                        timer.cancel();
                    }
            }
            };

            timer.scheduleAtFixedRate(timerTask, 1400, 1500);
        }
}
