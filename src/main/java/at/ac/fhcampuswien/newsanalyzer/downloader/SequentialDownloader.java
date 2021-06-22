package at.ac.fhcampuswien.newsanalyzer.downloader;

import at.ac.fhcampuswien.newsapi.NewsApiException;

import java.util.List;

public class SequentialDownloader extends Downloader {

    @Override
    public int process(List<String> urls) {
        int count = 0;

        try {
            if (urls != null) {
                for (String url : urls) {
                    String fileName = saveUrl2File(url);
                    if (fileName != null)
                        count++;
                }
                return count;
            } else {
                throw new NewsApiException("List of URLS is empty!");
            }
        } catch (NewsApiException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
