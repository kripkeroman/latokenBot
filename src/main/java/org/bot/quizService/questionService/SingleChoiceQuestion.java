package org.bot.quizService.questionService;

import java.util.List;

public class SingleChoiceQuestion extends Question
{
    private List<String> options;
    private Integer correctAnswer;

    public SingleChoiceQuestion(String questionText, List<String> options, Integer correctAnswer, int points) {
        super(questionText, points);
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public boolean checkAnswer(String answer) {
        try {
            Integer answerIndex = Integer.parseInt(answer);
            return correctAnswer.equals(answerIndex);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean checkAnswer(Integer answer) {
        return correctAnswer.equals(answer);
    }

    public List<String> getOptions() {
        return options;
    }

    public Integer getCorrectAnswer() {
        return correctAnswer;
    }
}
