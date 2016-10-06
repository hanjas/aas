/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aas.fish.merchant;

import java.util.stream.IntStream;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author USER
 */
public class Checker {
    public void start() {

        ObservableList<String> data = FXCollections.observableArrayList();
        IntStream.range(0, 1000).mapToObj(Integer::toString).forEach(data::add);

        FilteredList<String> filteredData = new FilteredList<>(data, s -> true);

        TextField filterInput = new TextField();
        filterInput.textProperty().addListener(obs->{
            String filter = filterInput.getText(); 
            if(filter == null || filter.length() == 0) {
                filteredData.setPredicate(s -> true);
            }
            else {
                filteredData.setPredicate(s -> s.contains(filter));
            }
        });

        
        BorderPane content = new BorderPane(new ListView<>(filteredData));
        content.setBottom(filterInput);
        Stage primaryStage = new Stage();
        Scene scene = new Scene(content, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
