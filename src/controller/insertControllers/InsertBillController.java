/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.insertControllers;

import backend.DBAccess;
import backend.SingleSellerDetails;
import controller.CreateSellerController;
import controller.PrintBillViewController;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import model.alert.SavedPopup;
import model.alert.ShowBillPopup;
import model.tableModel.BillTableModel;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class InsertBillController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            db = new DBAccess();
            pattern = "dd-MM-yyyy";
            setDate();
            setSellers();
            setFish();
            detectClick();
            advCuttingCheckBox.setSelected(true);
            setActionAdvCuttingCheckBox();
            list = FXCollections.observableArrayList();
            robot = new Robot();
            savedPopup = new SavedPopup();
            setCommaActions();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    datePicker.getScene().getWindow().setOnCloseRequest(e->{
                        db.close();
                    });
                }
            });
            
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
            
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    tableView.getScene().getWindow().setOnCloseRequest(e->{
                        db.close();
                    });
                }
            });
            
        } catch (Exception ex) {Logger.getLogger(CreateSellerController.class.getName()).log(Level.SEVERE, null, ex);}
    }
    private void setCommaActions() {
        int i=0;
        cashPaidField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    cashPaidField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        unitPriceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    unitPriceField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        marketExpField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    marketExpField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        iceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    iceField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        partyExpField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    partyExpField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        otherExpField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    otherExpField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
        linerAmtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obsevable, String oldvalue, String newvalue) {
                if(!newvalue.equals("")){
                    Long i = Long.parseLong(newvalue.replace(",", ""));
                    linerAmtField.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
                }
            }
        });
    }
    private void detectClick() {
        tableView.setOnMouseClicked(e->{
            if(e.getClickCount() == 2) {
                int index = tableView.getSelectionModel().getSelectedIndex();
                if(index >= 0) {
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
            BillTableModel item = (BillTableModel) it.next();
            totalBox = totalBox + Float.parseFloat( item.getBoxCount() );
            totalBill = totalBill + (int) Float.parseFloat( item.getRs() );
        }
        setTotals();
        try {
            showOnMouse();
        } catch (Exception ex) {
            Logger.getLogger(InsertBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void setSellers() throws SQLException {
        TextFields.bindAutoCompletion(partyNameField, db.getSellersNames());
    }
    private void setFish() throws SQLException {
        TextFields.bindAutoCompletion(fishNameField, db.getFishesNames());
    }
    private void setDate() {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

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
        if(!fishNameField.getText().equals("") && !unitPriceField.getText().replaceAll(",", "").replaceAll(",", "").equals("") ){
            if(!boxCountField.getText().equals("")){
                list.add(new BillTableModel(fishNameField.getText(), boxCountField.getText(), unitPriceField.getText().replaceAll(",", "").replaceAll(",", ""), getRs()));
                calculateTotal();
                setTotals();
                showOnMouse();
//                calcTotalBoxAndAmt();
                fishNameField.clear();
                boxCountField.clear();
                unitPriceField.clear();
                tableView.setItems(list);
            }
        }
    }
    private void calculateTotal() {
        if(!boxCountField.getText().equals(""))
            totalBox = totalBox + Float.parseFloat( boxCountField.getText() );
            totalBill = totalBill + (int) ( Float.parseFloat(boxCountField.getText())* Integer.parseInt( unitPriceField.getText().replaceAll(",", "").replaceAll(",", "") ) ) ;
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
    
    private String getRs() {
        return (Float.parseFloat(boxCountField.getText())* Integer.parseInt( unitPriceField.getText().replaceAll(",", "").replaceAll(",", "") ) ) + "" ;
    }
    private String getDate() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
    private String getDateForOldBal() {
        return datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private void setActionAdvCuttingCheckBox() {
        advCuttingCheckBox.selectedProperty().addListener(e->{
            isSelected = !isSelected;
        });
    }
    private void setShowBillDatas() {
        printBillViewController.setDate(getDate());
        printBillViewController.setPrtyName(partyNameField.getText());
        printBillViewController.setBillNo();
        printBillViewController.setTableData(tableView.getItems());
        printBillViewController.setLiner(linerNameField.getText());
        printBillViewController.setTruckNo(truckNoField.getText().toUpperCase());
        printBillViewController.setCommission( (int) ((Float.parseFloat(totalBillLabel.getText())/100) * Float.parseFloat(commissionField.getText())) +"");
        printBillViewController.setBoxExp( (int) (Float.parseFloat(totalBoxCountField.getText()) * Float.parseFloat(boxExpUnitField.getText())) + "");
        printBillViewController.setTotalBox(totalBox+"");
        printBillViewController.setMarketExp(marketExpField.getText().replaceAll(",", ""));
        if(isSelected)
            printBillViewController.setAdvCutt(advCutting);
        else printBillViewController.setAdvCutt("0");
        printBillViewController.setPercentCutt( (int) ((Integer.parseInt(totalBillLabel.getText())/100) * Float.parseFloat(percentageCuttingField.getText())) +"" );
        printBillViewController.setIce(iceField.getText().replaceAll(",", ""));
        printBillViewController.setPartyIce(partyIceField.getText().replaceAll(",", ""));
        printBillViewController.setTruckRent(truckRentField.getText());
        printBillViewController.setPartyTruckRent(partyTruckRentField.getText());
        printBillViewController.setLinerAmt(linerAmtField.getText().replaceAll(",", ""));
        printBillViewController.setOtherExp(otherExpField.getText().replaceAll(",", ""));
        printBillViewController.setPartyExp(partyExpField.getText().replaceAll(",", ""));
        printBillViewController.setTotalExp();
        printBillViewController.setTotalBillAmt(totalBill+"");
        printBillViewController.setSubTotal();
        printBillViewController.setOldBalAndGrandTotal(oldBalLabel.getText());
        printBillViewController.setCashPaid(cashPaidField.getText().replaceAll(",", ""));
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
    @FXML private void getDetails() throws SQLException, AWTException {
        rs = db.getSellersDetails(partyNameField.getText());
        while(rs.next()) {
            boxExpUnitField.setText(rs.getString("BoxExp"));
            commissionField.setText(rs.getString("comm"));
            percentageCuttingField.setText(rs.getString("percentCutting"));
            if((rs.getInt("CapitalAdvance") - rs.getInt("duplicateAdvance") ) <= 0 || rs.getInt("advCutting")==0 ) {
                advCuttingCheckBox.setSelected(false);
                advCuttingCheckBox.setDisable(true);
                isSelected = false;
            }
            advAmountLabel.setText(rs.getString("advCutting")+" ("+(rs.getInt("CapitalAdvance") - rs.getInt("duplicateAdvance") )+") left");
            advCutting = rs.getString("advCutting");
            totalBillLabel.setText("0");
            totalBoxCountField.setText("0");
        }
        calcTotalBoxAndAmt();
        tabKey();
    }
    @FXML private void tabKey() {
        robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
    }
    @FXML private void saveAndCloseOnMouse() throws SQLException {
        try {
            //        save();
//            Stage showBillStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PrintBillView.fxml"));
            Pane myPane = (Pane)loader.load();
            printBillViewController = loader.getController();
            setShowBillDatas();
            ShowBillPopup popup = new ShowBillPopup(myPane);
            Stage popupStage = new Stage();
            Scene scene = new Scene(popup);
            popupStage.setScene(scene);
            popupStage.initOwner((Stage) okBtn.getScene().getWindow());
            popupStage.show();
            popup.okBtn.setOnAction(e->{
                db.close();
                printBillViewController.saveToDb();
                printBillViewController.close();
                Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                stage.close();
                stage = (Stage) okBtn.getScene().getWindow();
                stage.close();
                savedPopup.showAndWait();
            });
            popup.okBtn.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ENTER){
                    db.close();
                    printBillViewController.saveToDb();
                    printBillViewController.close();
                    Stage stage = (Stage) popup.okBtn.getScene().getWindow();
                    stage.close();
                    stage = (Stage) okBtn.getScene().getWindow();
                    stage.close();
                    savedPopup.showAndWait();
                }
            });
            popup.printBtn.setOnAction(e->{
                Printer printer = Printer.getDefaultPrinter();
                PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
                PrinterJob job = PrinterJob.createPrinterJob();
                job.getJobSettings().setPageLayout(pageLayout);
                if(job != null){
                    boolean success = job.printPage(myPane);
                    if(success){
                        job.endJob();
                    }
                }
            });
            popup.okBtn.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ENTER){
                    Printer printer = Printer.getDefaultPrinter();
                    PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
                    PrinterJob job = PrinterJob.createPrinterJob();
                    job.getJobSettings().setPageLayout(pageLayout);
                    if(job != null){
                        boolean success = job.printPage(myPane);
                        if(success){
                            job.endJob();
                        }
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(InsertBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML private void saveAndClose(KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            saveAndCloseOnMouse();
        }
    }
    @FXML private void cancel(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML private void show(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            showOnMouse();
        }
    }
    @FXML private void showOnMouse() {
        try {
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
            totalExp = comm+boxExp+marketExp+percentCutt+truckRent+partyTruckRent+linerAmt+partyExp+otherExp+ice+partyIce+advCutt;
            totalExpenceLabel.setText(totalExp+"");
            db.close();
            String names = "FIRSTLETTER"+partyNameField.getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            SingleSellerDetails sellerDetails = new SingleSellerDetails(names);
            oldBalLabel.setText(sellerDetails.getLastBalance(getDateForOldBal()));
            int totalBill = ( Integer.parseInt(totalBillLabel.getText())-Integer.parseInt(totalExpenceLabel.getText())+Integer.parseInt(oldBalLabel.getText()) );
            totalLabel.setText( NumberFormat.getNumberInstance(Locale.US).format(totalBill) +"" );
            sellerDetails.close();
            tabKey();
        } catch (Exception ex) {
            Logger.getLogger(InsertBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML DatePicker datePicker;
    @FXML TextField partyNameField, fishNameField, boxCountField, unitPriceField,
            linerNameField, truckNoField, marketExpField, iceField, partyIceField,
            commissionField, totalBoxCountField, boxExpUnitField, percentageCuttingField,
            truckRentField, partyTruckRentField, partyExpField, otherExpField, cashPaidField, linerAmtField;
    @FXML CheckBox advCuttingCheckBox;
    @FXML TableView tableView;
    @FXML Label advAmountLabel, totalBillLabel, totalExpenceLabel, oldBalLabel, totalLabel;
    @FXML private Button okBtn, cancelBtn;
    private String pattern, advCutting="";
    private Robot robot;
    private DBAccess db;
    private ObservableList list;
    private ResultSet rs;
    private PrintBillViewController printBillViewController;
    private int taskProcess=0, taskFinished=0, threadTime=0, enterCheck=0, 
            totalBill = 0;
    private float totalBox = 0;
    private boolean isSelected = true;
    private SavedPopup savedPopup;
}
