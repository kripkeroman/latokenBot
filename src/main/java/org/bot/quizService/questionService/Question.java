package org.bot.quizService.questionService;

public abstract class Question
{
    private String questionText;
    private int points;

    public Question(String questionText, int points)
    {
        this.questionText = questionText;
        this.points = points;
    }

    public String getQuestionText()
    {
        return questionText;
    }

    public int getPoints()
    {
        return points;
    }

    public abstract boolean checkAnswer(String answer);
}
