package org.bot.quizService.questionService;

import java.util.Arrays;
import java.util.List;

public class MultipleChoiceQuestion extends Question
{
    private List<String> options;
    private List<String> correctAnswers;

    public MultipleChoiceQuestion(String questionText, List<String> options, List<String> correctAnswers, int points)
    {
        super(questionText, points);
        this.options = options;
        this.correctAnswers = correctAnswers;
    }

    @Override
    public boolean checkAnswer(String answer)
    {
        String[] answers = answer.split(",");
        return Arrays.asList(answers).containsAll(correctAnswers);
    }

    public List<String> getOptions()
    {
        return options;
    }

    public List<String> getCorrectAnswers()
    {
        return correctAnswers;
    }
}
