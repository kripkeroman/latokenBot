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
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (String buttonName : buttonNames)
        {
            rowInline.add(InlineKeyboardButton
                    .builder()
                    .text(buttonName)
                    .callbackData(buttonName)
                    .build());
        }

        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        rowsInline.add(rowInline);

        inlineKeyboardMarkup.setKeyboard(rowsInline);

        sendMessage(bot, chatId, messageText, inlineKeyboardMarkup);
    }

    public static void sendMessage(TelegramBot bot, long chatId, String messageText, InlineKeyboardMarkup inlineKeyboardMarkup)
    {
        SendMessage message = SendMessage
                .builder()
                .chatId(String.valueOf(chatId))
                .text(messageText)
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

        switch (callData) {
            case "О Latoken":
                sendMessage(bot, chatId, "Latoken - это ведущая криптобиржа, предлагающая платформу для торговли и обмена криптовалютами.");
                break;
            case "О Hackathon":
                sendMessage(bot, chatId, "Наш Hackathon - это уникальная возможность для разработчиков и энтузиастов показать свои навыки и выиграть призы.");
                break;
            case "О культуре":
                sendMessage(bot, chatId, "Культура Latoken основана на инновациях, командной работе и стремлении к высокому качеству.");
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
