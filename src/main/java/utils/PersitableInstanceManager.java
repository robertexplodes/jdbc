package utils;

import domain.interfaces.Persitable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.*;

public class PersitableInstanceManager {
    private static PersitableInstanceManager instance = null;

    private PersitableInstanceManager() {
    }

    public static PersitableInstanceManager getInstance() {
        if (instance == null) {
            instance = new PersitableInstanceManager();
        }
        return instance;
    }

    public Optional<Persitable> getNewValueForInstance(Class<? extends Persitable> clazz, List<Class<?>> constructorArguments, Map<Node, Class<?>> inputFields) {
        try {
            var constructor = clazz.getConstructor(constructorArguments.toArray(new Class[0]));
            List<Object> values = new ArrayList<>();
            for (Node node : inputFields.keySet()) {
                addToInputs(inputFields, values, node);
            }
            var newValues = constructor.newInstance(values.toArray());
            return Optional.of(newValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void addToInputs(Map<Node, Class<?>> inputFields, List<Object> values, Node node) {
        if (node instanceof TextField t) {
            if (inputFields.get(node) == Integer.class) {
                values.add(Integer.parseInt((t).getText()));
            } else if (inputFields.get(node) == double.class) {
                values.add(Double.parseDouble((t).getText()));
            } else {
                values.add(((TextField) node).getText());
            }
        } else if (node instanceof ChoiceBox<?> c) {
            values.add((c).getValue());
        } else if (node instanceof Label l) {
            if (inputFields.get(node) == Integer.class) {
                values.add(Integer.parseInt((l).getText()));
            } else
                values.add((l).getText());
        }
    }
}
