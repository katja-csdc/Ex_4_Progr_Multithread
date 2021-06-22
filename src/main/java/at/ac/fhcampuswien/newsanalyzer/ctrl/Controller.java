package at.ac.fhcampuswien.newsanalyzer.ctrl;


import at.ac.fhcampuswien.newsanalyzer.downloader.Downloader;
import at.ac.fhcampuswien.newsanalyzer.downloader.ParallelDownloader;
import at.ac.fhcampuswien.newsanalyzer.downloader.SequentialDownloader;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.SortBy;


import java.util.*;
import java.util.stream.Collectors;

import static at.ac.fhcampuswien.newsapi.enums.Endpoint.TOP_HEADLINES;

public class Controller {

    public static final String APIKEY = "52c142235f584b9590a9663822397532";  //TODO add your api key

    Downloader downloader;

    public void process(String searchWord) {
        System.out.println("Start process");

        //TODO implement Error handling
        try {
            if (searchWord.isEmpty())
                throw new NewsApiException("Search word is empty!");

            //TODO load the news based on the parameters
            NewsApi newsApi = new NewsApiBuilder()
                    .setApiKey(APIKEY)
                    .setQ(searchWord)
                    .setEndPoint(TOP_HEADLINES)
                    .setSourceCountry(Country.at)
                    .createNewsApi();

            NewsResponse newsResponse = newsApi.getNews();

            if (newsResponse != null) {
                List<Article> articles = newsResponse.getArticles();
                articles.stream().forEach(article -> System.out.println(article.toString()));
            } else {
                throw new NewsApiException("JSON Responce is empty!");
            }

            List<Article> articles = newsResponse.getArticles();

            // Download URLs to file
            this.downloader = new SequentialDownloader();
            long start = System.nanoTime();
            downloader.process(downloader.urlsToList(articles));
            long end = System.nanoTime();

            System.out.println("Executions time for sequential download is: " + (end - start) / 1000000 + " seconds");

            this.downloader = new ParallelDownloader();
            start = System.nanoTime();
            downloader.process(downloader.urlsToList(articles));
            end = System.nanoTime();

            System.out.println("Executions time for parallel download is: " + (end - start) / 1000000 + " seconds");

            //TODO implement methods for analysis
            //Number of articles found
            System.out.println("\nNumber of articles for your search is " + articles.size());

            //Count of most providers

            String prov = articles.stream()
                    .collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
                    .entrySet().stream()
                    .max(Comparator.comparingInt(t -> t.getValue().intValue()))
                    .get()
                    .getKey();

            if (prov != null)
                System.out.println("Provider delivers the most articles: " + prov);
            else
                System.out.println("There is no provider");

            //Shortest author's  name

            String authorsName = articles.stream()
                    .filter(article -> Objects.nonNull(article.getAuthor()))
                    .min(Comparator.comparingInt(article -> article.getAuthor().length()))
                    .get()
                    .getAuthor();

            if (authorsName != null)
                System.out.println("The shortest author's name is \"" + authorsName + "\"");
            else
                System.out.println("There is no authorsName");


            //Sort articles by longest title by alphabet

            List<Article> sortArticles = articles.stream()
                    .sorted(Comparator.comparingInt(article -> article.getTitle().length()))
                    .sorted(Comparator.comparing(Article::getTitle))
                    .collect(Collectors.toList());

            if (sortArticles != null)
                System.out.println("There is the longest title!");
            else
                System.out.println("Nothing to show");

        } catch (NewsApiException e) {
            System.out.println(e.getMessage());
        } catch (NoSuchElementException e) {
			System.out.println("Element wasn't found in Map!");
        } finally {
            System.out.println("End process");
        }
    }


    public Object getData() {

        return null;
    }
}