package me.hoon.restapipractice.configs;

import me.hoon.restapipractice.accounts.Account;
import me.hoon.restapipractice.accounts.AccountRole;
import me.hoon.restapipractice.accounts.AccountService;
import me.hoon.restapipractice.common.DefaultControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthServerConfigTest extends DefaultControllerTest {

    @Autowired
    AccountService accountService;

    //인증 토큰을 발급 받는 테스트
    @Test
    public void getAuthToken() throws Exception {

        String username = "hoon1@email.com";
        String password = "hoon";

        String clientId = "myApp";
        String clientPwd ="pass";

        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        accountService.saveAccount(account);


        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId, clientPwd))
                        .param("username", username)
                        .param("password", password)
                        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}