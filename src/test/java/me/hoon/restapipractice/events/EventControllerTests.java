package me.hoon.restapipractice.events;

import me.hoon.restapipractice.accounts.Account;
import me.hoon.restapipractice.accounts.AccountRole;
import me.hoon.restapipractice.accounts.AccountService;
import me.hoon.restapipractice.common.DefaultControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends DefaultControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    //정상 저장
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
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                //적용한 스니펫 : links
                .andDo(document("create-event",
                        links(
                                //링크 정보 문서 추가
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query-events"),
                                linkWithRel("update-event").description("link to update-event")
                        ),
                        requestHeaders(
                                //요청헤더 문서 추가
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
                        ),
                        requestFields(
                                //요청필드 문서 추가
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                        ),
                        responseHeaders(
                                //응답헤더 문서 추가
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
                        ),
                        relaxedResponseFields(//문서의 일부분만 확인해도 되게끔 설정해주는 prefix
                                //응답필드 문서 추가
                                fieldWithPath("id").description("id of new event"),
                                fieldWithPath("name").description("name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("endEventDateTime of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                                fieldWithPath("free").description("free of new event"),
                                fieldWithPath("offline").description("offline of new event"),
                                fieldWithPath("eventStatus").description("eventStatus of new event"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to self"),
                                fieldWithPath("_links.update-event.href").description("link to self")
                        )
                ))
        ;
    }

    private String getBearerToken() throws Exception {
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


        ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(clientId, clientPwd))
                .param("username", username)
                .param("password", password)
                .param("grant_type", "password"));

        String resultString = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser jackson2JsonParser = new Jackson2JsonParser();
        return jackson2JsonParser.parseMap(resultString).get("access_token").toString();
    }

    //입력값 외에 데이터가 들어오면 에러 발생
    //spring.jackson.deserialization.fail-on-unknown-properties=true
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

    //입력값 없이 저장 시 에러 발생
    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //Validation 테스트( basePrice 가 maxPrice 보다 클 경우 에러 발생)
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
                .andExpect(jsonPath("content[0].field").exists())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].code").exists())
                //.andExpect(jsonPath("$[0].defaultMessage").exists())
                //.andExpect(jsonPath("$[0].rejectValue").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    //30개의 이벤트를 10개씩 두번째 페이지 조회하기
    @Test
    public void queryEvents() throws Exception {
        IntStream.range(0, 30).forEach( i -> createEvents(i));

        mockMvc.perform(get("/api/events")
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;
    }

    //30개의 이벤트를 10개씩 두번째 페이지 조회하기
    @Test
    public void queryEventsWithAuthentication() throws Exception {
        IntStream.range(0, 30).forEach( i -> createEvents(i));

        mockMvc.perform(get("/api/events")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .param("page", "1")
                        .param("size", "10")
                        .param("sort", "name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.create-event").exists())
        ;
    }

    //event 단건 조회
    @Test
    public void getEvent() throws Exception {
        Integer id = 1;
        createEvents(id);
        mockMvc.perform(get("/api/events/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("name").value("event" + id))
        ;
    }

    private void createEvents(int index) {
        EventDto eventDto = EventDto.builder()
                .name("event" + index)
                .description("test event" + index)
                .build();

        Event event = modelMapper.map(eventDto, Event.class);
        eventRepository.save(event);
    }

    //Event 수정
    @Test
    public void editEvent() throws Exception {
        createEvents(1);

        EventDto eventDto = EventDto.builder()
                .name("changeName")
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


        mockMvc.perform(put("/api/events/{id}",1)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("changeName"));

    }



}
