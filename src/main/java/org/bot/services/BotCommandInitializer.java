package org.bot.services;

import lombok.extern.slf4j.Slf4j;
import org.bot.initializer.TelegramBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BotCommandInitializer
{
    /**
     *Method for initialize service menu
     *
     * @param bot
     */
    public static void initializeServiceMenu(TelegramBot bot)
    {
        List<BotCommand> listCommands = new ArrayList<>();
        listCommands.add(new BotCommand("/run", "Start bot"));
        listCommands.add(new BotCommand("/bot", "Start GPT bot"));
        listCommands.add(new BotCommand("/help", "Info about bot"));
        listCommands.add(new BotCommand("/stop", "Stop bot"));
        try
        {
            bot.execute(new SetMyCommands(listCommands, new BotCommandScopeDefault(),null));
        }
        catch (TelegramApiException e)
        {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
}
