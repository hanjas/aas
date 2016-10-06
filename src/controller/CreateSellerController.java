/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import backend.SingleSellerDetails;
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
 * @author Haxx
 */
public class CreateSellerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            db = new DBAccess();
            robot = new Robot();
            setGroup();
            setCommas();
            closeDBonClose();
        } catch (Exception ex) {Logger.getLogger(CreateSellerController.class.getName()).log(Level.SEVERE, null, ex);}
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
        advCuttingField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    advCuttingField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        capitalAdvanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    capitalAdvanceField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        boxExpField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    boxExpField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
    }
    private void setGroup() throws SQLException {
        TextFields.bindAutoCompletion(groupField, db.getGroupesNames());
    }
    private boolean isFieldsNotEmpty() {
        if(!nameField.getText().equals("") || !toGetField.getText().replaceAll("," , "").equals("") || !toGiveField.getText().replaceAll("," , "").equals("") || !commissionField.getText().equals("") || !capitalAdvanceField.getText().replaceAll("," , "").equals("")) {
            if(!advCuttingField.getText().replaceAll("," , "").equals("") || !percentCuttingField.getText().equals("") || !boxExpField.getText().replaceAll("," , "").equals("") || !groupField.getText().equals("")) {
                return true;
            } else return false;
        }
        return false;
    }
    
    private void save()  {
        try {
            String name = "FIRSTLETTER"+nameField.getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            SingleSellerDetails seller = new SingleSellerDetails(name);
            db.createSeller(nameField.getText().toUpperCase(), toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""), commissionField.getText(), capitalAdvanceField.getText().replaceAll("," , ""), "0", advCuttingField.getText().replaceAll("," , ""), percentCuttingField.getText(),"0", boxExpField.getText().replaceAll("," , ""), groupField.getText(), bankNameField.getText(), acNoField.getText(), branchField.getText(), ifcCodeField.getText());
            db.createNameInBalanceSheet("", nameField.getText().toUpperCase(), "Seller", "1", toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""));
            db.close();
            if(!toGetField.getText().replaceAll("," , "").equals("") && !toGiveField.getText().replaceAll("," , "").equals(""))
                seller.insertBillDetails("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", (Integer.parseInt(toGiveField.getText().replaceAll("," , ""))-Integer.parseInt(toGetField.getText().replaceAll("," , "")))+"" );
            else if(!toGetField.getText().replaceAll("," , "").equals(""))
                seller.insertBillDetails("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "-"+toGetField.getText().replaceAll("," , "") );
            else if(!toGiveField.getText().replaceAll("," , "").equals(""))
                seller.insertBillDetails("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", toGiveField.getText().replaceAll("," , "") );
            seller.close();
            showCreatedMsg();
        } catch (Exception ex) {
            showErrorMsg();
        }
    }
    
    @FXML private void tabKey() throws AWTException{
        robot.keyPress(KeyEvent.VK_TAB);
    }
    @FXML private void saveAndClose(javafx.scene.input.KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            saveAndCloseOnMouse();
        }
    }
    @FXML private void saveAndCloseOnMouse() throws Exception {
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
    @FXML private void cancel(javafx.scene.input.KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            db.close();
            stage.close();
        }
    }
    @FXML private void cancelOnMouse() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        db.close();
        stage.close();
    }
    @FXML private TextField nameField, toGetField, toGiveField, commissionField,capitalAdvanceField, advCuttingField;
    @FXML private TextField percentCuttingField, boxExpField, groupField, bankNameField;
    @FXML private TextField acNoField, branchField, ifcCodeField;
    @FXML private Button okBtn, cancelBtn;
    private Robot robot;
    private DBAccess db;
}
