package me.hoon.restapipractice.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@EqualsAndHashCode(of = "id")
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value= EnumType.STRING)
    private Set<AccountRole> roles;
}
