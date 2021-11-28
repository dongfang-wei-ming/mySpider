import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;
import okio.Sink;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spider {
    public static Pattern pattern = Pattern.compile(".*(.jpg|.png|.gif|.jpeg|.bmp)$");
    public static String path = "";
    public static int num = 1, sum = 0;
    /**
     * 获取目标网站所有的图片地址
     * @param path 目标地址,dPath 下载目标
     */
    public static void getAllLinks(String path,String dPath) throws IOException {
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(path),5000);
        } catch (Exception e) {
            System.out.println("无法连接目标。");
            return;
        }
        Elements select = doc.getElementsByTag("img");
        sum=select.size();
        if(sum ==0){
            System.out.println("找不到图片。");
            return ;
        }
        for(Element e : select){
            String picture_url = e.attr("abs:src");
            getter(picture_url,dPath);
        }
    }

    //用得到的url下载图片
    private static void getter(String picture_url, String dPath) throws IOException {
        //对图片路径进行处理
        if (picture_url == null ||picture_url.length()==0) return;
        final String pUrl = HttpUtil.urlHandler(picture_url);
        //获取图片名
        String[] split = pUrl.split("/");
        String name = split[split.length - 1];
        Matcher matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            name = UUID.randomUUID().toString() + ".jpg";
        }
        //用okhttp获取响应
        Response response = HttpUtil.get(pUrl);
        InputStream inputStream = response.body().byteStream();
        FileOutputStream fos = new FileOutputStream(dPath + "\\" + name);
        //用传统io读写文件
        byte[] b = new byte[1024];
        int byteread = 0;
        while ((byteread = inputStream.read(b)) != -1) {
            fos.write(b, 0, byteread);
        }

        System.out.println("已经完成第" + (num++) + "张，" + "总共" + sum + "张。");

    }

    public static void main(String[] args) throws IOException {
        //人机交互
        Scanner scanner=new Scanner(System.in);
        System.out.print("请输入要爬取的网站(带http/https前缀):");
        String str=scanner.nextLine();
        System.out.print("请输入储存的文件夹(默认在D:\\spider):");

        //储存路径处理
        String str1=scanner.nextLine();
        String trim = str1.trim();
        if(trim.length()==0) str1="D:\\spider";
        path=str;

        //获取开始时间
        long startTime = System.currentTimeMillis();
        System.out.println("正在爬取中，请等待.....");
        //开始爬取目标内容
        Spider.getAllLinks(path,str1);
        //获取结束时间
        long endTime = System.currentTimeMillis();
        //输出程序运行时间
        System.out.println("\n程序运行时间：" + (endTime - startTime) + "ms");
    }
}
