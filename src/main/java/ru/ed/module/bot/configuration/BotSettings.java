package ru.ed.module.bot.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
@ConfigurationProperties(prefix = "bot")
@Getter
@Setter
public class BotSettings {
    private String name;
    private String token;
    private Proxy proxy;

    @Getter
    @Setter
    public static class Proxy {
        private DefaultBotOptions.ProxyType type;
        private String host;
        private int port;
    }
}
