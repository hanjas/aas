/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import backend.DBAccess;
import controller.edit.EditFishController;
import controller.edit.EditGroupController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import model.alert.SavedPopup;

/**
 *
 * @author USER
 */
public class ListWithEditBtn extends HBox{
    
    Label nameLabel = new Label();
    Button editBtn = new Button("edit");
    String oldName="", newName="";

    public ListWithEditBtn(String name, String fishOrGroupe, Object controller, ListView superListView) {
        nameLabel.setText(name);
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        getChildren().addAll(nameLabel, editBtn);
        editBtn.setOnAction(e->{
            showWindow(name, fishOrGroupe, controller, superListView);
        });
    }
    
    public void setGroupAction(String oldName, String newName) {
        try {
            DBAccess db = new DBAccess();
            db.editGroup(oldName, newName);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(ListWithEditBtn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setFishAction(String oldName, String newName) {
        try {
            DBAccess db = new DBAccess();
            db.editFish(oldName, newName);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(ListWithEditBtn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void showWindow(String name, String type, Object controller, ListView superListView) {
        Stage stage = new Stage();
        Stage superStage = (Stage) superListView.getScene().getWindow();
        
        BorderPane mainPane = new BorderPane();
        BorderPane footerPane = new BorderPane();
        GridPane centerGrid = new GridPane();
        TextField nameField = new TextField();
        nameField.setText(name);
        Label nameLabel = new Label("Name");
        Button okBtn = new Button("Ok");
        
        mainPane.setPadding(new Insets(20));
        footerPane.setRight(okBtn);
        centerGrid.add(nameLabel, 0, 0);
        centerGrid.add(nameField, 1, 0);
        centerGrid.setHgap(10);
        centerGrid.setAlignment(Pos.CENTER);
        mainPane.setCenter(centerGrid);
        mainPane.setBottom(footerPane);
        Scene scene = new Scene(mainPane,300,150);
        stage.setScene(scene);
        stage.initOwner(superStage);
        stage.show();
        nameField.setOnAction(e->{
            okBtn.requestFocus();
        });
        okBtn.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER) {
                if(!nameField.getText().equals("") && !nameField.getText().toLowerCase().equals(name)){
                    if(type.equals("groupe")) {
                        EditGroupController groupController = (EditGroupController)controller;
                        setGroupAction(name, nameField.getText().toLowerCase());
                        groupController.setListItems();
                    } else if(type.equals("fish")) {
                        EditFishController fishController = (EditFishController) controller;
                        setFishAction(name, nameField.getText().toLowerCase());
                        fishController.setListItems();
                    }
                    showSavedPopup(stage);
                }
            }
        });
        okBtn.setOnAction(e->{
            if(!nameField.getText().equals("") && !nameField.getText().toLowerCase().equals(name)){
                if(type.equals("groupe")) {
                    EditGroupController groupController = (EditGroupController)controller;
                    setGroupAction(name, nameField.getText().toLowerCase());
                    groupController.setListItems();
                } else if(type.equals("fish")) {
                    EditFishController fishController = (EditFishController) controller;
                    setFishAction(name, nameField.getText().toLowerCase());
                    fishController.setListItems();
                }
                showSavedPopup(stage);
            }
        });
    }
    public void showSavedPopup(Stage stage) {
        SavedPopup popup = new SavedPopup();
        popup.initOwner(stage);
        popup.showAndWait();
        stage.close();
    }
}
