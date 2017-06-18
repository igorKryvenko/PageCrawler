package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by igor on 17.06.17.
 */
public class CrawlerJob implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerJob.class);

    private String url;
    private PageCrawler pageCrawler;
    private AtomicInteger integer;

    public CrawlerJob(String url, PageCrawler pageCrawler) {
        this.url = url;
        this.pageCrawler = pageCrawler;
    }

    @Override
    public void run() {
        try {
            System.out.println("Url" + url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("a");
            elements.forEach(e -> {
                        pageCrawler.addUrl(e.attr("href"));
                System.out.println(e.attr("href"));

                //System.out.println("Count of elements" + this.integer.incrementAndGet());
                        logger.debug(e.attr("href"));
                    }

            );
            if(this.pageCrawler.getLatch().getCount() == 1) {
                try {
                    this.pageCrawler.getLatch().await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
