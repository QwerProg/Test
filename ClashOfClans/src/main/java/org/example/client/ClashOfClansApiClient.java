package org.example.client;

import org.example.pojo.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ClashOfClansApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ClashOfClansApiClient.class);
    private final RestTemplate restTemplate;

    @Value("${coc.api.baseurl}")
    private String apiBaseUrl;

    @Value("${coc.api.token}")
    private String apiToken;

    @Autowired
    public ClashOfClansApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Player fetchPlayerData(String playerTag) {
        // Player tags in the API path must be URL encoded.
        // The # character should be replaced with %23.
        String encodedPlayerTag = UriComponentsBuilder.fromPath(playerTag).build().toString();
        if (encodedPlayerTag.startsWith("#")) { // UriComponentsBuilder might not encode # if it's the first char
            encodedPlayerTag = "%23" + encodedPlayerTag.substring(1);
        }


        String url = apiBaseUrl + "/players/" + encodedPlayerTag;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Accept", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            logger.debug("Requesting player data from URL: {}", url);
            ResponseEntity<Player> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Player.class
            );
            logger.info("Successfully fetched data for player tag: {}, Status: {}", playerTag, response.getStatusCode());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("HttpClientErrorException for player tag {}: {} - Response Body: {}",
                    playerTag, e.getStatusCode(), e.getResponseBodyAsString(), e);
            // 你可以根据 e.getStatusCode() 抛出更具体的自定义业务异常
            // 例如: if (e.getStatusCode().is4xxClientError()) throw new PlayerNotFoundException(...)
            throw e; // 重新抛出，让Service层或全局异常处理器处理
        } catch (Exception e) {
            logger.error("Generic exception fetching data for player tag {}: {}", playerTag, e.getMessage(), e);
            throw new RuntimeException("Error fetching player data from CoC API", e);
        }
    }
}