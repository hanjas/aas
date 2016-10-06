/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.SingleSellerDetails;
import java.net.URL;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import model.tableModel.BillTableModel;

/**
 * FXML Controller class
 *
 * @author BCz
 */
public class PrintBillViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                partyNameLabel.getScene().getWindow().setOnCloseRequest(e->{
                    seller.close();
                });
            }
        });

    }    
    
    public void setTableData(ObservableList tableList) {
        tableView.setItems(tableList);
    }
    public void setPrtyName(String name) {
        try {
            String names = "FIRSTLETTER"+name.toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            seller = new SingleSellerDetails(names);
            partyNameLabel.setText(name);
        } catch (Exception ex) {
            Logger.getLogger(ShowBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setBillNo() {
        try {
            int billNo = seller.getLastBillNo()+1;
            billNoLabel.setText("" + String.valueOf(billNo) );
        } catch (SQLException ex) {
            Logger.getLogger(ShowBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setShowBillNo(String billNo) {
        billNoLabel.setText(billNo);
    }
    public void setDate(String date) {
        dateLabel.setText(date);
    }
    public void setLiner(String linerName) {
        linerNameLabel.setText(linerName);
    }
    public void setTruckNo(String truckNo) {
        truckNoLabel.setText(truckNo);
    }
    public void setCommission(String comm) {
        if(comm.equals("") || comm.equals("0")){
            commissionLabel.setVisible(false);
            commissionLabelT.setVisible(false);
        } else {
            commissionLabel.setText(comm);
            this.comm = Integer.parseInt(comm);
        }
    }
    public void setBoxExp(String boxExp) {
        if(boxExp.equals("") || boxExp.equals("0")) {
            boxExpLabelT.setVisible(false);
            boxExpLabel.setVisible(false);
        } else {
            boxExpLabel.setText(boxExp);
            this.boxExp = Integer.parseInt(boxExp);
        }
    }
    public void setTotalBox(String totalBox) {
        totalBoxLabel.setText(totalBox);
    }
    public void setMarketExp(String marketExp) {
        if(marketExp.equals("") || marketExp.equals("0")) {
            marketExpLabel.setVisible(false);
            marketExpLabelT.setVisible(false);
        } else {
            marketExpLabel.setText(marketExp);
            this.marketExp = Integer.parseInt(marketExp);
        }
    }
    public void setAdvCutt(String advCutt) {
        if(advCutt.equals("") || advCutt.equals("0")) {
            advCuttLabel.setVisible(false);
            advCuttLabelT.setVisible(false);
        } else {
            advCuttLabel.setText(advCutt);
            this.advCutt = Integer.parseInt(advCutt);
        }
    }
    public void setPercentCutt(String percentCutt) {
        if(percentCutt.equals("") || percentCutt.equals("0")) {
            percentCuttLabel.setVisible(false);
            percentCuttLabelT.setVisible(false);
        } else {
            percentCuttLabel.setText(percentCutt);
            this.percentCutt = Integer.parseInt(percentCutt);
        }
    }
    public void setIce(String ice) {
        if(ice.equals("") || ice.equals("0")) {
            iceLabel.setVisible(false);
            iceLabelT.setVisible(false);
        } else {
            iceLabel.setText(ice);
            this.ice = Integer.parseInt(ice);
        }
    }
    public void setPartyIce(String partyIce) {
        if(partyIce.equals("") || partyIce.equals("0")) {
            partyIceLabel.setVisible(false);
            partyIceLabelT.setVisible(false);
        } else {
            partyIceLabel.setText(partyIce);
            this.partyIce = Integer.parseInt(partyIce);
        }
    }
    public void setTruckRent(String truckRent) {
        if(truckRent.equals("") || truckRent.equals("0")) {
            truckRentLabel.setVisible(false);
            truckRentLabelT.setVisible(false);
        } else {
            truckRentLabel.setText(truckRent);
            this.truckRent = Integer.parseInt(truckRent);
        }
    }
    public void setPartyTruckRent(String partyTruckRent) {
        if(partyTruckRent.equals("") || partyTruckRent.equals("0")) {
            partyTruckRentLabel.setVisible(false);
            partyTruckRentLabelT.setVisible(false);
        } else {
            partyTruckRentLabel.setText(partyTruckRent);
            this.partyTruckRent = Integer.parseInt(partyTruckRent);
        }
    }
    public void setLinerAmt(String linerAmt) {
        if(linerAmt.equals("") || linerAmt.equals("0")) {
            linerAmtLabel.setVisible(false);
            linerAmtLabelT.setVisible(false);
        } else {
            linerAmtLabel.setText(linerAmt);
            this.linerAmt = Integer.parseInt(linerAmt);
        }
    }
    public void setOtherExp(String otherExp) {
        if(otherExp.equals("") || otherExp.equals("0")) {
            otherExpLabel.setVisible(false);
            otherExpLabelT.setVisible(false);
        } else {
            otherExpLabel.setText(otherExp);
            this.otherExp = Integer.parseInt(otherExp);
        }
    }
    public void setPartyExp(String partyExp) {
        if(partyExp.equals("") || partyExp.equals("0")) {
            partyExpLabel.setVisible(false);
            partyExpLabelT.setVisible(false);
        } else {
            partyExpLabel.setText(partyExp);
            this.partyExp = Integer.parseInt(partyExp);
        }
    }
    public void setTotalExp() {
        totalExp = comm+boxExp+marketExp+advCutt+percentCutt+ice+partyIce+truckRent+partyTruckRent+linerAmt+otherExp+partyExp;
        totalLabel.setText(totalExp+"");
        totalExpLabel.setText(totalExp+"");
        Long i = Long.parseLong(totalExpLabel.getText());
        totalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
        totalExpLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setShowbillTotalExp(String totalExp) {
        totalExp = totalExp.replaceAll(",", "");
        Long i = Long.parseLong(totalExp);
        totalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
        totalExpLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setTotalBillAmt(String totalBillAmt) {
        Long i = Long.parseLong(totalBillAmt);
        totalBillAmtLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setSubTotal() {
        subTotalLabel.setText( (Integer.parseInt(totalBillAmtLabel.getText().replaceAll("," ,"")) - totalExp) +"");
        Long i = Long.parseLong(subTotalLabel.getText().replaceAll("," ,""));
        subTotalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setShowbillSubTotal(String subTotal) {
        Long i = Long.parseLong(subTotal);
        subTotalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
        
    }
    public void setOldBalAndGrandTotal(String oldBal) {
        try {
            Long i = Long.parseLong(oldBal);
            oldBalanceLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
            Long grandTotal = Long.parseLong(subTotalLabel.getText().replaceAll("," ,""))+ Long.parseLong(oldBalanceLabel.getText().replaceAll("," ,""));
            i = Long.parseLong(grandTotal+"");
            grandTotalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
        } catch (Exception ex) {
            Logger.getLogger(ShowBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setShowbillOldBal(String oldBal) {
        Long i = Long.parseLong(oldBal);
        oldBalanceLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setShowbillGrandTotal(String grandTotal) {
        Long i = Long.parseLong(grandTotal);
        grandTotalLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setCashPaid(String cashPaid) {
        if(cashPaid.equals("") || cashPaid.equals("0")){
            Long i = Long.parseLong(grandTotalLabel.getText().replaceAll(",", ""));
            balanceLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
        }
        else {
            Long i = Long.parseLong(cashPaid);
            cashPaidLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
            i = Long.parseLong( (Long.parseLong(grandTotalLabel.getText().replaceAll("," ,"")) - Long.parseLong(cashPaid) )+"" );
            balanceLabel.setText( NumberFormat.getNumberInstance(Locale.US).format(i) );
        }
    }
    public void setShowbillCashPaid(String cashPaid) {
        Long i = Long.parseLong(cashPaid);
        cashPaidLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void setShowbillBalance(String balance) {
        Long i = Long.parseLong(balance);
        balanceLabel.setText(NumberFormat.getNumberInstance(Locale.US).format(i));
    }
    public void saveToDb() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
            SimpleDateFormat df = new SimpleDateFormat( "dd-MM-yyy" );
            Calendar cal = Calendar.getInstance();
            cal.setTime( df.parse( dateLabel.getText() ) );
            String date = dateFormat.format(cal.getTime());
            seller.insertBillDetails(
                    linerNameLabel.getText(), truckNoLabel.getText(), date, billNoLabel.getText(),commissionLabel.getText(),
                    boxExpLabel.getText(), totalBoxLabel.getText(), marketExpLabel.getText(),
                    advCuttLabel.getText(), percentCuttLabel.getText(), iceLabel.getText(), partyIceLabel.getText(),
                    truckRentLabel.getText(),partyTruckRentLabel.getText(),
                    linerAmtLabel.getText(), otherExpLabel.getText(), partyExpLabel.getText(), totalBillAmtLabel.getText().replaceAll("," ,""),
                    totalExpLabel.getText(), subTotalLabel.getText().replaceAll("," ,""), oldBalanceLabel.getText().replaceAll("," ,""), grandTotalLabel.getText().replaceAll("," ,""),
                    cashPaidLabel.getText().replaceAll("," ,""), balanceLabel.getText().replaceAll("," ,"")
            );
            for (Iterator it = tableView.getItems().iterator(); it.hasNext();) {
                BillTableModel item = (BillTableModel) it.next();
                seller.insertFishDetails(item.getFishName(), item.getBoxCount(), item.getUnitPrice(), item.getRs(), billNoLabel.getText());
            }
            if(advCuttLabel.isVisible())
                seller.setDuplicateAdvance(advCuttLabel.getText(), partyNameLabel.getText());
            if(percentCuttLabel.isVisible())
                seller.setTotalPercentCutt(percentCuttLabel.getText(), partyNameLabel.getText());
            close();
        } catch (Exception ex) {
            Logger.getLogger(ShowBillController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void saveToMarketing(String date, String sellersName, String totalBox, String billAmt, String soldBox, String saleAmt, String remainingBox ) {
        
    }
    public void close(){
        seller.close();
    }
    
    @FXML Label partyNameLabel, dateLabel, linerNameLabel, truckNoLabel;
    @FXML Label commissionLabel, boxExpLabel, marketExpLabel, advCuttLabel;
    @FXML Label commissionLabelT, boxExpLabelT, marketExpLabelT, advCuttLabelT;
    @FXML Label percentCuttLabel, iceLabel, partyIceLabel, partyTruckRentLabel, truckRentLabel, otherExpLabel, partyExpLabel;
    @FXML Label percentCuttLabelT, iceLabelT, partyIceLabelT, partyTruckRentLabelT, truckRentLabelT, otherExpLabelT, partyExpLabelT;
    @FXML Label totalLabel, totalExpLabel, totalBillAmtLabel, subTotalLabel, totalBoxLabel;
    @FXML Label oldBalanceLabel, grandTotalLabel, billNoLabel, cashPaidLabel, balanceLabel;
    @FXML Label linerAmtLabel, linerAmtLabelT;
    @FXML TableView tableView;
    private int comm=0, boxExp=0, marketExp=0, advCutt=0, percentCutt=0, ice=0, partyIce=0, truckRent=0, partyTruckRent=0, linerAmt=0, otherExp=0, partyExp=0; 
    private int totalExp = 0;
    private SingleSellerDetails seller;
    
}