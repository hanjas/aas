/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.edit;

import backend.DBAccess;
import controller.insertControllers.InsertOutstandingController;
import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.SavedPopup;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class SingleSellerEditController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setName(String name) {
        nameField.setText(name);
        this.name = name;
    }
    public void setToGet(String toGet) {
        toGetField.setText(toGet);
        this.toGet = toGet;
    }
    public void setToGive(String toGive) {
        toGiveField.setText(toGive);
        this.toGive = toGive;
    }
    public void setComm(String comm) {
        commField.setText(comm);
        this.comm = comm;
    }
    public void setPercent(String percent) {
        percentCuttingField.setText(percent);
        this.percentCutt = percent;
    }
    public void setAdvance(String advance) {
        advCuttingField.setText(advance);
        this.advCutting = advance;
    }
    public void setBoxExp(String boxExp) {
        boxExpField.setText(boxExp);
        this.boxExp = boxExp;
    }
    public void setGroup(String group) {
        groupField.setText(group);
        this.group = group;
    }
    public void setBankName(String bankName) {
        bankNameField.setText(bankName);
        this.bankName = bankName;
    }
    public void setAcNo(String acNo) {
        acNoField.setText(acNo);
        this.acno = acNo;
    }
    public void setBranch(String branch) {
        branchField.setText(branch);
        this.branch = branch;
    }
    public void setIfcCode(String ifcCode) {
        ifcCodeField.setText(ifcCode);
        this.ifcCode = ifcCode;
    }
    @FXML private void tabKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(com.sun.glass.events.KeyEvent.VK_TAB);
        } catch (AWTException ex) {
            Logger.getLogger(InsertOutstandingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML private void saveAndClose(KeyEvent event) throws Exception {
        if(event.getCode() == KeyCode.ENTER){
            okBtnOnMouse();
        }
    }
    public void okBtnOnMouse() throws Exception {
        if(checkItems()) {
            DBAccess db = new DBAccess();
            db.editSeller(this.name,
                    nameField.getText().toUpperCase(),
                    toGetField.getText(),
                    toGiveField.getText(),
                    commField.getText(),
                    percentCuttingField.getText(),
                    advCuttingField.getText(),
                    boxExpField.getText(),
                    groupField.getText(),
                    bankNameField.getText(),
                    acNoField.getText(),
                    branchField.getText(),
                    ifcCodeField.getText()
            );
            db.close();
        }
        Stage stage = (Stage) okBtn.getScene().getWindow();
        SavedPopup popup = new SavedPopup();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(stage);
        popup.showAndWait();
        stage.close();
    }
    public boolean checkItems() {
        if(
            nameField.getText().toUpperCase().equals(name) &&
                toGetField.getText().equals(toGet) &&
                toGiveField.getText().equals(toGive) &&
                commField.getText().equals(comm) &&
                percentCuttingField.getText().equals(percentCutt) &&
                advCuttingField.getText().equals(advCutting) &&
                boxExpField.getText().equals(boxExp) &&
                groupField.getText().equals(group) &&
                bankNameField.getText().equals(bankName) &&
                acNoField.getText().equals(acno) &&
                branchField.getText().equals(branch) &&
                ifcCodeField.getText().equals(ifcCode)
                ) {
            return false;
        }
        return true;
    }
    @FXML private void cancel(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER){
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.close();
        }
    }
    @FXML private void cancelOnMouse() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    private String name, toGet, toGive, comm, percentCutt, advCutting, boxExp, group, bankName, acno, branch, ifcCode;
    @FXML TextField nameField, toGetField, toGiveField,
            commField, percentCuttingField, boxExpField, advCuttingField,
            groupField, bankNameField, acNoField, branchField, ifcCodeField;
    @FXML Button okBtn, cancelBtn;
    
}
