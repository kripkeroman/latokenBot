package org.bot.quizService;

import org.bot.bdService.modelForQuiz.QuizAnswer;
import org.bot.bdService.modelForQuiz.QuizAnswerRepository;
import org.bot.initializer.TelegramBot;
import org.bot.quizService.questionService.Question;
import org.bot.quizService.questionService.SingleChoiceQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.HashMap;
import java.util.Map;

@Component
public class QuizController
{
    private final QuizService quizService;
    private final QuizAnswerRepository quizAnswerRepository;
    private Map<Long, Map<Integer, String>> userAnswers = new HashMap<>();
    private Map<Long, Integer> currentQuestionIndex = new HashMap<>();

    @Autowired
    public QuizController(QuizService quizService, QuizAnswerRepository quizAnswerRepository)
    {
        this.quizService = quizService;
        this.quizAnswerRepository = quizAnswerRepository;
    }

    public void handleUpdate(Update update, TelegramBot bot)
    {
        Long chatId;
        String messageText;
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
        } else {
            // Если нет ни текста, ни колбэка, выходим
            return;
        }

        if (!userAnswers.containsKey(chatId)) {
            userAnswers.put(chatId, new HashMap<>());
            currentQuestionIndex.put(chatId, 0);
        }

        int questionIndex = currentQuestionIndex.get(chatId);
        Map<Integer, String> answers = userAnswers.get(chatId);

        // Сохраняем ответ пользователя на предыдущий вопрос
        if (questionIndex > 0) {
            answers.put(questionIndex - 1, messageText);
        }

        // Отправляем следующий вопрос или завершаем тест
        if (questionIndex < quizService.getQuestions().size()) {
            sendNextQuestion(chatId, questionIndex, bot);
            currentQuestionIndex.put(chatId, questionIndex + 1);
        } else {
            finishQuiz(chatId, answers, firstName, lastName, bot);
            userAnswers.remove(chatId);
            currentQuestionIndex.remove(chatId);
            bot.setInQuiz(false); // Завершаем викторину
        }
    }

    private void sendNextQuestion(Long chatId, int questionIndex, AbsSender sender)
    {
        Question question = quizService.getQuestions().get(questionIndex);
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(formatQuestion(question));

        try
        {
            sender.execute(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

        try
        {
            sender.execute(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String formatQuestion(Question question)
    {
        StringBuilder formattedQuestion = new StringBuilder(question.getQuestionText());
        int optionNumber = 1;

        if (question instanceof SingleChoiceQuestion)
        {
            SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
            for (String option : singleChoiceQuestion.getOptions())
            {
                formattedQuestion.append("\n").append(optionNumber).append(". ").append(option);
                optionNumber++;
            }
        }

        return formattedQuestion.toString();
    }
}
