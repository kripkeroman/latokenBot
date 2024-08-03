package org.bot.quizService.questionService;

public class TextQuestion extends Question
{
    private int minLength;

    public TextQuestion(String questionText, int minLength, int points)
    {
        super(questionText, points);
        this.minLength = minLength;
    }

    @Override
    public boolean checkAnswer(String answer)
    {
        return answer.length() >= minLength;
    }

    /**
     * @param answer
     * @return
     */
    @Override
    public boolean checkAnswer(Integer answer) {
        return false;
    }
}
