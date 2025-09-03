package ru.ed.module.bot.command;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command {

    void execute(Message message);

    boolean requiresAdminRights();

}
