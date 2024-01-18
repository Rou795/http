package hw.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

// Создание клиента для направления запросов серверу, устанавливаем общие настройки соединения

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

// Создание обекта запроса GET - создаётся для редактуры самого запроса в

        HttpGet request = new HttpGet(URL);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

// отправка запроса, через настроенного выше клиента и получение ответа от сервера

        CloseableHttpResponse response = httpClient.execute(request);

// помещение полученной ифнормации из response (тело) в переменную
        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

//  преобразование текста из переменной в объекты с последующим выводом

        List<Fact> facts = mapper.readValue(body, new TypeReference<List<Fact>>() {});
        facts.stream().filter(value -> value.getUpvotes() > 0).forEach(System.out::println);
        response.close();
        httpClient.close();
    }
}