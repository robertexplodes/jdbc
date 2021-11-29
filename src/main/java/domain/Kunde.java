package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Kunde {
    private Integer id;
    private String name;
    private String email;
}
