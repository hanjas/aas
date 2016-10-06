/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.insertControllers;

import backend.DBAccess;
import backend.ExpenceDetails;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.alert.ErrorMessage;
import model.alert.InfomationPopupWithMsg;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class InsertExpenceController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDate();
        setGroups();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                datePicker.requestFocus();
                datePicker.getEditor().selectPositionCaret(2);
            }
        });
        datePicker.addEventFilter(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode() == KeyCode.ENTER)
                tabKey();
        });
    }    
    private void setDate() {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override 
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override 
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        datePicker.setValue(LocalDate.now());
    }
    private void setGroups(){
        try {
            DBAccess db = new DBAccess();
            TextFields.bindAutoCompletion(groupField, db.getGroupesNames());
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(InsertIncomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private String getDate() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private boolean checkFields() {
        if(categoryField.getText().equals("") || amountField.getText().equals("") || groupField.getText().equals(""))
            return false;
        return true;
    }
    private void save() throws Exception{
        ExpenceDetails exp = new ExpenceDetails();
        exp.insertExpence(getDate(), categoryField.getText(), amountField.getText(), groupField.getText());
        exp.close();
    }
    
    @FXML private void tabKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
        } catch (AWTException ex) {
            Logger.getLogger(InsertExpenceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML private void nextAction(KeyEvent e) throws Exception {
        if(e.getCode() == KeyCode.ENTER) {
            if(checkFields()) {
                save();
                categoryField.clear();
                amountField.clear();
            }
            categoryField.requestFocus();
        }
    }
    
    @FXML private void saveAndClose(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            saveAndCloseOnMouse();
        }
    }
    @FXML private void saveAndCloseOnMouse() throws Exception {
        if(checkFields()) {
            save();
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            InfomationPopupWithMsg msg = new InfomationPopupWithMsg("Saved", "Expence Saved");
            msg.initOwner(stage);
            msg.showAndWait();
            stage.close();
        } else {
            ErrorMessage err = new ErrorMessage("oops some required field are EMPTY");
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
            err.initOwner(stage);
            err.showAndWait();
        }
    }
    @FXML private void cancel(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML private void cancelOnMouse() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML DatePicker datePicker;
    @FXML TextField categoryField, amountField, groupField;
    @FXML Button okBtn, cancelBtn;
}
