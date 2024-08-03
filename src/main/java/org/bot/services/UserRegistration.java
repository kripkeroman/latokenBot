package org.bot.services;

import lombok.extern.slf4j.Slf4j;
import org.bot.bdService.modelForUser.User;
import org.bot.bdService.modelForUser.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

@Slf4j
@Service
public class UserRegistration
{
    @Autowired
    private UserRepository userRepository;

    /**
     * Registration user in database
     *
     * @param msg
     */
    public void registerUser(Message msg)
    {
        if(userRepository.findById(msg.getChatId()).isEmpty())
        {
            var chatId = msg.getChatId();
            var chat = msg.getChat();

            User user = new User();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(user);
            log.info("User saved: " + user);
        }
    }
}
