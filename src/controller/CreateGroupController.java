/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import com.sun.glass.events.KeyEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.ErrorMessage;
import model.alert.InfomationPopupWithMsg;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class CreateGroupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            db = new DBAccess();
            robot = new Robot();
            closeDBonClose();
        } catch (Exception ex) {
            Logger.getLogger(CreateGroupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void showCreatedMsg() {
        InfomationPopupWithMsg saved = new InfomationPopupWithMsg("Created", nameField.getText().toUpperCase()+" created");
        Stage stage = (Stage) okBtn.getScene().getWindow();
        saved.initOwner(stage);
        saved.showAndWait();
        stage.close();
    }
    private void closeDBonClose() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nameField.getScene().getWindow().setOnCloseRequest(e->{
                    db.close();
                });
            }
        });
    }
    @FXML private void tabKey() throws AWTException{
        robot.keyPress(KeyEvent.VK_TAB);
    }
    @FXML private void saveAndCloseOnMouse() throws SQLException {
        if(!nameField.getText().equals("")) {
            db.createGroupe(nameField.getText().toLowerCase());
            db.close();
            showCreatedMsg();
        } else {
            ErrorMessage msg = new ErrorMessage("oops some required fiels are EMPTY");
            msg.initOwner(okBtn.getScene().getWindow());
            msg.initModality(Modality.WINDOW_MODAL);
            msg.showAndWait();
        }
    }
    @FXML private void saveAndClose(javafx.scene.input.KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            if(!nameField.getText().equals("")) {
                db.createGroupe(nameField.getText().toLowerCase());
                db.close();
                showCreatedMsg();
            } else {
                ErrorMessage msg = new ErrorMessage("oops some required fiels are EMPTY");
                msg.initOwner(okBtn.getScene().getWindow());
                msg.initModality(Modality.WINDOW_MODAL);
                msg.showAndWait();
            }
        }
    }
    @FXML private void cancelOnMouse() throws SQLException {
        db.close();
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
    @FXML private void cancel(javafx.scene.input.KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            db.close();
            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML TextField nameField;
    @FXML Button okBtn, cancelBtn;
    private Robot robot;
    private DBAccess db;
}
