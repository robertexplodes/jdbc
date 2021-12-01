package domain;

import domain.interfaces.NotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.beans.property.SimpleObjectProperty;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
public class Produkt implements Persitable {

    public Produkt(Integer id, @NonNull String holzart, @NonNull String produktart) {
        this.id = id;
        this.holzart = Holzart.valueOf(holzart);
        this.produktart = produktart;
    }

    public SimpleObjectProperty<String> holzartProperty() {
        var name = holzart.name();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        return new SimpleObjectProperty<>(name);
    }

    @With
    @NotEditable
    @Render
    private Integer id;

    @NonNull
    @Render
    private Holzart holzart;

    @NonNull
    @Render
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
}
