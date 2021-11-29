package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class Produkt {

    public Produkt(Integer id, @NonNull String holzart, @NonNull String produktart) {
        this.id = id;
        this.holzart = Holzart.valueOf(holzart);
        this.produktart = produktart;
    }

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
