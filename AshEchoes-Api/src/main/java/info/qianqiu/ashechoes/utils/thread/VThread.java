package info.qianqiu.ashechoes.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// 虚拟线程
public class VThread {

    public static ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

    public static Future<?> submit(Runnable runnable) {
        return service.submit(runnable);
    }

}
