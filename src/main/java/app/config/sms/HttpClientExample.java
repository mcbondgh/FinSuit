package app.config.sms;

public class HttpClientExample {
//    public static void main(String[] args) throws Exception {
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://jsonplaceholder.typicode.com/todos/1")
//                .build();
//        Gson gson = new Gson();
//        try (Response response = client.newCall(request).execute()) {
//            String responseBody = Objects.requireNonNull(response.body()).string();
//            Map<String, Object> map = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>() {
//            }.getType());
////            System.out.println(responseBody);
//            String value = map.get("userId").toString();
//
//
//            Timer timer = new Timer();
//            TimerTask timerTask = getTimerTask(timer);
//
//            timer.scheduleAtFixedRate(timerTask, 0, 1500);
//        }
//    }
//
//    @NotNull
//    private static TimerTask getTimerTask(Timer timer) {
//        String[] numbers = {"1", "4", "43", "33", "2"};
//        TimerTask timerTask = new TimerTask() {
//            final int size = numbers.length;
//            int count = 0;
//
//            @Override
//            public void run() {
//                if (count != size) {
//                    System.out.println(numbers[count]);
//                    count++;
//                } else {
//                    timer.cancel();
//                }
//            }
//        };
//        return timerTask;
//    }
}
