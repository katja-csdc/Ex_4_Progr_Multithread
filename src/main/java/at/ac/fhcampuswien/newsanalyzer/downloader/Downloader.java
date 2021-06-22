package at.ac.fhcampuswien.newsanalyzer.downloader;

import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Downloader {

    public static final String HTML_EXTENTION = ".html";
    public static final String DIRECTORY_DOWNLOAD = "./download/";

    public abstract int process(List<String> urls);

    public List<String> urlsToList(List<Article> articleList) {
        try {
            if (articleList == null)
                throw new NewsApiException("Article list is NULL");

            return articleList.stream()
                    .map(Article::getUrl)
                    .collect(Collectors.toList());
        } catch (NewsApiException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String saveUrl2File(String urlString) {
        InputStream is = null;
        OutputStream os = null;
        String fileName = "";
        try {
            URL url4download = new URL(urlString);
            try {
                is = url4download.openStream();
            }
            catch (IOException e){
                System.out.println("Something went wrong while loading URL: " + urlString);
                return null;
            }

            fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
            // fileName = fileName.replaceAll("^[^<>*%:&?/|\\\\]*$", "");
            if (fileName.isEmpty()) {
                fileName = url4download.getHost() + HTML_EXTENTION;
            }

            if(!fileName.endsWith(HTML_EXTENTION))
                fileName += HTML_EXTENTION;

            os = new FileOutputStream(DIRECTORY_DOWNLOAD + fileName);

            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(is).close();
                Objects.requireNonNull(os).close();
            } catch (IOException e) {
                System.out.println("There was NULL reference!");
            }
        }
        return fileName;
    }
}
