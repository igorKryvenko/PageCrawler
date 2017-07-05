package main;

import main.util.UrlUtils;
import org.jsoup.HttpStatusException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.net.UnknownHostException;


/**
 * Created by igor on 17.06.17.
 */
public class CrawlerJob implements Runnable {


    private String url;
    private PageCrawler pageCrawler;


    public CrawlerJob(String url, PageCrawler pageCrawler) {
        this.url = url;
        this.pageCrawler = pageCrawler;
    }

    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
            Elements elements = document.getElementsByTag("a");
            String baseUri = document.baseUri();
            elements.forEach(e -> {
                pageCrawler.addUrl(UrlUtils.normalize(e.attr("href"), baseUri));
                System.out.println(UrlUtils.normalize(e.attr("href"), baseUri));
            });
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                System.out.println("Server doesn't support https");
            } else if (e instanceof HttpStatusException) {
                System.out.println("Service is unavailable");
            } else e.printStackTrace();
        }

    }
}
