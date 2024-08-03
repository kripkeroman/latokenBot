package org.bot.initializer;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bot.config.BotConfig;
import org.bot.openAI.ChatGptService;
import org.bot.quizService.QuizController;
import org.bot.services.BotCommandInitializer;
import org.bot.services.SendMessages;
import org.bot.bdService.modelForUser.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.bot.constant.HelpText.HELP_TEXT;
import static org.bot.services.SendMessages.handleCallback;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRegistration userRegistration;
    private final QuizController quizController;

    final BotConfig config;
    private final ChatGptService gptService;
    private boolean inDialogWithBot = false;

    @Setter
    private boolean inQuiz = false;

    /**
     * Telegram bot constructor
     *
     * @param config
     */
    public TelegramBot(BotConfig config, ChatGptService gptService, QuizController quizController)
    {
        this.config = config;
        this.gptService = gptService;
        this.quizController = quizController;
        BotCommandInitializer.initializeServiceMenu(this);
    }

    /**
     * Pass the username to connect to the bot
     *
     * @return
     */
    @Override
    public String getBotUsername()
    {
        return config.getBotName();
    }

    /**
     * Pass the token to connect to the bot
     *
     * @return
     */
    @Override
    public String getBotToken()
    {
        return config.getBotToken();
    }

    /**
     * Update
     *
     * @param update
     */
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (inDialogWithBot) {
                if ("/exit".equals(messageText)) {
                    inDialogWithBot = false;
                    SendMessages.sendMessage(this, chatId, "Exiting bot dialog.");
                } else {
                    var gptGeneratedText = gptService.getResponseChatForUser(chatId, messageText);
                    SendMessages.sendMessage(this, chatId, gptGeneratedText);
                }
            } else if (inQuiz) {
                if ("/stop".equals(messageText)) {
                    inQuiz = false;
                    SendMessages.sendMessage(this, chatId, "Exiting quiz.");
                } else {
                    quizController.handleUpdate(update, this);
                }
            } else {
                switch (messageText) {
                    case "/bot":
                        inDialogWithBot = true;
                        var gptGeneratedText = gptService.getResponseChatForUser(chatId, messageText);
                        SendMessages.sendMessage(this, chatId, gptGeneratedText);
                        break;
                    case "/start":
                        userRegistration.registerUser(update.getMessage());
                        String name = update.getMessage().getChat().getFirstName();
                        String answer = "Hi " + name;
                        log.info("Replied to user " + name);
                        SendMessages.sendInlineKeyboardMessage(this, chatId, answer, "О Latoken", "О Hackathon", "О Culture", "Пройти тест");
                        break;
                    case "/help":
                        SendMessages.sendMessage(this, chatId, HELP_TEXT);
                        break;
                    case "/stop":
                        SendMessages.sendMessage(this, chatId, "No active process to stop.");
                        break;
                    default:
                        SendMessages.sendMessage(this, chatId, "Sorry, command not recognized");
                }
            }
        } else if (update.hasCallbackQuery()) {
            handleCallback(this, update.getCallbackQuery(), quizController);
        } else if (update.hasPollAnswer()) {
            quizController.handleUpdate(update, this);
        }
    }

}
