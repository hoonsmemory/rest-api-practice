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

    @Test
    public void freeTest() {
        //basePrice = 0, maxPrice = 0
        Event event1 = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();

        event1.checkFreeAndLocation();
        assertThat(event1.isFree()).isTrue();


        //basePrice = 100, maxPrice = 0
        Event event2 = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();

        event2.checkFreeAndLocation();
        assertThat(event2.isFree()).isFalse();


        //basePrice = 0, maxPrice = 1000
        Event event3 = Event.builder()
                .basePrice(0)
                .maxPrice(1000)
                .build();

        event3.checkFreeAndLocation();
        assertThat(event3.isFree()).isFalse();
    }

    @Test
    public void offlineTest() {
        Event event1 = Event.builder()
                .basePrice(0)
                .maxPrice(1000)
                .build();

        event1.checkFreeAndLocation();
        assertThat(event1.isOffline()).isTrue();

        Event event2 = Event.builder()
                .basePrice(0)
                .maxPrice(1000)
                .location("강남역 3번 출구")
                .build();

        event2.checkFreeAndLocation();
        assertThat(event2.isOffline()).isFalse();
    }
}