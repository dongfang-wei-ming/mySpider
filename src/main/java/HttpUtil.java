import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 叶刚诚
 * @create 2021-11-26-16:09
 */
public class HttpUtil {
    private static OkHttpClient okHttpClient;
    private static int num = 0;

    static {
        //参数设置
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
                .build();
    }

    public static Response get(String path) {
        //创建连接客户端
        Request request = new Request.Builder()
                .url(path)
                .header("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36")
                .build();
        //创建"调用" 对象
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();//执行
            if (response.isSuccessful()) {
                return response;
            }
        } catch (IOException e) {
            System.out.println("链接格式有误:" + path);
        }
        return null;
    }
    public static String urlHandler(String picture_url){
        if (picture_url.startsWith("//")) {
            picture_url = "https:" + picture_url;
        }
        if (!picture_url.contains("http://") && !picture_url.contains("https://")) {
            picture_url = "https://" + picture_url;
        }
        return picture_url;
    }
}
