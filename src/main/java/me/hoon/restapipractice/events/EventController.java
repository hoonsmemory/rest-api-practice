package me.hoon.restapipractice.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hoon.restapipractice.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

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
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }


        Event event = modelMapper.map(eventDto, Event.class);
        event.checkFreeAndLocation();
        Event savedEvent = eventRepository.save(event);

        ControllerLinkBuilder controllerLinkBuilder = linkTo(EventController.class).slash(savedEvent.getId());
        URI createURI = controllerLinkBuilder.toUri();

        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(controllerLinkBuilder.withRel("update-event"));
        return ResponseEntity.created(createURI).body(eventResource);
    }

    /*
        PagedResourcesAssembler
        Repository 받아온 page 를 Resource 로 변환해준다.
        페이지에 관련된 링크 정(첫 페이지, 마지막 페이지, 현재 페이지 등)
     */
    @GetMapping
    public ResponseEntity getEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler) {
        Page<Event> events = eventRepository.findAll(pageable);
        PagedResources<Resource<Event>> pagedResources = assembler.toResource(events, e ->new EventResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable("id") Integer id) {
        Optional<Event> event = eventRepository.findById(id);

        if(event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new EventResource(event.get()));
    }

    @PutMapping("/{id}")
    public ResponseEntity editEvent(@PathVariable("id") Integer id,
                                    @RequestBody @Valid EventDto eventDto,
                                    Errors errors) {

        //id 에 해당하는 값이 있는지 체크
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if(optionalEvent.isEmpty()) {
            errors.reject("null.event.object", null);
        }

        //DTO Validation 체크
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        //Event Validation 체크
        eventValidator.validate(eventDto, errors);
        if(errors.hasErrors()) {
            return badRequest(errors);
        }

        //eventDto to event
        Event event = optionalEvent.get();
        modelMapper.map(eventDto, event);

        //save
        Event savedEvent = eventRepository.save(event);

        //return
        EventResource eventResource = new EventResource(savedEvent);
        return ResponseEntity.ok(eventResource);
    }

    private static ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
