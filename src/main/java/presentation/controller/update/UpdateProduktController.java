package presentation.controller.update;

import domain.Produkt;

import java.util.Optional;
import java.util.function.Consumer;

public class UpdateProduktController implements UpdateController<Produkt>{
    @Override
    public Optional<Produkt> getValue() {
        return Optional.empty();
    }

    @Override
    public void setEntity(Produkt entity) {

    }

    @Override
    public void setOnSave(Consumer<Produkt> onSave) {

    }
}
