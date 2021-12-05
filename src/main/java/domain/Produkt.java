package domain;

import domain.interfaces.Persitable;
import javafx.beans.property.SimpleObjectProperty;
import lombok.*;

import java.util.Comparator;
import java.util.Objects;

@Getter
@ToString
@AllArgsConstructor
public class Produkt implements Persitable, Comparable<Produkt> {

    public Produkt(Integer id, @NonNull String holzart, @NonNull String produktart) {
        this(id, Holzart.valueOf(holzart), produktart);
    }


    public SimpleObjectProperty<String> holzartProperty() {
        var name = holzart.name();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return new SimpleObjectProperty<>(name);
    }

    @With
    private Integer id;

    @NonNull
    private Holzart holzart;

    @NonNull
    private String produktart;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produkt produkt = (Produkt) o;
        return Objects.equals(id, produkt.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(@NonNull Produkt o) {
        return Comparator.comparing(Produkt::getId)
                .compare(this, o);
    }
}
