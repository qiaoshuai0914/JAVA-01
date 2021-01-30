import okhttp3.*;

import java.io.IOException;

public class OkHttpService {

    public static OkHttpClient client = new OkHttpClient();

    public static String requestGet(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        Response response = call.execute();
        String res = response.body().string();
        return res;
    }
    public static  void asynchronousGet(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {//异步请求
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }
                System.out.println("success result:" + response.body().string());
            }
        });
    }
    public static void main(String[] args) {

        String accessUrl = "http://baidu.com";
        try {
            //发送请求
            String resBody = OkHttpService.requestGet(accessUrl);
            System.out.println(resBody);

            OkHttpService.asynchronousGet(accessUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

