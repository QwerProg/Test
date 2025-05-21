package org.example.controller;

import org.example.pojo.Player;
import org.example.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // API端点示例: /api/v1/players/%23YOURTAG (注意#需要进行URL编码)
    // Spring会自动解码PathVariable中的%23为#
    @GetMapping("/{playerTag}")
    public ResponseEntity<?> getPlayerDetails(@PathVariable String playerTag) {
        logger.info("Received request for player tag: {}", playerTag);

        // Service层已包含对playerTag格式的基本校验，这里可以直接调用
        // 但在Controller层做一些初步的、快速的校验也是好习惯
        if (playerTag == null || !playerTag.startsWith("#") || playerTag.length() < 2) {
            logger.warn("Invalid player tag format in request: {}", playerTag);
            return ResponseEntity.badRequest().body("Invalid player tag format. Tag must start with #.");
        }

        try {
            Player player = playerService.getPlayerInfo(playerTag);
            if (player != null) {
                return ResponseEntity.ok(player);
            } else {
                // Service层可能返回null表示未找到或处理过的错误
                logger.warn("Player data not found or error for tag: {}", playerTag);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player data not found for tag: " + playerTag);
            }
        } catch (Exception e) {
            // 捕获Service层可能未处理或重新抛出的其他通用异常
            logger.error("Internal server error while processing request for player tag {}: {}", playerTag, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}