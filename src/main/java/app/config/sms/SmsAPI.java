package app.config.sms;

import app.models.MainModel;
import app.repositories.SmsAPIEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;


public class SmsAPI {

//    OK - Successfully Sent
//    100 - Bad gateway request
//    101 - Wrong action
//    102 - Authentication failed
//    103 - Invalid phone number
//    104 - Phone coverage not active
//    105 - Insufficient balance
//    106 - Invalid Sender ID
//    109 - Invalid Schedule Time
//    111 - SMS contains spam word. Wait for approval

    private String api;
    private String checkBalanceUrl = "https://sms.arkesel.com/sms/api?action=check-balance&api_key=";
    private String sendSmsUrl = "https://sms.arkesel.com/sms/api?action=send-sms&api_key=";
    private String senderId;
    Gson gson = new Gson();
    MainModel MODEL_OBJECT = new MainModel();

    /* CHECK SMS BALANCE */
    public String getSmsBalance() throws IOException {
        for(SmsAPIEntity item : MODEL_OBJECT.getSmsApi()) {api = item.getKey();}
        String balance = "";
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().get().url(checkBalanceUrl + api+"&response=json").build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String jsonValue = response.body().string();
            Map<String, Object> map = gson.fromJson(jsonValue, new TypeToken<Map<String, Object>>(){}.getType());
            balance = map.get("balance").toString();
        }
        return balance;
    }
    public String sendSms(String mobileNumber, String messageBody) throws IOException {
        for(SmsAPIEntity item : MODEL_OBJECT.getSmsApi()) {
            api = item.getKey();
            senderId = item.getSender_id();
        }
        String status = "";
        //https://sms.arkesel.com/sms/api?action=send-sms&api_key=T2lVanFDcUdoR0VqYm1Zd3pyVGY&to=PhoneNumber&from=SenderID&sms=YourMessage
        OkHttpClient client = new OkHttpClient().newBuilder().build();
//        Map<String, String> mappedValues = new LinkedHashMap<>();
//        mappedValues.put("sender", senderId);
//        mappedValues.put("message", messageBody);
//        mappedValues.put("recipients", mobileNumber);

//        RequestBody jsonRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(mappedValues));
//        RequestBody jsonRequestBody = RequestBody.create(gson.toJson(mappedValues), MediaType.parse("application/json; charset=utf-8"));
//        System.out.println(jsonRequestBody.toString());

        Request request = new Request.Builder()
                .url(sendSmsUrl + api + "&to=" + mobileNumber + "&from="+senderId + "&sms=" + messageBody) .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String responseBody = Objects.requireNonNull((response.body()).string());
            Map<String, Object> map = gson.fromJson(responseBody, new TypeToken<Map<String, Object>>() {
            }.getType());
            status = map.get("code").toString();
        } catch (IOException e) {
            throw new RuntimeException("Network failed, there seem to be no internet...");
        }
        return status;
    }
//    public static void main(String[] args) throws IOException {
//        SmsAPI api1 = new SmsAPI();
//        System.out.println(api1.getSmsBalance());
////        System.out.println(api1.sendSms("0246453922", "this was sent from the terminal"));
//    }



}
