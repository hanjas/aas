/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 *
 * @author USER
 */
public class ListWithDeleteBtn extends HBox{
    Label dateLabel = new Label();
    Label idLabel = new Label();
    Label iddLabel = new Label();
    Label categoryLabel = new Label();
    Label amountLabel = new Label();
    public Button button = new Button("Remove");

    public ListWithDeleteBtn(String date, String id,String idd, String category, String amount) {
        super();
        dateLabel.setText(date);
        idLabel.setText(id);
        iddLabel.setText(idd);
        categoryLabel.setText(category);
        amountLabel.setText(amount);    
        
        dateLabel.setStyle("-fx-border-width: 0 1 0 0;"
                + " -fx-border-color:black;"
                + " -fx-border-style: solid;");
        categoryLabel.setStyle("-fx-border-width: 0 1 0 0;"
                + " -fx-border-color:black;"
                + " -fx-border-style: solid;");
        amountLabel.setStyle("-fx-border-insets: 0 20 0 0;"
                + " -fx-padding: 0 0 0 10;"
                + " -fx-border-width: 0 1 0 0;"
                + " -fx-border-color:black;"
                + " -fx-border-style: solid;");
        
        categoryLabel.setPrefWidth(250);
        dateLabel.setPrefWidth(250);
        amountLabel.setPrefWidth(250);
        categoryLabel.setMaxWidth(250);
        dateLabel.setMaxWidth(250);
        amountLabel.setMaxWidth(250);
        
        idLabel.setVisible(false);
        iddLabel.setVisible(false);
//        HBox.setHgrow(categoryLabel, Priority.ALWAYS);
//        HBox.setHgrow(dateLabel, Priority.ALWAYS);
//        HBox.setHgrow(amountLabel, Priority.ALWAYS);
        this.getChildren().addAll(dateLabel,idLabel,categoryLabel,amountLabel,button);
//        setSpacing(5);
    }
    public String getIds() {
        return idLabel.getText();
    }
    public String getIdd() {
        return iddLabel.getText();
    }
    public String getDate() {
        return dateLabel.getText();
    }
}
