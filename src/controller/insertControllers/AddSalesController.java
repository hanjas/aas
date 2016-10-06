/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.insertControllers;

import backend.DBAccess;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.AddSalesListView;
import model.EditBillListView;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class AddSalesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setDate();
        Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    datePicker.requestFocus();
                    datePicker.getEditor().selectPositionCaret(2);
                    stage = (Stage) datePicker.getScene().getWindow();
                }
            });
            datePicker.addEventFilter(KeyEvent.KEY_PRESSED, e->{
                if(e.getCode() == KeyCode.ENTER)
                    tabKey();
        });
    }    
    private void tabKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
        } catch (AWTException ex) {
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
    
    @FXML private void showBtnAction() throws Exception {
        DBAccess db = new DBAccess();
        ResultSet rs = db.getBills(getDate());
        List<AddSalesListView> arrayList = new ArrayList<>();
        while(rs.next()) {
            AddSalesListView items = new AddSalesListView(rs.getString("billNo"), rs.getString("name"), rs.getString("totalBillAmt"), rs.getString("cashPaid"), stage);
            arrayList.add(items);
        }
        ObservableList<AddSalesListView> myObservableList = FXCollections.observableList(arrayList);
        listView.setItems(myObservableList);
        db.close();
    }
    @FXML private void showBtnOnKeyPress(KeyEvent e) throws Exception {
        if(e.getCode() == KeyCode.ENTER)
            showBtnAction();
    }
    
    @FXML private DatePicker datePicker;
    @FXML private Button showBtn;
    @FXML private ListView<AddSalesListView> listView;
    private Stage stage;
    private ObservableList list;
}
