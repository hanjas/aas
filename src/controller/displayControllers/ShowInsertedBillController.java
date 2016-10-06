/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.displayControllers;

import backend.DBAccess;
import backend.SingleSellerDetails;
import controller.PrintBillViewController;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.ShowBillPopup;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class ShowInsertedBillController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            db = new DBAccess();
            robot = new Robot();
            setSellers();
        } catch (Exception ex) {
            Logger.getLogger(ShowInsertedBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void setSellers() throws SQLException {
        TextFields.bindAutoCompletion(partyNameField, db.getSellersNames());
    }
    private void setShowBillDatas() throws Exception {
        String names = "FIRSTLETTER"+partyNameField.getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
        seller = new SingleSellerDetails(names);
        ResultSet rs = seller.getBillDetails(billNoField.getText());
        while(rs.next()){
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dff = new SimpleDateFormat("dd-MM-yyyy");
            Date today = df.parse(rs.getString("date"));
            
            
            showbillController.setDate(dff.format(today));
            showbillController.setPrtyName(partyNameField.getText());
            showbillController.setShowBillNo(billNoField.getText());
            showbillController.setLiner(rs.getString("LinerName"));
            showbillController.setTruckNo(rs.getString("TruckNo"));
            showbillController.setCommission(rs.getString("Comm"));
            showbillController.setBoxExp(rs.getString("BoxExp"));
            showbillController.setTotalBox(rs.getString("TotalBox"));
            showbillController.setMarketExp(rs.getString("MarketExp"));
            showbillController.setAdvCutt(rs.getString("AdvCutting"));
            showbillController.setPercentCutt(rs.getString("PercentCutting"));
            showbillController.setIce(rs.getString("Ice"));
            showbillController.setPartyIce(rs.getString("PartyIce"));
            showbillController.setTruckRent(rs.getString("TruckRent"));
            showbillController.setPartyTruckRent(rs.getString("PartyTruckRent"));
            showbillController.setLinerAmt(rs.getString("LinerAmt"));
            showbillController.setOtherExp(rs.getString("OtherExp"));
            showbillController.setPartyExp(rs.getString("PartyExp"));
            showbillController.setTotalBillAmt(rs.getString("TotalBillAmt"));
            showbillController.setShowbillTotalExp(rs.getString("TotalExp"));
            showbillController.setShowbillSubTotal(rs.getString("SubTotal"));
            showbillController.setOldBalAndGrandTotal(rs.getString("oldBal"));
            showbillController.setShowbillCashPaid(rs.getString("CashPaid"));
            showbillController.setShowbillBalance(rs.getString("Balance"));
        }
            showbillController.setTableData(seller.getFishDetails(billNoField.getText()));
    }
    
    @FXML private void partyNameAction() throws Exception {
        if(!partyNameField.getText().equals("")) {
            String names = "FIRSTLETTER"+partyNameField.getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            seller = new SingleSellerDetails(names);
            billNoField.setText(seller.getLastBillNo()+"");
            seller.close();
            tabKey();
        }
    }
    
    @FXML private void showBillOnMouse() {
        try {
            if(!partyNameField.getText().equals("") && !billNoField.getText().equals("")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PrintBillView.fxml"));
                Pane myPane = (Pane)loader.load();
                showbillController = loader.getController();
                setShowBillDatas();
                ShowBillPopup popup = new ShowBillPopup(myPane);
                Stage popupStage = new Stage();
                Scene scene = new Scene(popup);
                popupStage.setScene(scene);
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.setAlwaysOnTop(true);
                popupStage.show();
                Stage stage = (Stage) submitBtn.getScene().getWindow();
                stage.close();
                popup.okBtn.setOnAction(e->{
                    popupStage.close();
                });
                popup.okBtn.setOnKeyPressed(e->{
                    if(e.getCode() == KeyCode.ENTER)
                        popupStage.close();
                });
            }
        } catch(Exception exx) {Logger.getLogger(ShowInsertedBillController.class.getName()).log(Level.SEVERE, null, exx);}
    }
    @FXML private void showBill(javafx.scene.input.KeyEvent event) throws SQLException {
        if(event.getCode() == KeyCode.ENTER){
            showBillOnMouse();
        }
    }
    
    @FXML private void tabKey() throws AWTException{
        robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
    }
    
    @FXML private TextField partyNameField, billNoField;
    @FXML private Button submitBtn;
    private SingleSellerDetails seller;
    private DBAccess db;
    private PrintBillViewController showbillController;
    private Robot robot;
}
