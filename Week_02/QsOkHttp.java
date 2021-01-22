public class QsOkHttp{
  public satic void main(String[] args){
    requestGet("http://localhost:8801");
  }


  public String requestGet(String url)throws Exception{
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder().get(url).url(),build();
    Call call = client.newCall(request);
    Response response = call.execute();
    String res = response.body().string();
    return res;
  }
}

