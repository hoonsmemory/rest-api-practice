package me.hoon.restapipractice.events;

import me.hoon.restapipractice.common.DefaultControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends DefaultControllerTest {

    @Autowired
    EventRepository eventRepository;

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
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-event").exists())
                .andDo(print())
                .andDo(document("create-event"));
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
