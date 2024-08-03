package org.bot.quizService.questionService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleChoiceQuestion extends Question
{
    private List<String> options;
    private List<Integer> correctAnswers;

    public MultipleChoiceQuestion(String questionText, List<String> options, List<Integer> correctAnswers, int points) {
        super(questionText, points);
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public boolean checkAnswer(String answer) {
        try {
            List<Integer> userAnswers = Arrays.stream(answer.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            return checkAnswer(userAnswers);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean checkAnswer(List<Integer> userAnswers) {
        return userAnswers.containsAll(correctAnswers) && correctAnswers.containsAll(userAnswers);
    }

    @Override
    public boolean checkAnswer(Integer answer) {
        return false;
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }
}
