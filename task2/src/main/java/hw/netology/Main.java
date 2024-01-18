package hw.netology;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.axet.vget.VGet;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Main {
    public static ObjectMapper mapper = new ObjectMapper();
    public static final String URL = "https://api.nasa.gov/planetary/apod?api_key=";
    public static final String TOKEN = "aRSrUUUV125CG1MTACDQE2rymTYwDEDGTe4no3im";

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URL + TOKEN);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        CloseableHttpResponse response = httpClient.execute(request);

        String body = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        Fact fact = mapper.readValue(body, new TypeReference<Fact>() {
        });
        response.close();
        httpClient.close();
        String name = fact.nameFile();
        String type = name.split("\\.", 2)[1];
        if (fact.getMediaType().equals("image")) {
            BufferedImage img = ImageIO.read(new URL(fact.getHdurl()));
            ImageIO.write(img, type, new File(name));
        } else if (fact.getMediaType().equals("video")) {
// не уверен, что выйдет - пробовал на примере с видео из ютуба, выходит ошибка. Пока разбираюсь, в чём дело(
            VGet video = new VGet(new URL(fact.getHdurl()), new File(name));
            video.download();
        }
    }
}