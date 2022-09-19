package me.hoon.restapipractice.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@Import(RestDocsConfiguration.class)
@RunWith(SpringRunner.class)
//@WebMvcTest //웹 관련 빈만 등록해 준다. (슬라이스), Repository 관련 Bean 은 Mocking, stubbing 해야한다.
@SpringBootTest //Mocking 할 게 많으면 테스트하기 힘드므로 @SpringBootTest 를 사용해서 모든 빈을 등록시킨다.
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Ignore
public class DefaultControllerTest {

    /**
     * MockMvc 를 사용하면 mocking 이 되어있는 DispatcherServlet 을 상대로 가짜 요청, 응답을 확인하는 테스트가 가능하다.
     * 단위테스트라고는 볼 수 없다.
     */
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;
}
