package ibf2022.ssf.newsapi.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import ibf2022.ssf.newsapi.models.News;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class NewsServices {

    @Value("${api.key}")
    private String API_KEY;

    @Value("${api.url}")
    private String URL;

    public Optional<List<News>> getEverything(String query) {
        String url = UriComponentsBuilder
                .fromUriString(URL + "/everything")
                .queryParam("q", query)
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Key", API_KEY)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;

        String payload = "";
        int statusCode = 0;
        try {
            resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            statusCode = resp.getStatusCode().value();
        } catch (HttpClientErrorException ex) {
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
            return Optional.empty();
        } finally {
            // System.out.printf("URL: %s\n", url);
            // System.out.printf("Payload: %s\n", payload);
            // System.out.printf("Status Code: %s\n", statusCode);
        }

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();
        JsonArray articles = json.getJsonArray("articles");

        String title;
        String description;
        String author;
        String imageUrl;
        String content;
        List<News> newsList = new LinkedList<>();

        for (int i = 0; i < articles.size(); i++) {
            JsonObject data = articles.getJsonObject(i);
            News news = new News();

            title = "%s".formatted(data.getString("title"));
            description = "%s".formatted(data.getString("description"));
            url = data.getString("url");
            imageUrl = data.getString("urlToImage",
                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fsearch%2Fimages%3Fk%3Dno%2Bimage%2Bavailable&psig=AOvVaw3ZJCMEiz2qwlK99WFuBqe6&ust=1677311296710000&source=images&cd=vfe&ved=0CA8QjRxqFwoTCLiW09HVrf0CFQAAAAAdAAAAABAJ");
            author = "Author: %s".formatted(data.getString("author", "john doe"));
            content = "Content: %s".formatted(data.getString("content"));

            news.setTitle(title);
            news.setAuthor(author);
            news.setDescription(description);
            news.setUrl(url);
            news.setImageUrl(imageUrl);
            news.setContent(content);
            newsList.add(news);
        }
        return Optional.of(newsList);
    }

    public Optional<List<News>> getTopHeadlines() {
        String url = UriComponentsBuilder
                .fromUriString(URL + "/top-headlines")
                .queryParam("country", "sg")
                .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Api-Key", API_KEY)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = null;

        String payload = "";
        int statusCode = 0;
        try {
            resp = restTemplate.exchange(req, String.class);
            payload = resp.getBody();
            statusCode = resp.getStatusCode().value();
        } catch (HttpClientErrorException ex) {
            payload = ex.getResponseBodyAsString();
            statusCode = ex.getStatusCode().value();
            return Optional.empty();
        } finally {
            // System.out.printf("URL: %s\n", url);
            // System.out.printf("Payload: %s\n", payload);
            // System.out.printf("Status Code: %s\n", statusCode);
        }

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject json = reader.readObject();
        JsonArray articles = json.getJsonArray("articles");

        String title;
        String description;
        String author;
        String imageUrl;
        String content;
        List<News> newsList = new LinkedList<>();

        for (int i = 0; i < articles.size(); i++) {
            JsonObject data = articles.getJsonObject(i);
            News news = new News();

            title = "%s".formatted(data.getString("title"));
            description = "%s".formatted(data.getString("description"));
            url = data.getString("url");
            imageUrl = data.getString("urlToImage",
                    "https://www.google.com/url?sa=i&url=https%3A%2F%2Fstock.adobe.com%2Fsearch%2Fimages%3Fk%3Dno%2Bimage%2Bavailable&psig=AOvVaw3ZJCMEiz2qwlK99WFuBqe6&ust=1677311296710000&source=images&cd=vfe&ved=0CA8QjRxqFwoTCLiW09HVrf0CFQAAAAAdAAAAABAJ");
            author = "Author: %s".formatted(data.getString("author", "john doe"));
            content = "Content: %s".formatted(data.getString("content", "No content."));

            news.setTitle(title);
            news.setAuthor(author);
            news.setDescription(description);
            news.setUrl(url);
            news.setImageUrl(imageUrl);
            news.setContent(content);
            newsList.add(news);
        }
        return Optional.of(newsList);
    }
}
