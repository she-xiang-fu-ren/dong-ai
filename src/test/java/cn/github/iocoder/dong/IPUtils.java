package cn.github.iocoder.dong;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import java.util.concurrent.TimeUnit;

public class IPUtils {
    private static Searcher searcher = null;
    static {
        String dbPath = "src/main/resources/data/ip2region.xdb";

        // 1、从 dbPath 加载整个 xdb 到内存。
        byte[] cBuff = new byte[0];
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
        }
    }

    public static void main(String[] args) {
        getIP("172.17.0.6");
        getIP("117.30.118.48");
        getIP("117.30.118.48");
        // 3、查询


        // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
        // searcher.close();

        // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
    }

    public static void getIP(String ip){
        try {
            String region = searcher.search(ip);
//            System.out.println(region);
            for (int i = 0; i < 2; i++) {
                region = region.substring(region.indexOf("|")+1);
            }
            System.out.println(region);
        } catch (Exception e) {
        }
    }

//    public static void main(String[] args) {
//        // 1、创建 searcher 对象
//        String dbPath = "D:\\Idea2023\\dong-ai\\src\\main\\resources\\data\\ip2region.xdb";
//        Searcher searcher = null;
//        try {
//            searcher = Searcher.newWithFileOnly(dbPath);
//        } catch (IOException e) {
//            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
//            return;
//        }
//
//        // 2、查询
//        try {
//            String ip = "172.28.80.1";
//            long sTime = System.nanoTime();
//            String region = searcher.search(ip);
//            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
//            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
//        } catch (Exception e) {
////            System.out.printf("failed to search(%s): %s\n", ip, e);
//        }
//
//        // 3、关闭资源
//        try {
//            searcher.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
//    }
}
