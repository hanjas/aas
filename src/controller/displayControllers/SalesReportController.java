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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class SalesReportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDate();
    }    
    
    private String getFromDate() {
        return fromDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private String getToDate() {
        return toDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
        DBAccess db = new DBAccess();
        HashMap param = new HashMap();
        param.put("fromDate", getFromDate());
        param.put("toDate", getToDate());
        JasperPrint print = JasperFillManager.fillReport("src\\reports\\SalesReport.jasper", param, db.connection);
        db.close();
        JasperViewer viewer = new JasperViewer(print,false);
        viewer.setTitle("Sales Report");
        viewer.setAlwaysOnTop(true);
        viewer.setVisible(true);
    }
    
    @FXML private void tabKey() throws AWTException{
        Robot robot = new Robot();
        robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
    }
    @FXML private void showDetailsOnMouse() throws Exception {
        show();
        Stage stage = (Stage) okBtn.getScene().getWindow();
        stage.close();
    }
    @FXML private void showDetails(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            show();
            Stage stage = (Stage) okBtn.getScene().getWindow();
            stage.close();
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
    
    @FXML DatePicker fromDatePicker, toDatePicker;
    @FXML Button okBtn, cancelBtn;
}
