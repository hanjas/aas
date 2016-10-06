/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import backend.DBAccess;
import controller.edit.EditBuyerController;
import controller.edit.EditInvestmentController;
import controller.edit.EditInvestmentDeptController;
import controller.edit.EditOutstandingController;
import controller.edit.EditSellerController;
import controller.edit.SiingleInvestmentDeptEditController;
import controller.edit.SingleBuyerEditController;
import controller.edit.SingleInvestmentEditController;
import controller.edit.SingleOutstandingEditController;
import controller.edit.SingleSellerEditController;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.alert.ConfirmDelete;
import model.alert.DeletedPopup;
import model.alert.ErrorMessage;

/**
 *
 * @author USER
 */
public class ListWithDeleteAndRemoveBtn extends HBox {
    Label nameLabel = new Label();
    public Button editBtn = new Button("Edit");
    public Button rmvBtn = new Button("Remove");
    private String sellerOrBuyer="";
    public EditSellerController sellerController;
    public EditBuyerController buyerController;
    public EditOutstandingController outstandingController;
    public EditInvestmentController investmentController;
    public EditInvestmentDeptController investmentDeptController;

    public ListWithDeleteAndRemoveBtn(String name, String sellerOrBuyer, Object controller) {
        super();
        nameLabel.setText(name); 
        nameLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(nameLabel, Priority.ALWAYS);
        this.getChildren().addAll(nameLabel, editBtn, rmvBtn);
        setSpacing(5);
        editBtnAction(sellerOrBuyer);
        deleteBtnAction(sellerOrBuyer);
        this.sellerOrBuyer = sellerOrBuyer;
        if(sellerOrBuyer.equals("seller"))
            this.sellerController = (EditSellerController) controller;
        if(sellerOrBuyer.equals("buyer"))
            this.buyerController = (EditBuyerController) controller;
        if(sellerOrBuyer.equals("outstander"))
            this.outstandingController = (EditOutstandingController) controller;
        if(sellerOrBuyer.equals("investment"))
            this.investmentController = (EditInvestmentController) controller;
        if(sellerOrBuyer.equals("investmentDept"))
            this.investmentDeptController = (EditInvestmentDeptController) controller;
    }
    private void editBtnAction(String type) {
        if(type.equals("seller")) {
            editBtn.setOnAction(e->{
                Stage seller = new Stage();
                seller.setTitle("Create Seller");
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/SingleSellerEdit.fxml"));
                    Pane myPane = (Pane)loader.load();
                    SingleSellerEditController ss = loader.getController();
                    DBAccess db = new DBAccess();
                    ResultSet rs = db.getSellersDetails(nameLabel.getText());
                    while(rs.next()) {
                        ss.setAcNo(rs.getString("AcNo"));
                        ss.setBankName(rs.getString("BankName"));
                        ss.setBoxExp(rs.getString("boxExp"));
                        ss.setBranch(rs.getString("branch"));
                        ss.setComm(rs.getString("comm"));
                        ss.setGroup(rs.getString("groupe"));
                        ss.setIfcCode(rs.getString("ifcCode"));
                        ss.setName(rs.getString("Name"));
                        ss.setAdvance(rs.getString("advCutting"));
                        ss.setPercent(rs.getString("PercentCutting"));
                        ss.setToGet(rs.getString("OpBalToGet"));
                        ss.setToGive(rs.getString("OpBalToGive"));
                    }
                    db.close();
                    Scene myScene = new Scene(myPane);
                    seller.setScene(myScene);
                    seller.setMaximized(false);
                    seller.setAlwaysOnTop(true);
                    seller.showAndWait();
                    sellerController.refreshItems();
                    sellerController.setItems();
                }catch (Exception ex) { System.out.println(ex); }
            });
        }
        if(type.equals("buyer")) {
            editBtn.setOnAction(e->{
                Stage seller = new Stage();
                seller.setTitle("Create Buyer");
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/SingleBuyerEdit.fxml"));
                    Pane myPane = (Pane)loader.load();
                    SingleBuyerEditController ss = loader.getController();
                    DBAccess db = new DBAccess();
                    ResultSet rs = db.getBuyerDetails(nameLabel.getText());
                    while(rs.next()) {
                        ss.setAcNo(rs.getString("AcNo"));
                        ss.setBankName(rs.getString("BankName"));
                        ss.setBranch(rs.getString("branch"));
                        ss.setGroup(rs.getString("groupe"));
                        ss.setIfcCode(rs.getString("ifcCode"));
                        ss.setName(rs.getString("Name"));
                        ss.setToGet(rs.getString("OpBalToGet"));
                        ss.setToGive(rs.getString("OpBalToGive"));
                    }
                    db.close();
                    Scene myScene = new Scene(myPane);
                    seller.setScene(myScene);
                    seller.setMaximized(false);
                    seller.setAlwaysOnTop(true);
                    seller.showAndWait();
                    buyerController.refreshItems();
                    buyerController.setItems();
                }catch (Exception ex) { System.out.println(ex); }
            });
        }
        if(type.equals("outstander")) {
            editBtn.setOnAction(e->{
                Stage seller = new Stage();
                seller.setTitle("Create outstander");
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/SingleOutstandingEdit.fxml"));
                    Pane myPane = (Pane)loader.load();
                    SingleOutstandingEditController ss = loader.getController();
                    DBAccess db = new DBAccess();
                    ResultSet rs = db.getOutstandingDetails(nameLabel.getText());
                    while(rs.next()) {
                        ss.setGroup(rs.getString("groupe"));
                        ss.setName(rs.getString("Name"));
                        ss.setToGet(rs.getString("OpBalToGet"));
                        ss.setToGive(rs.getString("OpBalToGive"));
                    }
                    db.close();
                    Scene myScene = new Scene(myPane);
                    seller.setScene(myScene);
                    seller.setMaximized(false);
                    seller.setAlwaysOnTop(true);
                    seller.showAndWait();
                    outstandingController.refreshItems();
                    outstandingController.setItems();
                }catch (Exception ex) { System.out.println(ex); }
            });
        }
        if(type.equals("investment")) {
            editBtn.setOnAction(e->{
                Stage seller = new Stage();
                seller.setTitle("Create Investment Accounts");
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/SingleInvestmentEdit.fxml"));
                    Pane myPane = (Pane)loader.load();
                    SingleInvestmentEditController ss = loader.getController();
                    DBAccess db = new DBAccess();
                    ResultSet rs = db.getInvestmentDetails(nameLabel.getText());
                    while(rs.next()) {
                        ss.setGroup(rs.getString("groupe"));
                        ss.setName(rs.getString("Name"));
                        ss.setToGet(rs.getString("OpBalToGet"));
                        ss.setToGive(rs.getString("OpBalToGive"));
                    }
                    db.close();
                    Scene myScene = new Scene(myPane);
                    seller.setScene(myScene);
                    seller.setMaximized(false);
                    seller.setAlwaysOnTop(true);
                    seller.showAndWait();
                    investmentController.refreshItems();
                    investmentController.setItems();
                }catch (Exception ex) { System.out.println(ex); }
            });
        }
        if(type.equals("investmentDept")) {
            editBtn.setOnAction(e->{
                Stage seller = new Stage();
                seller.setTitle("Edit Investment Taken Account");
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/SiingleInvestmentDeptEdit.fxml"));
                    Pane myPane = (Pane)loader.load();
                    SiingleInvestmentDeptEditController ss = loader.getController();
                    DBAccess db = new DBAccess();
                    ResultSet rs = db.getInvestmentDeptDetails(nameLabel.getText());
                    while(rs.next()) {
                        ss.setGroup(rs.getString("groupe"));
                        ss.setName(rs.getString("Name"));
                        ss.setToGet(rs.getString("OpBalToGet"));
                        ss.setToGive(rs.getString("OpBalToGive"));
                    }
                    db.close();
                    Scene myScene = new Scene(myPane);
                    seller.setScene(myScene);
                    seller.setMaximized(false);
                    seller.setAlwaysOnTop(true);
                    seller.showAndWait();
                    investmentDeptController.refreshItems();
                    investmentDeptController.setItems();
                }catch (Exception ex) { System.out.println(ex); }
            });
        }
    }
    private void deleteBtnAction(String type) {
        if(type.equals("seller")) {
            rmvBtn.setOnAction(e->{
                
                ConfirmDelete deletePopup = new ConfirmDelete();
                Stage stage = (Stage) rmvBtn.getScene().getWindow();
                deletePopup.initOwner(stage);
                deletePopup.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            boolean success = false;
                            DBAccess db = new DBAccess();
                                if(!db.sellerInserted(nameLabel.getText())) {
                                    db.deleteSeller(nameLabel.getText());
                                    success = true;
                                }
                            db.close();
                            showPopup(success);
                        } catch (Exception ex) {
                            Logger.getLogger(ListWithDeleteAndRemoveBtn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            });
        }
        if(type.equals("buyer")) {
            rmvBtn.setOnAction(e->{
                ConfirmDelete deletePopup = new ConfirmDelete();
                Stage stage = (Stage) rmvBtn.getScene().getWindow();
                deletePopup.initOwner(stage);
                deletePopup.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            boolean success = false;
                            DBAccess db = new DBAccess();
                            if(!db.buyerInserted(nameLabel.getText())) {
                                db.deleteBuyer(nameLabel.getText());
                                success = true;
                                }
                            db.close();
                            showPopup(success);
                        } catch (Exception ex) {
                            Logger.getLogger(ListWithDeleteAndRemoveBtn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            });
        }

        if(type.equals("outstander")) {
            rmvBtn.setOnAction(e->{
                ConfirmDelete deletePopup = new ConfirmDelete();
                Stage stage = (Stage) rmvBtn.getScene().getWindow();
                deletePopup.initOwner(stage);
                deletePopup.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            boolean success = false;
                            DBAccess db = new DBAccess();
                            if(!db.outstanderInserted(nameLabel.getText())) {
                                db.deleteOutstanding(nameLabel.getText());
                                success = true;
                                }
                            db.close();
                            showPopup(success);
                        } catch (Exception ex) {
                            Logger.getLogger(ListWithDeleteAndRemoveBtn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            });

        }
        
        if(type.equals("investment")) {
            rmvBtn.setOnAction(e->{
                ConfirmDelete deletePopup = new ConfirmDelete();
                Stage stage = (Stage) rmvBtn.getScene().getWindow();
                deletePopup.initOwner(stage);
                deletePopup.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            boolean success = false;
                            DBAccess db = new DBAccess();
                            if(!db.InvestmentInserted(nameLabel.getText())) {
                                db.deleteInvestment(nameLabel.getText());
                                success = true;
                                }
                            db.close();
                            showPopup(success);
                        } catch (Exception ex) {
                            Logger.getLogger(ListWithDeleteAndRemoveBtn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            });

        }
        
        if(type.equals("investmentDept")) {
            rmvBtn.setOnAction(e->{
                ConfirmDelete deletePopup = new ConfirmDelete();
                Stage stage = (Stage) rmvBtn.getScene().getWindow();
                deletePopup.initOwner(stage);
                deletePopup.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        try {
                            boolean success = false;
                            DBAccess db = new DBAccess();
                            if(!db.InvestmentDeptInserted(nameLabel.getText())) {
                                db.deleteInvestmentDept(nameLabel.getText());
                                success = true;
                                }
                            db.close();
                            showPopup(success);
                        } catch (Exception ex) {
                            Logger.getLogger(ListWithDeleteAndRemoveBtn.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            });

        }
    }
    private void showPopup(boolean success) {
        Stage stage = (Stage) rmvBtn.getScene().getWindow();
        if(success) {
            DeletedPopup popup = new DeletedPopup();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(stage);
            popup.showAndWait();
            stage.close();
        } else {
            ErrorMessage popup = new ErrorMessage("There are some data in this account");
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(stage);
            popup.showAndWait();
        }
        if(sellerOrBuyer.equals("seller")){
            sellerController.refreshItems();
            stage.close();
            sellerController.setItems();
        }
        if(sellerOrBuyer.equals("buyer")){
            buyerController.refreshItems();
            stage.close();
            buyerController.setItems();
        }
        if(sellerOrBuyer.equals("outstander")){
            outstandingController.refreshItems();
            stage.close();
            outstandingController.setItems();
        }
        if(sellerOrBuyer.equals("investment")){
            investmentController.refreshItems();
            stage.close();
            investmentController.setItems();
        }
        if(sellerOrBuyer.equals("investmentDept")){
            investmentDeptController.refreshItems();
            stage.close();
            investmentDeptController.setItems();
        }
        stage.show();
    }
}