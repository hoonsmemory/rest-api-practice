package me.hoon.restapipractice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest //웹 관련 빈만 등록해 준다. (슬라이스), Repository 관련 Bean 은 Mocking, stubbing 해야한다.
@SpringBootTest //Mocking 할 게 많으면 테스트하기 힘드므로 @SpringBootTest 를 사용해서 모든 빈을 등록시킨다.
@AutoConfigureMockMvc
public class EventControllerTests {

    /**
     * MockMvc 를 사용하면 mocking 이 되어있는 DispatcherServlet 을 상대로 가짜 요청, 응답을 확인하는 테스트가 가능하다.
     * 단위테스트라고는 볼 수 없다.
     */
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    public void createEvent() throws Exception {
        EventDto eventDto = EventDto.builder()
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

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE));
    }

    @Test
    public void createEvent_bad_request() throws Exception {
        Event event = Event.builder()
                .id(100)
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
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISH)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Event Bean")
                .description("Event Bean create Test")
                .beginEnrollmentDateTime(LocalDateTime.of(2022, 8, 26, 14, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2022, 9, 1, 15, 00))
                .beginEventDateTime(LocalDateTime.of(2022, 8, 26, 14, 00))
                .endEventDateTime(LocalDateTime.of(2022, 9, 1, 15, 00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("REST API 오프라인 강의")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].code").exists())
                //.andExpect(jsonPath("$[0].defaultMessage").exists())
                //.andExpect(jsonPath("$[0].rejectValue").exists())
        ;
    }
}
