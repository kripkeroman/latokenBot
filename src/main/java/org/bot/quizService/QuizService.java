package org.bot.quizService;

import org.bot.quizService.questionService.MultipleChoiceQuestion;
import org.bot.quizService.questionService.Question;
import org.bot.quizService.questionService.SingleChoiceQuestion;
import org.bot.quizService.questionService.TextQuestion;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class QuizService
{
    private List<Question> questions;

    public QuizService() {
        this.questions = new ArrayList<>();
        loadQuestions();
    }

    private void loadQuestions() {
        // Вопрос 1
        questions.add(new MultipleChoiceQuestion(
                "Какие из этих материалов вы прочитали?",
                Arrays.asList(
                        "О Хакатоне deliver.latoken.com/hackathon",
                        "О Latoken deliver.latoken.com/about",
                        "Большая часть из #nackedmanagement coda.io/@latoken/latoken-talent/nakedmanagement-88 траллалалсрра"
                ),
                Arrays.asList(
                        0,
                        1,
                        2
                ),
                3
        ));

        // Вопрос 2
        questions.add(new SingleChoiceQuestion(
                "Какой призовой фонд на Хакатоне?",
                Arrays.asList("25,000 Опционов", "100,000 Опционов или 10,000 LA", "Только бесценный опыт"),
                1,
                1
        ));

        // Вопрос 3
        questions.add(new SingleChoiceQuestion(
                "Что от вас ожидают на хакатоне в первую очередь?",
                Arrays.asList(
                        "Показать мои способности узнавать новые технологии",
                        "Показать работающий сервис",
                        "Продемонстрировать навыки коммуникации и командной работы"
                ),
                0,
                1
        ));

        // Вопрос 4
        questions.add(new MultipleChoiceQuestion(
                "Что из этого является преимуществом работы в Latoken?",
                Arrays.asList(
                        "Быстрый рост через решение нетривиальных задач",
                        "Передовые технологии AIxWEB3",
                        "Глобальный рынок, клиенты в 200+ странах",
                        "Возможность совмещать с другой работой и хобби",
                        "Самая успешная компания из СНГ в WEB3",
                        "Удаленная работа, но без давншифтинга",
                        "Оплата в твердой валюте, без привязки к банкам",
                        "Опционы с 'откешиванием' криптолетом",
                        "Комфортная среда для свободы творчества"
                ),
                Arrays.asList(
                        0,
                        1,
                        2,
                        4,
                        5,
                        6,
                        7
                ),
                7
        ));

        // Вопрос 5
        questions.add(new TextQuestion(
                "Каковы Ваши зарплатные ожидания в USD?",
                10, // Нет правильного ответа, оценка не производится
                0
        ));

        // Вопрос 6
        questions.add(new SingleChoiceQuestion(
                "Какое расписание Хакатона корректнее?",
                Arrays.asList(
                        "Пятница: 18:00 Разбор задач. Суббота: 18:00 Демо результатов, 19-00",
                        "Суббота: 12:00 Презентация компании, 18:00 Презентация результатов проектов"
                ),
                0,
                1
        ));

        // Вопрос 7
        questions.add(new MultipleChoiceQuestion(
                "Каковы признаки 'Wartime CEO' согласно крупнейшему венчурному фонду a16z?",
                Arrays.asList(
                        "Сосредотачивается на общей картине и дает сотрудникам принимать детальные решения ",
                        "На общей картине дает команде возможность принимать детальные решения",
                        "Употребляет ненормативную лексику, кричит, редко говорит спокойным тоном",
                        "Терпит отклонения от плана, если они связаны с усилиями и творчеством",
                        "Не терпит отклонений от плана",
                        "Обучает своих сотрудников для обеспечения их удовлетворенности и карьерного развития",
                        "Тренерует сотрудников, так чтобы им не прострелили зад на поле боя"
                ),
                Arrays.asList(
                        2,
                        4,
                        6
                ),
                3
        ));

        // Вопрос 8
        questions.add(new MultipleChoiceQuestion(
                "Что Latoken ждет от каждого члена команды?",
                Arrays.asList(
                        "Спокойной работы без излишнего стресса",
                        "Вникания в блокеры вне основного стека, чтобы довести свою задачу до прода",
                        "Тестирование продукта",
                        "Субординацию, и не вмешательство в чужие дела",
                        "Вежливость и корректность в коммуникации",
                        "Измерение результатов",
                        "Демонстрацию результатов в проде каждую неделю"
                ),
                Arrays.asList(
                        1,
                        2,
                        5,
                        6
                ),
                4
        ));

        // Вопрос 9
        questions.add(new SingleChoiceQuestion(
                "Представьте вы на выпускном экзамене. Ваш сосед слева просит вас передать ответы от соседа справа. Вы поможете?",
                Arrays.asList(
                        "Да",
                        "Да, но если преподаватель точно не увидит",
                        "Да, но только если мне тоже помогут",
                        "Нет",
                        "Нет, если мне не дадут посмотреть эти ответы",
                        "Нет, если это может мне повредить"
                ),
                3,
                1
        ));

        // Вопрос 10
        questions.add(new SingleChoiceQuestion(
                "Кирпич весит килограмм и еще пол-кирпича. Сколько весит кирпич?",
                Arrays.asList("1 кг", "1.5 кг", "2 кг", "3 кг"),
                1,
                1
        ));

        // Вопрос 11
        questions.add(new TextQuestion(
                "Напишите ваши 'за' и 'против' работы в Latoken? Чем подробнее, тем лучше - мы читаем.",
                200,
                0
        ));
    }

    public int calculateScore(Map<Integer, String> userAnswers) {
        int score = 0;
        for (Map.Entry<Integer, String> entry : userAnswers.entrySet()) {
            int questionIndex = entry.getKey();
            String userAnswer = entry.getValue();
            Question question = questions.get(questionIndex);

            if (question instanceof SingleChoiceQuestion) {
                SingleChoiceQuestion singleChoiceQuestion = (SingleChoiceQuestion) question;
                if (singleChoiceQuestion.checkAnswer(userAnswer)) {
                    score += singleChoiceQuestion.getPoints();
                }
            } else if (question instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                if (multipleChoiceQuestion.checkAnswer(userAnswer)) {
                    score += multipleChoiceQuestion.getPoints();
                }
            }
        }
        return score;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
