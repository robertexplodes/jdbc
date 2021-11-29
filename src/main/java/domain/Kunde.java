package domain;

import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@Getter
public class Kunde {
    private Integer id;
    @NonNull
    private String name;
    @NonNull
    private String email;

    public Kunde(Integer id, @NonNull String name, @NonNull String email) {
        this.id = id;
        this.name = name;
        var emailRegex = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
        if(!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email is not valid");
        }
        this.email = email;
    }

    public Kunde(String name, String email) {
        this(null, name, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kunde kunde = (Kunde) o;
        return Objects.equals(id, kunde.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
