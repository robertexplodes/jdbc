package domain;

import javafx.beans.property.SimpleObjectProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class Mitarbeiter implements Persitable {
    public Mitarbeiter(@NonNull String namenskuerzel, @NonNull String name, String rolle, double gehalt) {
        this.namenskuerzel = namenskuerzel;
        this.name = name;
        this.rolle = Rolle.valueOf(rolle);
        this.gehalt = gehalt;
    }

    public SimpleObjectProperty<String> rollenProperty() {
        String roleName = rolle.name();
        roleName = roleName.substring(0, 1).toUpperCase() + roleName.substring(1).toLowerCase();
        return new SimpleObjectProperty<>(roleName);
    }

    @NonNull
    private String namenskuerzel;
    @NonNull
    private String name;
    @NonNull
    private Rolle rolle;

    private double gehalt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mitarbeiter that = (Mitarbeiter) o;
        return Objects.equals(namenskuerzel, that.namenskuerzel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namenskuerzel);
    }
}
