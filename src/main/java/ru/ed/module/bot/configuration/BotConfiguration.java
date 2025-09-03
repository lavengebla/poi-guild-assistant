package ru.ed.module.bot.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.ed.module.bot.event.TelegramEventPublisher;
import ru.ed.module.bot.service.TelegramBotImpl;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class BotConfiguration {

    private final BotSettings botSettings;

    @Bean
    @ConditionalOnProperty(name = "bot.proxy.enabled", havingValue = "false", matchIfMissing = true)
    public TelegramBotImpl defaultTelegramBot() {
        return new TelegramBotImpl(botSettings);
    }

    @Bean
    @ConditionalOnProperty(name = "bot.proxy.enabled", havingValue = "true")
    public TelegramBotImpl proxyTelegramBot() {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        defaultBotOptions.setProxyType(botSettings.getProxy().getType());
        defaultBotOptions.setProxyHost(botSettings.getProxy().getHost());
        defaultBotOptions.setProxyPort(botSettings.getProxy().getPort());
        return new TelegramBotImpl(defaultBotOptions, botSettings);
    }

}
