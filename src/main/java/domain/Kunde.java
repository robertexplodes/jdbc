package domain;

import domain.interfaces.Persitable;
import lombok.Getter;
import lombok.NonNull;
import lombok.With;

import java.util.Comparator;
import java.util.Objects;

@Getter
public class Kunde implements Persitable, Comparable<Kunde> {
    @With
    private Integer id;
    @NonNull
    private final String name;
    @NonNull
    private final String email;

    public Kunde(Integer id, @NonNull String name, @NonNull String email) {
        this.id = id;
        this.name = name;
        var emailRegex = "^(.+)@(.+)$";
        if(!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email is not valid " + email);
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

    @Override
    public int compareTo(@NonNull Kunde o) {
        return Comparator.comparing(Kunde::getId)
                .compare(this, o);
    }
}
