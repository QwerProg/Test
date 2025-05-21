package org.example.service.impl;

import org.example.client.ClashOfClansApiClient;
import org.example.pojo.Player;
import org.example.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;


@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private final ClashOfClansApiClient apiClient;

    @Autowired
    public PlayerServiceImpl(ClashOfClansApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Player getPlayerInfo(String playerTag) {
        if (playerTag == null || playerTag.trim().isEmpty() || !playerTag.trim().startsWith("#")) {
            logger.warn("Invalid player tag format provided: {}", playerTag);
            // 在实际应用中，可以抛出自定义的业务异常
            // throw new InvalidPlayerTagFormatException("Player tag must start with # and not be empty.");
            return null;
        }

        try {
            return apiClient.fetchPlayerData(playerTag);
        } catch (HttpClientErrorException.NotFound e) {
            logger.warn("Player not found for tag {}: {}", playerTag, e.getMessage());
            return null; // 或者抛出自定义的 PlayerNotFoundException
        } catch (HttpClientErrorException e) {
            logger.error("Client error when fetching player {}: Status {}, Message {}",
                    playerTag, e.getStatusCode(), e.getResponseBodyAsString());
            // 可以根据不同的客户端错误做不同处理或抛出不同异常
            return null;
        } catch (Exception e) {
            logger.error("An unexpected error occurred while fetching player info for tag {}: {}",
                    playerTag, e.getMessage(), e);
            // 抛出通用业务异常或返回null
            return null;
        }
    }
}