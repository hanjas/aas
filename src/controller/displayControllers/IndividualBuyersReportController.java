/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.displayControllers;

import backend.DBAccess;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.TextFields;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.view.*;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class IndividualBuyersReportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            robot = new Robot();
            setBuyers();
            setDate();
            fromDatePicker.addEventFilter(KeyEvent.KEY_PRESSED, e->{
                if(e.getCode() == KeyCode.ENTER) 
                    fromDateTab();
            });
            toDatePicker.addEventFilter(KeyEvent.KEY_PRESSED, e->{
                if(e.getCode() == KeyCode.ENTER)
                    tabKey();
            });
        } catch (Exception ex) {
            Logger.getLogger(IndividualBuyersReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private String getFromDate() {
        return fromDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private String getToDate() {
        return toDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private void setBuyers() throws Exception {
        DBAccess db = new DBAccess();
        buyerNameField.setItems(db.getBuyersNames());
        TextFields.bindAutoCompletion(buyerNameField.getEditor(), db.getBuyersNames());
        db.close();
    }    
    private void setDate() {
        fromDatePicker.setConverter(new StringConverter<LocalDate>() {
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
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setConverter(new StringConverter<LocalDate>() {
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
        toDatePicker.setValue(LocalDate.now());
    }
    private void show() throws Exception {
        if(!buyerNameField.getEditor().getText().equals("")){
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            param.put("buyerName", buyerNameField.getEditor().getText());
            param.put("frmDate", getFromDate());
            param.put("toDate", getToDate());
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\IndividualBuyer.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            Stage stage = (Stage) okBtn.getScene().getWindow();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
            jasperReport.setOnCloseRequest(e->{
                stage.close();
            });
        }
    }
    @FXML private void nameFieldTab() {
        fromDatePicker.requestFocus();
        fromDatePicker.getEditor().selectPositionCaret(2);
    }
    @FXML private void fromDateTab() {
        toDatePicker.requestFocus();
        toDatePicker.getEditor().selectPositionCaret(2);
    }
    @FXML private void tabKey() {
        robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
    }
    @FXML private void showDetailsOnMouse() throws Exception {
        show();
    }
    @FXML private void showDetails(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            show();
        }
    }
    @FXML private void cancelOnMouse() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    @FXML private void cancel(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML private ComboBox buyerNameField;
    @FXML private Button okBtn, cancelBtn;
    private Robot robot;
    
}
