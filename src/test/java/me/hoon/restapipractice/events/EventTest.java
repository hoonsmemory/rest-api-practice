package me.hoon.restapipractice.events;


import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Event builder test")
                .description("The Lombok is working")
                .build();

        assertThat(event).isNotNull();
    }
}