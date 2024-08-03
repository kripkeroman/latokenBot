package org.bot.services;

import lombok.extern.slf4j.Slf4j;
import org.bot.initializer.TelegramBot;
import org.bot.quizService.QuizController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SendMessages
{
    public static void sendInlineKeyboardMessage(TelegramBot bot, long chatId, String messageText, String... buttonNames)
    {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(InlineKeyboardButton.builder().text(buttonNames[0]).callbackData(buttonNames[0]).build());
        firstRow.add(InlineKeyboardButton.builder().text(buttonNames[1]).callbackData(buttonNames[1]).build());
        firstRow.add(InlineKeyboardButton.builder().text(buttonNames[2]).callbackData(buttonNames[2]).build());
        rowsInline.add(firstRow);

        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(InlineKeyboardButton.builder().text(buttonNames[3]).callbackData(buttonNames[3]).build());
        rowsInline.add(secondRow);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        sendMessage(bot, chatId, messageText, inlineKeyboardMarkup);
    }

    public static void sendMessage(TelegramBot bot, long chatId, String messageText, InlineKeyboardMarkup inlineKeyboardMarkup)
    {
        SendMessage message = SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(messageText)
                .parseMode("Markdown")
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        try
        {
            bot.execute(message);
        }
        catch (TelegramApiException e)
        {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public static void sendMessage(TelegramBot bot, long chatId, String answer)
    {
        SendMessage message = SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(answer)
                .parseMode("Markdown")
                .build();
        try
        {
            bot.execute(message);
        }
        catch (TelegramApiException e)
        {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    public static void handleCallback(TelegramBot bot, CallbackQuery callbackQuery, QuizController quizController)
    {
        String callData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        switch (callData)
        {
            case "О Latoken":
                sendMessage(bot, chatId, "Для ознакомления перейдите по ссылке [Latoken](https://coda.io/@latoken/latoken-talent/latoken-161)");
                break;
            case "О Hackathon":
                sendMessage(bot, chatId, "Для ознакомления перейдите по ссылке [О Hackathon](https://coda.io/@latoken/latoken-talent/latoken-161)");
                break;
            case "О Culture":
                sendMessage(bot, chatId, "Для ознакомления перейдите по ссылке [О Culture](https://coda.io/@latoken/latoken-talent/culture-139)");
                break;
            case "Пройти тест":
                bot.setInQuiz(true);
                Update update = new Update();
                update.setCallbackQuery(callbackQuery);
                quizController.handleUpdate(update, bot);
                break;
            default:
                sendMessage(bot, chatId, "Sorry, command not recognized");
                break;
        }
    }
}
