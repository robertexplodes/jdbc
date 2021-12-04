package domain;

import domain.interfaces.RenderNotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.beans.property.SimpleObjectProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Comparator;
import java.util.Objects;

@AllArgsConstructor
@ToString
@Getter
public class Mitarbeiter implements Persitable, Comparable<Mitarbeiter> {
    @NonNull
    @RenderNotEditable
    private String namenskuerzel;

    @NonNull
    @Render
    private String name;

    @NonNull
    @Render
    private Rolle rolle;

    @Render
    private double gehalt;

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

    @Override
    public int compareTo(@NonNull Mitarbeiter o) {
        return Comparator.comparing(Mitarbeiter::getNamenskuerzel)
                .compare(this, o);
    }
}
