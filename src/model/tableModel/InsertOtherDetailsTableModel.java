/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.tableModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author BCz
 */
public class InsertOtherDetailsTableModel {
   
    private StringProperty BuyerName, BoxCount, BillAmt, CashPaid, Id;

    public InsertOtherDetailsTableModel(String buyerName,String boxCount, String billAmt, String CashPaid, String id){
        this.BuyerName = new SimpleStringProperty(buyerName);
        this.BoxCount = new SimpleStringProperty(boxCount);
        this.BillAmt = new SimpleStringProperty(billAmt);
        this.CashPaid = new SimpleStringProperty(CashPaid);
        this.Id = new SimpleStringProperty(id);
        
    }
    public String getBuyerName(){
        return BuyerName.get();
    }
    public void setBuyerName(String buyerName){
        BuyerName.set(buyerName);
    }
    
    public String getBoxCount(){
        return BoxCount.get();
    }
    public void setBoxCount(String boxCount){
        BoxCount.set(boxCount);
    }
    
    public String getBillAmt() {
        return BillAmt.get();
    }
    public void setBillAmt(String billAmt) {
        BillAmt.set(billAmt);
    }
    
    public String getCashPaid() {
        return CashPaid.get();
    }
    public void setCashPaid(String cashPaid) {
        CashPaid.set(cashPaid);
    }
    
    public String getId() {
        return Id.get();
    }
    public void setId(String id) {
        Id.set(id);
    }
}