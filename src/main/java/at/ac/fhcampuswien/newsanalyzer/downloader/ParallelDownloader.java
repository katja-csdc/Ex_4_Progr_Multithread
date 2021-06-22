package at.ac.fhcampuswien.newsanalyzer.downloader;

import at.ac.fhcampuswien.newsapi.NewsApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParallelDownloader extends Downloader {

    @Override
    public int process(List<String> urls) {
        int count = 0;

        try {
            // TODO implement parallel saving
            if(urls.size() <= 0) {
                throw new NewsApiException("URL-List is empty!");
            }

            ExecutorService service = Executors.newFixedThreadPool(urls.size());
            List<ProcessTask> list = new ArrayList<>();
            List<Future<String>> futures = null;

            for (String url : urls) {
                ProcessTask c = new ProcessTask(url);
                list.add(c);
            }

            try {
                futures = service.invokeAll(list);
            } catch (InterruptedException e) {
                System.out.println("Something went wrong while parallel execution");
            }

            try {
                for (Future<String> f : futures) {
                    System.out.println(f.get());
                    if (f.get().equalsIgnoreCase("success"))
                        count++;
                }
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Something went wrong while getting results of execution!");
            }

            return count;
        }
        catch (NewsApiException e){
            System.out.println(e.getMessage());
            return 0;
        }
    }
}

class ProcessTask implements Callable<String> {
    private String url;

    public ProcessTask(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        try {
            String fileName = Downloader.saveUrl2File(url);

            if (fileName == null) {
                throw new NewsApiException("Name of file in thread was NULL!");
            } else {
                return "SUCCESS";
            }

        } catch (NewsApiException e) {
            System.out.println("There was an error in thread!");
            return "FAIL";
        }
    }
}
