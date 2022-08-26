package me.hoon.restapipractice.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * > @EqualsAndHashCode(of = "id")
 *   Equals, HashCode 를 이용할 때 모든 field 를 사용한다.
 *   엔티티간에 연관관계가 있을 때 연관관계가 상호 참조를 하게되면 Equals, HashCode 코드 안에서 stack overflow 가 생길 우려가 있다.
 * > @Data 를 적용할 경우 @EqualsAndHashCode 도 같이 적용되며,
 *   위와 같이 상호 참조가 일어나 stack overflow 가 생일 우려가 있으므로 엔티티에 적용하는건 좋지 않다.
 */
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice;   // (optional)
    private int maxPrice;    // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;

    //ORDINAL 을 사용할 경우 enum 의 순서가 바뀔 시 값도 변경될 수 있으므로 STRING 으로 사용하는게 좋다.
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;
}
