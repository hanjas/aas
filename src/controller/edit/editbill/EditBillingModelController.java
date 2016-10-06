/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.edit.editbill;

import backend.DBAccess;
import backend.SingleSellerDetails;
import controller.EditBillViewController;
import controller.insertControllers.InsertBillController;
import controller.insertControllers.InsertOutstandingController;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.alert.InfomationPopupWithMsg;
import model.alert.ShowBillPopup;
import model.tableModel.BillTableModel;
import model.tableModel.EditBillTableModel;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class EditBillingModelController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list= FXCollections.observableArrayList();
        advCuttingCheckBox.setSelected(false);
        setActionAdvCuttingCheckBox();
        detectClick();
    }    
    public void checkAdvanceCuttedOrNot() {
        
    }
    public void setItems(String name, String id) {
        try {
            this.name = name;
            this.id = id;
            nameLabel.setText(name);
            String names = "FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            SingleSellerDetails seller = new SingleSellerDetails(names);
            ResultSet rs = seller.getBillDetails(name, id);
            float comm, totalBill , totalComm;
            int commm;
            while(rs.next()) {
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
                Date today = df.parse(rs.getString("date"));
                
                this.date = dff.format(today);
                linerNameField.setText(rs.getString("linerName"));
                truckNoField.setText(rs.getString("truckNo"));
                marketExpField.setText(rs.getString("marketExp"));
                iceField.setText(rs.getString("ice"));
                partyIceField.setText(rs.getString("PartyIce"));
                totalComm = rs.getFloat("comm");
                totalBill = rs.getFloat("totalBillAmt");
                comm = totalComm/totalBill*100;
                commm = (int)comm;
                commissionField.setText(commm+"" );
                totalBoxCountField.setText(rs.getString("totalBox"));
                totalBox = rs.getInt("totalBox");
                if(rs.getFloat("totalBox")>=1)
                    boxExpUnitField.setText(rs.getInt("boxExp")/rs.getFloat("totalBox")+"");
                totalComm = rs.getFloat("PercentCutting");
                comm = totalComm/totalBill*100;
                commm = (int)comm;
                percentageCuttingField.setText(commm+"");
                truckRentField.setText(rs.getString("TruckRent"));
                partyTruckRentField.setText(rs.getString("PartyTruckRent"));
                partyExpField.setText(rs.getString("PartyExp"));
                otherExpField.setText(rs.getString("OtherExp"));
                cashPaidField.setText(rs.getString("CashPaid"));
                linerAmtField.setText(rs.getString("LinerAmt"));
                totalBillLabel.setText(rs.getString("TotalBillAmt"));
                this.totalBill = rs.getInt("totalBillAmt");
                if(rs.getInt("advCutting")>=1)
                    cutted = true;
                if(cutted)
                    totalExpenceLabel.setText(rs.getInt("TotalExp")-rs.getInt("advCutting")+"");
                else
                    totalExpenceLabel.setText(rs.getInt("TotalExp")+"");
                oldBalLabel.setText(rs.getString("OldBal"));
                totalLabel.setText(rs.getString("GrandTotal"));
            }
            list.clear();
            list = seller.getEditBillFishDetails(id);
            tableView.setItems(list);
            seller.close();
            DBAccess db = new DBAccess();
            rs = db.getSellersDetails(name);
            while(rs.next()) {
                advCuttAmt = rs.getInt("advCutting");
                if(cutted)
                    advAmountLabel.setText(rs.getString("advCutting")+" ("+(rs.getInt("CapitalAdvance") - rs.getInt("duplicateAdvance") + rs.getInt("advCutting") )+") left");
                else {
                    if((rs.getInt("CapitalAdvance") - rs.getInt("duplicateAdvance") ) <= 0 || rs.getInt("advCutting")==0) {
                        advCuttingCheckBox.setSelected(false);
                        advCuttingCheckBox.setDisable(true);
                        isSelected = false;
                    }
                    advAmountLabel.setText(rs.getString("advCutting")+" ("+(rs.getInt("CapitalAdvance") - rs.getInt("duplicateAdvance") )+") left");
                }                    
                advCutting = rs.getString("advCutting");
                percentageCuttingField.setText(rs.getString("PercentCutting"));
                commissionField.setText(rs.getString("Comm"));
            }
            TextFields.bindAutoCompletion(fishNameField, db.getFishesNames());
            db.close();
            showOnMouse();
        } catch (Exception ex) {
            Logger.getLogger(EditBillingModelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void setActionAdvCuttingCheckBox() {
        advCuttingCheckBox.selectedProperty().addListener(e->{
            isSelected = !isSelected;
        });
    }
    private void detectClick() {
        tableView.setOnMouseClicked(e->{
            if(e.getClickCount() == 2) {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if(index >= 0) {
                    EditBillTableModel item = (EditBillTableModel)tableView.getItems().get(index);
                    tableView.getItems().remove(index);
                    calcTotalBoxAndAmt();
                }
            }
        });
    }
    private void calcTotalBoxAndAmt() {
        totalBox = 0;
        totalBill = 0;
        for (Iterator it = tableView.getItems().iterator(); it.hasNext();) {
            EditBillTableModel item = (EditBillTableModel) it.next();
            totalBox = totalBox + Float.parseFloat( item.getBoxCount() );
            totalBill = totalBill +(int) Float.parseFloat( item.getRs() );
        }
        setTotals();
        try {
            showOnMouse();
        } catch (Exception ex) {
            Logger.getLogger(EditBillingModelController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void doubleEnter() {
        insertToTable();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                commissionField.requestFocus();
            }
        });
    }
    private void singleEnter() {
        insertToTable();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                fishNameField.requestFocus();
            }
        });
    }
    private void insertToTable() {
        if(!fishNameField.getText().equals("") && !unitPriceField.getText().equals("") ){
            if(!boxCountField.getText().equals("")){
                list.add(new EditBillTableModel(fishNameField.getText(), boxCountField.getText(), unitPriceField.getText(), getRs(), ""));
                calcTotalBoxAndAmt();
                setTotals();
                showOnMouse();
                fishNameField.clear();
                boxCountField.clear();
                unitPriceField.clear();
                tableView.setItems(list);
            }
        }
    }
    private String getRs() {
        return (Float.parseFloat(boxCountField.getText())* Integer.parseInt( unitPriceField.getText() ) ) + "" ;
    }
    private void calculateTotal() {
        if(!boxCountField.getText().equals("")){
            totalBox = totalBox + Integer.parseInt( boxCountField.getText() );
            totalBill = totalBill + (int) ( Float.parseFloat(boxCountField.getText())* Integer.parseInt( unitPriceField.getText() ) ) ;
        }
    }
    private void setTotals() {
        totalBoxCountField.setText(totalBox+"");
        totalBillLabel.setText(totalBill+"");
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
    private void setShowBillDatas() {
        editBillController.setDate(date);
        editBillController.setPrtyName(name);
        editBillController.setBillNo();
        editBillController.setTableData(tableView.getItems());
        editBillController.setLiner(linerNameField.getText());
        editBillController.setTruckNo(truckNoField.getText().toUpperCase());
        editBillController.setCommission( (int) ((Float.parseFloat(totalBillLabel.getText())/100) * Float.parseFloat(commissionField.getText())) +"");
        editBillController.setBoxExp( (int) (Float.parseFloat(totalBoxCountField.getText()) * Float.parseFloat(boxExpUnitField.getText())) + "");
        editBillController.setTotalBox(totalBox+"");
        editBillController.setMarketExp(marketExpField.getText());
        if(isSelected)
            editBillController.setAdvCutt(advCutting);
        else editBillController.setAdvCutt("0");
        editBillController.setPercentCutt( (int) ((Integer.parseInt(totalBillLabel.getText())/100) * Float.parseFloat(percentageCuttingField.getText())) +"" );
        editBillController.setIce(iceField.getText());
        editBillController.setPartyIce(partyIceField.getText());
        editBillController.setTruckRent(truckRentField.getText());
        editBillController.setPartyTruckRent(partyTruckRentField.getText());
        editBillController.setLinerAmt(linerAmtField.getText());
        editBillController.setOtherExp(otherExpField.getText());
        editBillController.setPartyExp(partyExpField.getText());
        editBillController.setTotalExp();
        editBillController.setTotalBillAmt(totalBill+"");
        editBillController.setSubTotal();
        editBillController.setOldBalAndGrandTotal(oldBalLabel.getText());
        editBillController.setCashPaid(cashPaidField.getText());
        editBillController.setAdvCuttedOrNot(cutted, advCuttAmt);
    }
    @FXML private void advCuttingCheckBoxAction(KeyEvent event) throws AWTException {
        if(event.getCode() == KeyCode.ENTER)
            tabKey();
    }
    @FXML private void unitAction() throws InterruptedException {
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
    @FXML private void show(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            showOnMouse();
        }
    }
    @FXML private void showOnMouse() {
        int marketExp=0, ice=0, partyIce=0, comm=0, boxExp=0, percentCutt=0, partyTruckRent=0,
        truckRent=0, linerAmt=0, partyExp=0, otherExp=0, advCutt=0, totalExp=0;

        if(!commissionField.getText().equals("")){
            comm = (int) ((Float.parseFloat(totalBillLabel.getText())/100) * Float.parseFloat(commissionField.getText()));
        }
        if(!totalBoxCountField.getText().equals("") || !boxExpUnitField.getText().equals("") ){
            boxExp = (int) (Float.parseFloat(totalBoxCountField.getText()) * Float.parseFloat(boxExpUnitField.getText()));
        }
        if(!marketExpField.getText().replaceAll(",", "").equals("")){
            marketExp = Integer.parseInt( marketExpField.getText().replaceAll(",", "") );
        }
        if(!percentageCuttingField.getText().equals("")){
            percentCutt =  (int) ((Float.parseFloat(totalBillLabel.getText())/100) * Float.parseFloat(percentageCuttingField.getText()));
        }
        if(!partyTruckRentField.getText().equals("")){
            partyTruckRent = Integer.parseInt( partyTruckRentField.getText() );
        }
        if(!truckRentField.getText().equals("")){
            truckRent = Integer.parseInt( truckRentField.getText() );
        }
        if(!linerAmtField.getText().replaceAll(",", "").equals("")){
            linerAmt = Integer.parseInt( linerAmtField.getText().replaceAll(",", "") );
        }
        if(!partyExpField.getText().replaceAll(",", "").equals("")){
            partyExp = Integer.parseInt( partyExpField.getText().replaceAll(",", "") );
        }
        if(!otherExpField.getText().replaceAll(",", "").equals("")){
            otherExp = Integer.parseInt( otherExpField.getText().replaceAll(",", "") );
        }
        if(!iceField.getText().replaceAll(",", "").equals("")){
            ice = Integer.parseInt( iceField.getText().replaceAll(",", "") );
        }
        if(!partyIceField.getText().replaceAll(",", "").equals("")){
            partyIce = Integer.parseInt( partyIceField.getText().replaceAll(",", "") );
        }
        if(isSelected)
            advCutt = Integer.parseInt(advCutting);
        totalExp = comm+boxExp+marketExp+percentCutt+partyTruckRent+truckRent+linerAmt+partyExp+otherExp+ice+partyIce+advCutt;
        totalExpenceLabel.setText(totalExp+"");
        totalLabel.setText( ( Integer.parseInt(totalBillLabel.getText())-Integer.parseInt(totalExpenceLabel.getText())+Integer.parseInt(oldBalLabel.getText()) )+"" );
        tabKey();
    }
    @FXML private void saveAndClose(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditBillView.fxml"));
            Pane myPane = (Pane)loader.load();
            editBillController = loader.getController();
            setShowBillDatas();
            ShowBillPopup popup = new ShowBillPopup(myPane);
            Stage popupStage = new Stage();
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setAlwaysOnTop(true);
            popupStage.show();
            popup.okBtn.setOnAction(e->{
                editBillController.saveToDb(id);
                editBillController.close();
                Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                InfomationPopupWithMsg saved = new InfomationPopupWithMsg("Saved", "Bill Saved");
                saved.initOwner(stage);
                saved.showAndWait();
                stage.close();
                stage = (Stage) okBtn.getScene().getWindow();
                stage.close();
//                savedPopup.showAndWait();
            });
            popup.okBtn.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ENTER){
                    editBillController.saveToDb(id);
                    editBillController.close();
                    Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                    stage.close();
                    stage = (Stage) okBtn.getScene().getWindow();
                    stage.close();
//                    savedPopup.showAndWait();
                }
            });
            popup.printBtn.setOnAction(e->{
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
                PrinterJob job = PrinterJob.createPrinterJob();
                job.getJobSettings().setPageLayout(pageLayout);
                if(job != null){
                    boolean success = job.printPage(myPane);
                    if(success){
                        job.endJob();
                    }
                }
            });
        } 
    }
    @FXML private void saveAndCloseOnMouse() throws SQLException {
        try {
            //        save();
//            Stage showBillStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditBillView.fxml"));
            Pane myPane = (Pane)loader.load();
            editBillController = loader.getController();
            setShowBillDatas();
            ShowBillPopup popup = new ShowBillPopup(myPane);
            Stage popupStage = new Stage();
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setAlwaysOnTop(true);
            popupStage.show();
            popup.okBtn.setOnAction(e->{
                editBillController.saveToDb(id);
                editBillController.close();
                Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                InfomationPopupWithMsg saved = new InfomationPopupWithMsg("Saved", "Bill Saved");
                saved.initOwner(stage);
                saved.showAndWait();
                stage.close();
                stage = (Stage) okBtn.getScene().getWindow();
                stage.close();
//                savedPopup.showAndWait();
            });
            popup.okBtn.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ENTER){
                    editBillController.saveToDb(id);
                    editBillController.close();
                    Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                    stage.close();
                    stage = (Stage) okBtn.getScene().getWindow();
                    stage.close();
//                    savedPopup.showAndWait();
                }
            });
            popup.printBtn.setOnAction(e->{
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
                PrinterJob job = PrinterJob.createPrinterJob();
                job.getJobSettings().setPageLayout(pageLayout);
                if(job != null){
                    boolean success = job.printPage(myPane);
                    if(success){
                        job.endJob();
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(InsertBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML private void cancel() {
        
    }
    @FXML private void tabKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
        } catch (AWTException ex) {
            Logger.getLogger(InsertOutstandingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML TextField partyNameField, fishNameField, boxCountField, unitPriceField,
            linerNameField, truckNoField, marketExpField, iceField, partyIceField,
            commissionField, totalBoxCountField, boxExpUnitField, percentageCuttingField,
            truckRentField, partyTruckRentField, partyExpField, otherExpField, cashPaidField, linerAmtField;
    @FXML CheckBox advCuttingCheckBox;
    @FXML TableView tableView;
    @FXML Label advAmountLabel, totalBillLabel, totalExpenceLabel, oldBalLabel, totalLabel, nameLabel;
    @FXML private Button okBtn, cancelBtn;
    private ObservableList<EditBillTableModel> list;
    private int taskProcess=0, taskFinished=0, threadTime=0, enterCheck=0, 
            totalBill = 0, advCutt=0, advCuttAmt=0;
    private float totalBox=0;
    private boolean isSelected = false, cutted=false;
    private String advCutting="", date="", name="", id="";
    private EditBillViewController editBillController;
}
