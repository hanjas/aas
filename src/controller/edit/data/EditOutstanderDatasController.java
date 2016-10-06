/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.edit.data;

import backend.DBAccess;
import backend.SingleOutstandingDetails;
import controller.edit.EditIncomeController;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import model.ListWithDeleteBtn;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class EditOutstanderDatasController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = FXCollections.observableArrayList();
        setDate();
        setOutstandingNames();
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
    private void setOutstandingNames() {
        try {
            list.clear();
            DBAccess db = new DBAccess();
            list = db.getOutstandingsNames();
            outstandingNameField.setItems(list);
            TextFields.bindAutoCompletion(outstandingNameField.getEditor(), list);
            db.close();
            
        } catch (Exception ex) {
            Logger.getLogger(EditOutstanderDatasController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML private void showBtnAction() {
        try {
            DBAccess db = new DBAccess();
            ResultSet rs = db.getOutstandingDetails(outstandingNameField.getValue().toString(), getFromDate(), getToDate());
            List<ListWithDeleteBtn> list = new ArrayList<>();
            while(rs.next()) {
                if(!rs.getString("toget").equals("")) {
                    ListWithDeleteBtn listBtn = new ListWithDeleteBtn(
                            rs.getString("date"),
                            rs.getString("id"),
                            rs.getString("idd"),
                            rs.getString("Category"),
                            rs.getString("toGet")
                    );
                    listBtn.button.setOnAction(e->{
                        try {
                            DBAccess dbs = new DBAccess();
                            dbs.deleteDayBook(listBtn.getIds());
                            dbs.deleteIncome(listBtn.getIdd());
                            dbs.insertDayBookBalance(listBtn.getDate());
                            dbs.close();
                            SingleOutstandingDetails so = new SingleOutstandingDetails(outstandingNameField.getValue().toString().replaceAll("\\s+", "_"));
                            so.deleteOutstandingData(listBtn.getDate(), listBtn.getIdd());
                            so.close();
                            list.remove(listBtn);
                            ObservableList<ListWithDeleteBtn> myObservableList = FXCollections.observableList(list);
                            listView.setItems(myObservableList);
                        } catch (Exception ex) {
                            Logger.getLogger(EditIncomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    list.add(listBtn);
                }
                if(!rs.getString("NonExpence").equals("")) {
                    ListWithDeleteBtn listBtn = new ListWithDeleteBtn(
                            rs.getString("date"),
                            rs.getString("id"),
                            rs.getString("idd"),
                            rs.getString("Category"),
                            rs.getString("NonExpence")
                    );
                    listBtn.button.setOnAction(e->{
                        try {
                            DBAccess dbs = new DBAccess();
                            dbs.deleteDayBook(listBtn.getIds());
                            dbs.deleteIncome(listBtn.getIdd());
                            dbs.insertDayBookBalance(listBtn.getDate());
                            dbs.close();
                            SingleOutstandingDetails so = new SingleOutstandingDetails(outstandingNameField.getValue().toString().replaceAll("\\s+", "_"));
                            so.deleteOutstandingData(listBtn.getDate(), listBtn.getIdd());
                            so.close();
                            list.remove(listBtn);
                            ObservableList<ListWithDeleteBtn> myObservableList = FXCollections.observableList(list);
                            listView.setItems(myObservableList);
                        } catch (Exception ex) {
                            Logger.getLogger(EditIncomeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    list.add(listBtn);
                }
            }
                ObservableList<ListWithDeleteBtn> myObservableList = FXCollections.observableList(list);
                listView.setItems(myObservableList);
                db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditIncomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML Button showBtn;
    @FXML ListView<ListWithDeleteBtn> listView;
    @FXML ComboBox outstandingNameField;
    private ObservableList list;
}