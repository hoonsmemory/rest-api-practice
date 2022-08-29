package me.hoon.restapipractice.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class EventValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return EventDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EventDto eventDto = (EventDto) target;

        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue.event.basePrice");
        }

        LocalDateTime beginEventDateTime = eventDto.getBeginEventDateTime();
        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        LocalDateTime beginEnrollmentDateTime = eventDto.getBeginEnrollmentDateTime();
        LocalDateTime closeEnrollmentDateTime = eventDto.getCloseEnrollmentDateTime();

        if(endEventDateTime.isBefore(beginEventDateTime) ||
                endEventDateTime.isBefore(closeEnrollmentDateTime) ||
                endEventDateTime.isBefore(beginEnrollmentDateTime)) {
            errors.rejectValue("endEventDateTime", "before.event.endEventDateTime");
        }

    }
}
