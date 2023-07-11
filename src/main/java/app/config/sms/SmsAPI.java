package app.config.sms;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;




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

    private String api = "T2lVanFDcUdoR0VqYm1Zd3pyVGY";
    private String checkBalanceUrl = "https://sms.arkesel.com/sms/api?action=check-balance&api_key=";
    private String sendSmsUrl = "https://sms.arkesel.com/sms/api?action=send-sms&api_key=";
    private String senderId = "SERVER";
    Gson gson = new Gson();


    /* CHECK SMS BALANCE */
    public String getSmsBalance() throws IOException {
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

    public String sendSms(String mobileNumber, String messageBody) {
        String status = "";
    //https://sms.arkesel.com/sms/api?action=send-sms&api_key=T2lVanFDcUdoR0VqYm1Zd3pyVGY&to=PhoneNumber&from=SenderID&sms=YourMessage
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Map<String, String> mappedValues = new LinkedHashMap<>();
        mappedValues.put("sender", senderId);
        mappedValues.put("message", messageBody);
        mappedValues.put("recipients", mobileNumber);

        RequestBody jsonRequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(mappedValues));
        System.out.println(jsonRequestBody.toString());

        Request request = new Request.Builder()
//                .url("https://sms.arkesel.com/api/v2/sms/send")
                .url(sendSmsUrl + api + "&to=" + mobileNumber + "&from="+senderId + "&sms=" + messageBody)
                .addHeader("api-key", "T2lVanFDcUdoR0VqYm1Zd3pyVGY")
                .addHeader("Content-Type", "application/json")
                .post(jsonRequestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            status = response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return status;
    }
    public static void main(String[] args) throws IOException {
        SmsAPI api1 = new SmsAPI();
        System.out.println(api1.sendSms("0246453922", "this was sent from the terminal"));
    }

}
