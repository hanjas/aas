/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.insertControllers;

import backend.DBAccess;
import backend.SingleBuyerDetails;
import backend.SingleSellerDetails;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.alert.SavedPopup;
import model.tableModel.InsertOtherDetailsTableModel;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class InsertOtherDetailsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            setDate();
            setSellers();
            setBuyers();
            robot = new Robot();
            list = FXCollections.observableArrayList();
            removedList = FXCollections.observableArrayList();
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
            detectClick();
            datePicker.setVisible(false);
            date.setVisible(false);
            partyName.setVisible(false);
            partyNameField.setVisible(false);
            billNoField.setVisible(false);
            billNo.setVisible(false);
            submitBtn.setVisible(false);
        } catch (Exception ex) {
            Logger.getLogger(InsertOtherDetailsController.class.getName()).log(Level.SEVERE, null, ex);
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
    private void setSellers() throws Exception {
        DBAccess db = new DBAccess();
        partyNameField.setItems(db.getSellersNames());
        TextFields.bindAutoCompletion(partyNameField.getEditor(), db.getSellersNames());
        db.close();
    }
    private void setBuyers() throws Exception {
        DBAccess db = new DBAccess();
        buyerNameField.setItems(db.getBuyersNames());
        TextFields.bindAutoCompletion(buyerNameField.getEditor(), db.getBuyersNames());
        db.close();
    }
    private String getDate() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private void setSalesDetails(String sellerName, String billNo) throws Exception {
        list.clear();
        DBAccess db = new DBAccess();
        list = db.getSalesDetails(sellerName, billNo);
        tableView.setItems(list);
        db.close();
        calcTotalBoxAndAmt();
    }
    private void detectClick() {
        tableView.setOnMouseClicked(e->{
            if(e.getClickCount() == 2) {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if(index >= 0) {
                    removedList.add(tableView.getItems().get(index));
                    tableView.getItems().remove(index);
                    calcTotalBoxAndAmt();
                }
            }
        });
    }
    
    private void calcTotalBoxAndAmt() {
        totalBox = 0;
        totalCash = 0;
        for (Iterator it = tableView.getItems().iterator(); it.hasNext();) {
            InsertOtherDetailsTableModel item = (InsertOtherDetailsTableModel) it.next();
            totalBox = totalBox + Float.parseFloat( item.getBoxCount() );
            totalCash = totalCash + Integer.parseInt( item.getBillAmt());
        }
        calcLossAndExcess();
    }
    private void calcLossAndExcess() {
        lossAndExcess = totalCash - Integer.parseInt(billAmtLabel.getText());
        boxNeed = totalBox - Float.parseFloat(totalBoxLabel.getText());
        boxNeedLabel.setText(boxNeed+" box");
        if(lossAndExcess < 0) {
            lossLabel.setText(lossAndExcess+"");
            excessLabel.setText("");
            lossLabel.setVisible(true);
            lossLabelT.setVisible(true);
            excessLabel.setVisible(false);
            excessLabelT.setVisible(false);
        } else {
            excessLabel.setText(lossAndExcess+"");
            lossLabel.setText("");
            excessLabel.setVisible(true);
            excessLabelT.setVisible(true);
            lossLabel.setVisible(false);
            lossLabelT.setVisible(false);
        }
    }
    private boolean isFieldsNotEmpty() {
        if(buyerNameField.getEditor().getText().equals("") || boxCountField.getText().equals("") ||
                billAmtField.getText().equals("")  )
            return false;
        return true;
    }
    private void insertToTable() {
        if(isFieldsNotEmpty()) {
            list.add(new InsertOtherDetailsTableModel(
                    buyerNameField.getEditor().getText(),
                    boxCountField.getText(),
                    billAmtField.getText(),
                    cashPaidField.getText(),
                    ""
            ));
            totalBox = totalBox + Float.parseFloat(boxCountField.getText());
            totalCash = totalCash + Integer.parseInt(billAmtField.getText());
            tableView.setItems(list);
            buyerNameField.getEditor().clear();
            boxCountField.clear();
            billAmtField.clear();
            cashPaidField.clear();
            calcLossAndExcess();
        }
    }
    private void doubleEnter() {
        insertToTable();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                okBtn.requestFocus();
            }
        });
    }
    private void singleEnter() {
        insertToTable();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                buyerNameField.getEditor().requestFocus();
            }
        });
    }
    private boolean task() {
        if(taskProcess == 0) {
            taskFinished = 0;
            taskProcess = 1;
            new Timer().scheduleAtFixedRate(new TimerTask() {   
                public void run() {
                    threadTime=threadTime+500;
                    if(threadTime == 1000) {
                        threadTime = 0;
                        taskFinished = 1;
                        this.cancel();
                    }
                }
            }, 0, 500);
        }
        return true;
    }
    
    private void save() throws Exception {
        for (Iterator it = removedList.iterator(); it.hasNext();) {
            InsertOtherDetailsTableModel item = (InsertOtherDetailsTableModel) it.next();
            SingleBuyerDetails buyer = new SingleBuyerDetails(item.getBuyerName().replaceAll("\\s+", "_"));
                buyer.removeData(item.getId());
                buyer.insertLossAndExcess(billNoField.getText(), partyNameLabel.getText(), lossLabel.getText().replace("-", ""), excessLabel.getText().replace("-", ""), boxNeed+"");
            buyer.close();
        }
        for (Iterator it = tableView.getItems().iterator(); it.hasNext();) {
            InsertOtherDetailsTableModel item = (InsertOtherDetailsTableModel) it.next();
            SingleBuyerDetails buyer = new SingleBuyerDetails(item.getBuyerName().replaceAll("\\s+", "_"));
            int total = Integer.parseInt(item.getBillAmt())+Integer.parseInt(buyer.getLastBalance(getDate()));
            int balance=0;
            if(item.getCashPaid().equals(""))
                balance = total;
            else balance = total - Integer.parseInt(item.getCashPaid());
            buyer.insertBuyerDetails(item.getId(), partyNameField.getEditor().getText() ,billNoField.getText(), getDate(), item.getBoxCount(), item.getBillAmt(), buyer.getLastBalance(getDate()), total+"", item.getCashPaid(), balance+"");
            buyer.insertLossAndExcess(billNoField.getText(), partyNameLabel.getText(), lossLabel.getText().replace("-", ""), excessLabel.getText().replace("-", ""), boxNeed+"");
            buyer.close();
        }
    }
    public void setNameAndBillNo(String sellerName, String billNo) throws Exception{
        partyNameField.getEditor().setText(sellerName);
        billNoField.setText(billNo);
        submitOnAction();
    }
    @FXML private void partyNameAction() throws Exception {
        if(!partyNameField.getEditor().getText().equals("")) {
            String names = "FIRSTLETTER"+partyNameField.getEditor().getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            seller = new SingleSellerDetails(names);
            billNoField.setText(seller.getLastBillNo()+"");
            seller.close();
            tabKey();
        }
    }
    
    @FXML public void submitOnAction() throws Exception {
        if(!partyNameField.getEditor().getText().equals("") && !billNoField.getText().equals("")) {
            String names = "FIRSTLETTER"+partyNameField.getEditor().getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            seller = new SingleSellerDetails(names);
            ResultSet rs = seller.getBillDetails(billNoField.getText());
            while(rs.next()) {
                totalBoxLabel.setText(rs.getString("TotalBox"));
                billAmtLabel.setText(rs.getString("TotalBillAmt"));
            }
            partyNameLabel.setText(partyNameField.getEditor().getText());
            seller.close();
            setSalesDetails(partyNameField.getEditor().getText(), billNoField.getText());
        }
        tabKey();
    }
    @FXML private void submit(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            submitOnAction();
        }
    }
    @FXML private void tabKey() {
        robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
    }
    @FXML private void saveAndClose(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            save();
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            SavedPopup popup = new SavedPopup();
            popup.initOwner(stage);
            popup.showAndWait();
            stage.close();
        }
    }
    @FXML private void saveAndCloseOnMouse() throws Exception {
        save();
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        SavedPopup popup = new SavedPopup();
        popup.initOwner(stage);
        popup.showAndWait();
        stage.close();
    }
    @FXML private void cancel(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML private void cashPaidAction() throws InterruptedException {
        enterCheck++;
        task();
        PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
        delay.setOnFinished(event -> {
            if(taskFinished == 1) {
                if(enterCheck == 1) singleEnter();
                if(enterCheck > 1) doubleEnter();
                enterCheck = 0;
                taskProcess = 0;
            }
        });
        delay.play();
    }
    
    @FXML DatePicker datePicker;
    @FXML TableView tableView;
    @FXML TextField billNoField, boxCountField, billAmtField, cashPaidField;
    @FXML ComboBox partyNameField, buyerNameField;
    @FXML Label partyNameLabel, totalBoxLabel, billAmtLabel, excessLabel,
            excessLabelT, lossLabel, lossLabelT, boxNeedLabel, date, partyName, billNo;
    @FXML Button okBtn, cancelBtn, submitBtn;
    private Robot robot;
    private SingleSellerDetails seller;
    private ObservableList list, removedList;
    private int taskProcess=0, taskFinished=0, threadTime=0, enterCheck=0,
            totalCash=0, lossAndExcess=0;
    private float totalBox = 0, boxNeed=0;
}
