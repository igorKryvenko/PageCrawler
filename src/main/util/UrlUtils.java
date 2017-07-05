package main.util;

import java.util.Arrays;

/**
 * Created by igor on 06.07.17.
 */
public class UrlUtils {
    private static final String[] startWidth={"javascript:","#"};
    private static final String[] endWidth={".swf",".pdf",".png",".gif",".jpg",".jpeg"};

    public static boolean checkUrl(String url){
        boolean start = Arrays.stream(startWidth).filter(row->url.startsWith(row)).count()>0;
        boolean end  = Arrays.stream(endWidth).filter(row -> url.endsWith(row)).count()>0;

        if (start & end) {
            return true;}
        else return false;
    }

    public static String normalize(String targetUrl, String baseUrl) {
        if(targetUrl.startsWith("http://"))                   return targetUrl;
        if(targetUrl.startsWith("https://"))                  return targetUrl;
        if(targetUrl.toLowerCase().startsWith("javascript:")) return targetUrl;


        StringBuilder builder = new StringBuilder();

        if(!baseUrl.startsWith("http://")) {
            builder.append("http://");
        }

        if(targetUrl.startsWith("/")) {

            int endOfDomain = baseUrl.indexOf("/", 7);
            if(endOfDomain == -1){
                builder.append(baseUrl);
            } else {
                builder.append(baseUrl.substring(0, endOfDomain));
            }
            builder.append(targetUrl);

        } else {

            int lastDirSeparatorIndex = baseUrl.lastIndexOf("/");
            if(lastDirSeparatorIndex > 6){
                builder.append(baseUrl.substring(0, lastDirSeparatorIndex));
            } else {
                builder.append(baseUrl);
            }
            builder.append('/');
            builder.append(targetUrl);
        }


        int fragmentIndex = builder.indexOf("#");
        if(fragmentIndex > -1) {
            builder.delete(fragmentIndex, builder.length());
        }


        int indexOfDirUp = builder.indexOf("../");
        while(indexOfDirUp > -1){
            int indexOfLastDirBeforeDirUp = builder.lastIndexOf("/", indexOfDirUp - 2);
            if(indexOfLastDirBeforeDirUp > -1 ) {
                builder.delete(indexOfLastDirBeforeDirUp +1, indexOfDirUp + 3);
            }
            indexOfDirUp = builder.indexOf("../");
        }

        return builder.toString();

    }
}
