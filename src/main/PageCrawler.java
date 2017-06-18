package main;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by igor on 17.06.17.
 */
public class PageCrawler {
    private LinkedBlockingQueue<String> link = new LinkedBlockingQueue<>();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private Set<String> result = new HashSet<>();
    private String url;
    private CountDownLatch latch = new CountDownLatch(2);

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

    public void start() throws InterruptedException {
        String next;
        while(!link.isEmpty()) {

            next = link.take();
            System.out.println("Next " + next);
            if(result.contains(next)) continue;
            if(!validUrl(next)) continue;
            result.add(next);
            System.out.println(result);
            CrawlerJob job = new CrawlerJob(next,this);
            executor.submit(job);

            if(link.isEmpty()) {
                latch.await();
            }

            executor.shutdown();

            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }
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

    public static void main(String[] args) {
        PageCrawler crawler = new PageCrawler("https://habrahabr.ru");
        try {
            crawler.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
