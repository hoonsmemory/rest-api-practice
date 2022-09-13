package me.hoon.restapipractice.configs;

import me.hoon.restapipractice.accounts.Account;
import me.hoon.restapipractice.accounts.AccountRole;
import me.hoon.restapipractice.accounts.AccountService;
import me.hoon.restapipractice.events.Event;
import me.hoon.restapipractice.events.EventDto;
import me.hoon.restapipractice.events.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
public class AppConfig {



    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Order(1)
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            EventRepository eventRepository;

            @Override
            public void run(ApplicationArguments args) throws Exception {


                //기동 시점에 Account 생성
                Account account = Account.builder()
                        .email("hoon@email.com")
                        .password("hoon")
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();

                accountService.saveAccount(account);

                //기동 시점에 event 하나 구현
                Event event = Event.builder()
                        .name("Event Bean")
                        .description("Event Bean create Test")
                        .beginEnrollmentDateTime(LocalDateTime.of(2022, 8, 26, 14, 00))
                        .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 1, 15, 00))
                        .beginEventDateTime(LocalDateTime.of(2022, 8, 26, 14, 00))
                        .endEventDateTime(LocalDateTime.of(2022, 9, 1, 15, 00))
                        .basePrice(100)
                        .maxPrice(200)
                        .limitOfEnrollment(100)
                        .location("REST API 오프라인 강의")
                        .build();

                eventRepository.save(event);

            }
        };
    }

}
