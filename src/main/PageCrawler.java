package main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by igor on 17.06.17.
 */
public class PageCrawler {
    public LinkedBlockingQueue<String> link = new LinkedBlockingQueue<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    public Set<String> result = new HashSet<>();
    private String url;
    private CountDownLatch latch = new CountDownLatch(1);

    public PageCrawler(String url) {
        this.url = url;
        this.link.add(url);
    }

    public void addUrl(String url) {
        link.add(url);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void start() throws InterruptedException, BrokenBarrierException {
        String next;
        while (!link.isEmpty()) {
            next = link.take();

            if (result.contains(next)) continue;
            if (!validUrl(next)) continue;
            result.add(next);


            CrawlerJob job = new CrawlerJob(next, this);
            executor.execute(job);

            if (link.isEmpty()) {
                Thread.sleep(3000);
            }


        }
        executor.shutdown();

        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    }

    private boolean validUrl(String url) {
        if (url.startsWith("javascript")) {
            return false;
        }
        if (url.startsWith("#")) {
            return false;
        }
        if (url.endsWith(".pdf")) {
            return false;
        }
        if (url.endsWith(".png")) {
            return false;
        }
        if (url.endsWith(".jpeg")) {
            return false;
        }
        if (url.endsWith(".gif")) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) throws BrokenBarrierException {
        PageCrawler crawler = new PageCrawler("https://habrahabr.ru");
        try {
            crawler.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
