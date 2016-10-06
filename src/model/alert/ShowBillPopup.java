/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.alert;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author BCz
 */
public class ShowBillPopup extends BorderPane {

    public ShowBillPopup(Node node) {
        printBtn = new Button("Print");
        okBtn = new Button("Ok");
        cancelBtn = new Button("Cancel");
        BorderPane footer = new BorderPane();
        GridPane btnGrid = new GridPane();
        VBox vbox = new VBox(printBtn);
        vbox.setPadding(new Insets(0, 0, 0, 0));
        
//        btnGrid.add(printBtn, 0, 0);
        btnGrid.add(okBtn, 1, 0);
        btnGrid.add(cancelBtn, 2, 0);
        
        btnGrid.setAlignment(Pos.CENTER);
        btnGrid.setHgap(10);
        footer.setRight(btnGrid);
        footer.setLeft(vbox);
        setCenter(node);
        setBottom(footer);
        setPadding(new Insets(10));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                okBtn.requestFocus();
            }
        });
    }
    public Button printBtn, okBtn, cancelBtn;
}
