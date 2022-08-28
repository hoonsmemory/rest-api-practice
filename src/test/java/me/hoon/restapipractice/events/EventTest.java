package me.hoon.restapipractice.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Event builder test")
                .description("The Lombok is working")
                .build();

        assertThat(event).isNotNull();
    }

    private Object[] paramsForTest() {
        return new Object[] {
                new Object[] {0 ,0 , true},
                new Object[] {100, 0, false},
                new Object[] {0, 1000, false},
        };
    }

    @Test
    @Parameters(method = "paramsForTest")
    public void freeTest(int baePrice, int maxPrice, boolean free) {
        //basePrice = 0, maxPrice = 0
        Event event1 = Event.builder()
                .basePrice(baePrice)
                .maxPrice(maxPrice)
                .build();

        event1.checkFreeAndLocation();
        assertThat(event1.isFree()).isEqualTo(free);
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