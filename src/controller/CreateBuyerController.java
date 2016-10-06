/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import backend.SingleBuyerDetails;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.ErrorMessage;
import model.alert.InfomationPopupWithMsg;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class CreateBuyerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            robot = new Robot();
            setGroups();
            db = new DBAccess();
            setCommas();
            closeDBonClose();
        } catch (Exception ex) {
            Logger.getLogger(CreateBuyerController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void setGroups() throws Exception {
        DBAccess db1 = new DBAccess();
        TextFields.bindAutoCompletion(groupField, db1.getGroupesNames());
        db1.close();
    }
    private boolean isFieldsNotEmpty() {
        if(nameField.getText().equals("") || toGetField.getText().replaceAll("," , "").equals("") || toGiveField.getText().replaceAll("," , "").equals("") || groupField.getText().equals("")) {
            return false;
        }
        return true;
    }
    
    private void save() {
        try {
            SingleBuyerDetails buyer = new SingleBuyerDetails(nameField.getText().toUpperCase().replaceAll("\\s+", "_"));
            db.createBuyers(nameField.getText().toUpperCase(), toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""), groupField.getText(), bankNameField.getText(), acNoField.getText(), branchField.getText(), ifcCodeField.getText());
            db.createNameInBalanceSheet("", nameField.getText().toUpperCase(), "Buyer", "2", toGiveField.getText().replaceAll("," , "").replace("-", ""), toGetField.getText().replaceAll("," , "").replace("-", ""));
            db.close();
            if(!toGetField.getText().replaceAll("," , "").equals("") && !toGiveField.getText().replaceAll("," , "").equals(""))
                buyer.insertBuyerDetails("","","", "", "", "", "", "", "", (Integer.parseInt(toGiveField.getText().replaceAll("," , ""))-Integer.parseInt(toGetField.getText().replaceAll("," , "")))+"" );
            else if(!toGetField.getText().replaceAll("," , "").equals(""))
                buyer.insertBuyerDetails("","", "", "", "", "", "", "", "", "-"+toGetField.getText().replaceAll("," , "") );
            else if(!toGiveField.getText().replaceAll("," , "").equals(""))
                buyer.insertBuyerDetails("","", "", "", "", "", "", "", "", toGiveField.getText().replaceAll("," , "") );
            buyer.close();
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
                ErrorMessage msg = new ErrorMessage("oops some required fields are EMPTY");
                msg.initOwner(okBtn.getScene().getWindow());
                msg.initModality(Modality.WINDOW_MODAL);
                msg.showAndWait();
            }
        }
    }
    @FXML private void saveAndClose(javafx.scene.input.KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            if(isFieldsNotEmpty()) {
                save();
                db.close();
                showCreatedMsg();
            } else {
                if(!toGetField.getText().replaceAll("," , "").equals("") || !toGiveField.getText().replaceAll("," , "").equals("")) {
                    save();
                    db.close();
                    showCreatedMsg();
                } else {
                    ErrorMessage msg = new ErrorMessage("oops some required fields are EMPTY");
                    msg.initOwner(okBtn.getScene().getWindow());
                    msg.initModality(Modality.WINDOW_MODAL);
                    msg.showAndWait();
                }
            }
        }
    }
    @FXML private void cancel(javafx.scene.input.KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            db.close();
            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML private void cancelOnMouse() throws SQLException {
        db.close();
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
    @FXML Button okBtn;
    @FXML TextField nameField, toGetField, toGiveField, groupField, bankNameField, acNoField, branchField, ifcCodeField;
    @FXML BorderPane mainBorder;
    private Robot robot;
    private DBAccess db;
}
