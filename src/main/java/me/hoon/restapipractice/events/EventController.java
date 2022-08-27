package me.hoon.restapipractice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        eventValidator.eventCreateValidate(eventDto, errors);
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }


        Event event = modelMapper.map(eventDto, Event.class);
        event.checkFreeAndLocation();
        Event savedEvent = eventRepository.save(event);

        URI createURI = linkTo(EventController.class).slash(savedEvent.getId()).toUri();
        return ResponseEntity.created(createURI).body(event);
    }
}
