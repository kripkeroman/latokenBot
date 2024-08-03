package org.bot.quizService;

import org.bot.bdService.modelForQuiz.QuizAnswer;
import org.bot.bdService.modelForQuiz.QuizAnswerRepository;
import org.bot.initializer.TelegramBot;
import org.bot.quizService.questionService.MultipleChoiceQuestion;
import org.bot.quizService.questionService.Question;
import org.bot.quizService.questionService.SingleChoiceQuestion;
import org.bot.quizService.questionService.TextQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bot.services.SendMessages.sendMessage;

@Component
public class QuizController
{
    private final QuizService quizService;
    private final QuizAnswerRepository quizAnswerRepository;
    private Map<Long, Map<Integer, String>> userAnswers = new HashMap<>();
    private Map<Long, Integer> currentQuestionIndex = new HashMap<>();

    @Autowired
    public QuizController(QuizService quizService, QuizAnswerRepository quizAnswerRepository) {
        this.quizService = quizService;
        this.quizAnswerRepository = quizAnswerRepository;
    }

    public void handleUpdate(Update update, TelegramBot bot)
    {
        Long chatId = null;
        String messageText = "";
        String firstName = "";
        String lastName = "";

        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            messageText = update.getMessage().getText();
            firstName = update.getMessage().getChat().getFirstName();
            lastName = update.getMessage().getChat().getLastName();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            messageText = update.getCallbackQuery().getData();
            firstName = update.getCallbackQuery().getFrom().getFirstName();
            lastName = update.getCallbackQuery().getFrom().getLastName();
        } else if (update.hasPollAnswer()) {
            chatId = update.getPollAnswer().getUser().getId();
            List<Integer> selectedOptionIds = update.getPollAnswer().getOptionIds();
            messageText = selectedOptionIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        } else {
            return;
        }

        if (!userAnswers.containsKey(chatId)) {
            userAnswers.put(chatId, new HashMap<>());
            currentQuestionIndex.put(chatId, 0);
        }

        int questionIndex = currentQuestionIndex.get(chatId);
        Map<Integer, String> answers = userAnswers.get(chatId);

        if (questionIndex > 0 && !messageText.isEmpty()) {
            answers.put(questionIndex - 1, messageText);
        }

        if (questionIndex < quizService.getQuestions().size()) {
            sendNextQuestion(chatId, questionIndex, bot);
            currentQuestionIndex.put(chatId, questionIndex + 1);
        } else {
            finishQuiz(chatId, answers, firstName, lastName, bot);
            userAnswers.remove(chatId);
            currentQuestionIndex.remove(chatId);
            bot.setInQuiz(false);
        }
    }

    private void sendNextQuestion(Long chatId, int questionIndex, AbsSender sender) {
        Question question = quizService.getQuestions().get(questionIndex);
        if (question instanceof TextQuestion) {
            sendMessage(sender, chatId, question.getQuestionText());
        } else {
            List<String> options;
            boolean allowsMultipleAnswers = false;
            if (question instanceof SingleChoiceQuestion) {
                SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
                options = singleChoiceQuestion.getOptions();
            } else if (question instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                options = multipleChoiceQuestion.getOptions();
                allowsMultipleAnswers = true;
            } else {
                return;
            }

            SendPoll poll = SendPoll.builder()
                    .chatId(chatId.toString())
                    .question(question.getQuestionText())
                    .options(options)
                    .isAnonymous(false)
                    .type("regular")
                    .allowMultipleAnswers(allowsMultipleAnswers)
                    .build();

            try {
                sender.execute(poll);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void finishQuiz(Long chatId, Map<Integer, String> answers, String firstName, String lastName, AbsSender sender)
    {
        int score = quizService.calculateScore(answers);

        QuizAnswer quizAnswer = quizAnswerRepository.findByChatId(chatId);
        if (quizAnswer == null)
        {
            quizAnswer = new QuizAnswer();
            quizAnswer.setChatId(chatId);
        }
        quizAnswer.setFirstName(firstName);
        quizAnswer.setLastName(lastName);
        quizAnswer.setScore(score);
        quizAnswer.setSalary(answers.get(4));
        quizAnswer.setReview(answers.get(10));
        quizAnswerRepository.save(quizAnswer);

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if (score == 22)
        {
            message.setText("Поздравляем! Вы набрали максимальное количество баллов: 22. Отличная работа!");
        }
        else
        {
            message.setText("Вы набрали " + score + " баллов из 22. Необходимо подучить еще материал.");
        }

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(AbsSender sender, Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            sender.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
