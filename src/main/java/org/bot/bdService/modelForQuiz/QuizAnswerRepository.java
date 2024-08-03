package org.bot.bdService.modelForQuiz;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAnswerRepository extends JpaRepository<QuizAnswer, Long>
{
    QuizAnswer findByChatId(Long chatId);
}
