package cn.github.iocoder.dong;
import org.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class BaiduMessageTest {
        public static final String API_KEY = "GArckN****hR8ZfR";
        public static final String SECRET_KEY = "8B4pkM****nuHSeT";

        static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

        public static void main(String []args) throws IOException {
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro?access_token=" + getAccessToken())
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            System.out.println(response.body().string());

        }


        /**
         * 从用户的AK，SK生成鉴权签名（Access Token）
         *
         * @return 鉴权签名（Access Token）
         * @throws IOException IO异常
         */
        static String getAccessToken() throws IOException {
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                    + "&client_secret=" + SECRET_KEY);
            Request request = new Request.Builder()
                    .url("https://aip.baidubce.com/oauth/2.0/token")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Response response = HTTP_CLIENT.newCall(request).execute();
            return new JSONObject(response.body().string()).getString("access_token");
        }


}
