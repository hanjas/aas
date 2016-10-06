/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import backend.SingleInvestmentDeptDetails;
import com.sun.glass.events.KeyEvent;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.ErrorMessage;
import model.alert.InfomationPopupWithMsg;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class CreateInvestmentDeptController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            robot = new Robot();
            db = new DBAccess();
            setGroups();
            setCommas();
            closeDBonClose();
        } catch (Exception ex) {
            Logger.getLogger(CreateOutstandingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    private void showCreatedMsg() {
        InfomationPopupWithMsg saved = new InfomationPopupWithMsg("Created", nameField.getText().toUpperCase()+" created");
        Stage stage = (Stage) okBtn.getScene().getWindow();
        saved.initOwner(stage);
        saved.showAndWait();
        stage.close();
    }
    private void showErrorMsg() {
        ErrorMessage msg = new ErrorMessage(nameField.getText().toUpperCase()+" is already created");
        msg.initOwner( (Stage) okBtn.getScene().getWindow() );
        msg.showAndWait();
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
    private void setCommas() {
        toGetField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    toGetField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        toGiveField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    toGiveField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
    }
    private void setGroups() throws SQLException {
        TextFields.bindAutoCompletion(groupeField, db.getGroupesNames());
    }
    private boolean isFieldsNotEmpty() {
        if(nameField.getText().equals("") || groupeField.getText().equals("")) {
            if(toGetField.getText().replaceAll("," , "").equals("") && toGiveField.getText().replaceAll("," , "").equals("")) {
                return false;
            }
        }
        return true;
    }
    
    private void save() {
        try {
            SingleInvestmentDeptDetails investment = new SingleInvestmentDeptDetails(nameField.getText().toUpperCase().replaceAll("\\s+", "_"));
            db.createInvestmentDept(nameField.getText().toUpperCase(), toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""), groupeField.getText());
            db.createNameInBalanceSheet("", nameField.getText().toUpperCase(), "InvestmentDept", "5", toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""));
            db.insertInvestmentDeptDetails(nameField.getText().toUpperCase(), toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""));
            db.close();
            if(!toGetField.getText().replaceAll("," , "").equals("") && !toGiveField.getText().replaceAll("," , "").equals(""))
                investment.insertInvestmentDeptDetails("", "", "", "", "", (Integer.parseInt(toGiveField.getText().replaceAll("," , ""))-Integer.parseInt(toGetField.getText().replaceAll("," , "")))+"");
            else if(!toGetField.getText().replaceAll("," , "").equals(""))
                investment.insertInvestmentDeptDetails("", "", "", "", "", "-"+toGetField.getText().replaceAll("," , ""));
            else if(!toGiveField.getText().replaceAll("," , "").equals(""))
                investment.insertInvestmentDeptDetails("", "", "", "", "", toGiveField.getText().replaceAll("," , ""));
            investment.close();
            showCreatedMsg();
        } catch (Exception ex) {
            showErrorMsg();
        }
    }
    
    @FXML private void tabKey() throws AWTException{
        robot.keyPress(KeyEvent.VK_TAB);
    }
    @FXML private void saveAndCloseOnMouse() throws SQLException {
        if(isFieldsNotEmpty()) {
            save();
            db.close();
        } else {
            if(!toGetField.getText().replaceAll("," , "").equals("") || !toGiveField.getText().replaceAll("," , "").equals("")) {
                save();
                db.close();
            } else {
                ErrorMessage msg = new ErrorMessage("oops some required fiels are EMPTY");
                msg.initOwner(okBtn.getScene().getWindow());
                msg.initModality(Modality.WINDOW_MODAL);
                msg.showAndWait();
            }
        }
    }
    @FXML private void saveAndClose(javafx.scene.input.KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            saveAndCloseOnMouse();
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
    @FXML Button okBtn;
    @FXML private TextField nameField, toGetField, toGiveField, groupeField;
    private Robot robot;
    private DBAccess db;
    
}
