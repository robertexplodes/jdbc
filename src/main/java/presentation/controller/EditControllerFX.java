package presentation.controller;

import domain.Holzart;
import domain.Rolle;
import domain.interfaces.RenderNotEditable;
import domain.interfaces.Persitable;
import domain.interfaces.Render;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import persistence.Repository;
import utils.InstanceManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public record EditControllerFX<T extends Persitable>(Repository<?, T> repository,
                                                     TableView<T> container) implements EditController<T> {


    @SneakyThrows
    @Override
    public void openEditWindow(T entity) {
        var clazz = entity.getClass();
        var fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Render.class) || f.isAnnotationPresent(RenderNotEditable.class))
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
            var returnValue = getMethod.invoke(entity);

            Node type;
            if (field.isAnnotationPresent(RenderNotEditable.class))
                type = new Label(returnValue.toString());
            else
                type = getNodeForType(getMethod, returnValue);
            inputFields.put(type, getMethod.getReturnType());
            var labelText = toFirstLetterUppercase(field.getName());
            pane.addRow(i, new Label(labelText), type);
            constructorArguments.add(getMethod.getReturnType());
        }
        var stage = new Stage();

        Button save = new Button("Save");
        save.setOnAction(event -> {
            var instanceManager = InstanceManager.getInstance();
            var newInstance = instanceManager.getNewValueForInstance(clazz, constructorArguments, inputFields);
            if (newInstance.isEmpty())
                return;

            var value = newInstance.get();
            var persitable = (T) value;
            updateEntity(persitable);
            stage.close();
        });
        pane.addRow(fields.size(), save);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(pane));
        stage.show();

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

    private String toFirstLetterUppercase(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @SneakyThrows
    private void updateEntity(T entity) {
        repository.update(entity);
        var observableList = FXCollections.observableList(repository.findAll()).sorted();
        container.setItems(observableList);
        container.refresh();
    }
}
