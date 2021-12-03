package utils;

import domain.*;
import domain.interfaces.NotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
// TODO: make this class Single Responsibility
public class Utils {

    private static Utils instance;

    public static Utils getInstance(Connection connection) {
        if(instance == null)
            instance = new Utils(connection);
        return instance;
    }

    private final MitarbeiterRepository mitarbeiterRepository;
    private final KundenRepository kundenRepository;
    private final ProduktRepository produktRepository;

    private Utils(Connection connection) {
        mitarbeiterRepository = JdbcMitarbeiterRepository.getInstance(connection);
        kundenRepository = JdbcKundenRepository.getInstance(connection);
        produktRepository = JdbcProduktRepository.getInstance(connection);
    }

    public Node getNodeForType(Method getMethod, Object returnValue) {
        return switch (getMethod.getReturnType().getSimpleName()) {
            case "String", "Integer", "Double", "double" -> new TextField(returnValue.toString());
            case "Rolle", "Holzart" -> {
                var choiceBox = new ChoiceBox<>();
                if (returnValue instanceof Rolle r) {
                    choiceBox.getItems().addAll(Rolle.values());
                    choiceBox.getSelectionModel().select(r);
                } else if (returnValue instanceof Holzart h) {
                    choiceBox.getItems().addAll(Holzart.values());
                    choiceBox.getSelectionModel().select(h);
                }
                yield choiceBox;
            }
            default -> throw new IllegalArgumentException("Unsupported type" + getMethod.getReturnType().getSimpleName());
        };
    }

    @SneakyThrows
    public void openEditWindow(Class<? extends Persitable> clazz, Persitable persitable) {
        var fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Render.class))
                .toList();

        var pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setHgap(10);
        pane.setVgap(30);
        var constructorArguments = new ArrayList<Class<?>>();
        var inputFields = new LinkedHashMap<Node, Class<?>>();
        for (int i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            var methodName = "get" + toFirstLetterUppercase(field.getName());

            var getMethod = clazz.getMethod(methodName);
            var returnValue = getMethod.invoke(persitable);

            Node type = getNodeForType(getMethod, returnValue);
            if (field.isAnnotationPresent(NotEditable.class)) {
                type = new Label(returnValue.toString());
            }
            inputFields.put(type, getMethod.getReturnType());
            var labelText = toFirstLetterUppercase(field.getName());
            pane.addRow(i, new Label(labelText), type);
            constructorArguments.add(getMethod.getReturnType());
        }
        var stage = new Stage();

        Button save = new Button("Save");
        save.setOnAction(event -> {
            var newValues = getNewValueForInstance(clazz, constructorArguments, inputFields);
            if (newValues.isEmpty())
                return;
            var newInstance = newValues.get();
            updateInstance(newInstance);
            stage.close();
        });
        pane.addRow(fields.size(), save);
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @SneakyThrows
    private void updateInstance(Persitable newInstance) {
        if(newInstance instanceof Mitarbeiter m) {
            mitarbeiterRepository.update(m);
        } else if (newInstance instanceof Kunde k) {
            kundenRepository.update(k);
        } else if(newInstance instanceof Produkt p) {
            produktRepository.update(p);
        } else {
            System.out.println("didnt update");
        }
    }

    private Optional<? extends Persitable> getNewValueForInstance(Class<? extends Persitable> clazz, ArrayList<Class<?>> constructorArguments, LinkedHashMap<Node, Class<?>> inputFields) {
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

    private void addToInputs(LinkedHashMap<Node, Class<?>> inputFields, List<Object> values, Node node) {
        if (node instanceof TextField) {
            if (inputFields.get(node) == Integer.class) {
                values.add(Integer.parseInt(((TextField) node).getText()));
            } else if (inputFields.get(node) == double.class) {
                values.add(Double.parseDouble(((TextField) node).getText()));
            } else {
                values.add(((TextField) node).getText());
            }
        } else if (node instanceof ChoiceBox<?>) {
            values.add(((ChoiceBox<?>) node).getValue());
        } else if (node instanceof Label) {
            values.add(((Label) node).getText());
        }
    }

    private String toFirstLetterUppercase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
