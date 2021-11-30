package domain;

import javafx.beans.property.SimpleObjectProperty;
import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Getter
@ToString
public class Produkt implements Persitable{

    public Produkt(Integer id, @NonNull String holzart, @NonNull String produktart) {
        this.id = id;
        this.holzart = Holzart.valueOf(holzart);
        this.produktart = produktart;
    }

    public SimpleObjectProperty<String> getHolzartProperty() {
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
}
