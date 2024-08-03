package org.bot.quizService.questionService;

import java.util.List;

public class SingleChoiceQuestion extends Question
{
    private List<String> options;
    private String correctAnswer;

    public SingleChoiceQuestion(String questionText, List<String> options, String correctAnswer, int points)
    {
        super(questionText, points);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer)
    {
        return correctAnswer.equalsIgnoreCase(answer);
    }

    public List<String> getOptions()
    {
        return options;
    }
}
