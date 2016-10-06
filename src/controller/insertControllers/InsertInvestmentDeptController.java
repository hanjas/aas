/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.insertControllers;

import backend.DBAccess;
import backend.SingleInvestmentDeptDetails;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class InsertInvestmentDeptController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setInvestmentDept();
        setDate();
        setComma();
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
    private void setComma() {
        int i=0;
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
    private void setInvestmentDept() {
        try {
            DBAccess db = new DBAccess();
            investmentDeptNameField.setItems(db.getInvestmentDeptNames());
            TextFields.bindAutoCompletion(investmentDeptNameField.getEditor(), db.getInvestmentDeptNames());
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(InsertOutstandingController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    private String getDate() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private String getBalance(String oldBalance) {
        if(!toGetField.getText().replaceAll("," , "").equals("") && !toGiveField.getText().replaceAll("," , "").equals("")) // if both are not empty
            return ( Integer.parseInt(oldBalance) + Integer.parseInt(toGetField.getText().replaceAll("," , "")) - Integer.parseInt(toGiveField.getText().replaceAll("," , "")) )+"";
        else if(!toGetField.getText().replaceAll("," , "").equals("") && toGiveField.getText().replaceAll("," , "").equals("")) // if buy not and give empty
            return ( Integer.parseInt(oldBalance) + Integer.parseInt(toGetField.getText().replaceAll("," , "")) )+"";
        else if(toGetField.getText().replaceAll("," , "").equals("") && !toGiveField.getText().replaceAll("," , "").equals("")) // if buy empty and give not
            return ( Integer.parseInt(oldBalance) - Integer.parseInt(toGiveField.getText().replaceAll("," , "")) )+"";
        return oldBalance;
    }
    private boolean checkFields() {
        if(categoryField.getText().equals("") || investmentDeptNameField.getEditor().getText().equals(""))
            return false;
        else if(toGetField.getText().replaceAll("," , "").equals("") && toGiveField.getText().replaceAll("," , "").equals(""))
            return false;
        return true;
    }
    
    
    private void save() throws Exception{
        SingleInvestmentDeptDetails investment = new SingleInvestmentDeptDetails(investmentDeptNameField.getEditor().getText().replaceAll("\\s+", "_"));
        investment.insertInvestmentDeptDetails(getDate(), categoryField.getText(), investment.getLastBalance( getDate() ), toGetField.getText().replaceAll("," , ""), toGiveField.getText().replaceAll("," , ""), getBalance(investment.getLastBalance( getDate() ) ) );
        investment.close();
    }
    
    
    @FXML private void tabKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
        } catch (AWTException ex) {
            Logger.getLogger(InsertOutstandingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML private void nextAction(KeyEvent e) throws Exception {
        if(e.getCode() == KeyCode.ENTER) {
            if(checkFields()) {
                save();
                investmentDeptNameField.getEditor().clear();
                investmentDeptNameField.getSelectionModel().clearSelection();
                categoryField.clear();
                toGetField.clear();
                toGiveField.clear();
            }
            investmentDeptNameField.getEditor().requestFocus();
        }
    }
    @FXML private void saveAndClose(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            if(checkFields()) {
                save();
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
            }
        }
    }
    @FXML private void saveAndCloseOnMouse() throws Exception {
        if(checkFields()) {
            save();
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
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
    @FXML ComboBox investmentDeptNameField;
    @FXML TextField toGetField, toGiveField, categoryField;
    @FXML Button okBtn, cancelBtn;
    
}

