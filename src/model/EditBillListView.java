/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import backend.SingleSellerDetails;
import controller.edit.editbill.EditBillingModelController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.alert.ErrorMessage;
import model.alert.InfomationPopupWithMsg;

/**
 *
 * @author USER
 */
public class EditBillListView extends HBox {
    Label nameLabel = new Label();
    Label totalBillLabel = new Label();
    Label cashPaidLabel = new Label();
    Label idLabel = new Label();
    Button editBtn =  new Button("edit");
    Button rmvBtn =  new Button("remove");
    Stage stage;
    
    public EditBillListView(String id, String name, String billAmt, String cashPaid, Stage stage) {
        this.stage = stage;
        nameLabel.setText(name);
        totalBillLabel.setText(billAmt);
        cashPaidLabel.setText(cashPaid);
        idLabel.setText(id);
        
        nameLabel.setPrefWidth(237);
        totalBillLabel.setPrefWidth(106);
        cashPaidLabel.setPrefWidth(106);
        nameLabel.setMaxWidth(250);
        totalBillLabel.setMaxWidth(120);
        cashPaidLabel.setMaxWidth(120);
        
        nameLabel.setAlignment(Pos.CENTER);
        totalBillLabel.setAlignment(Pos.CENTER);
        cashPaidLabel.setAlignment(Pos.CENTER);
        
//        setSpacing(10);
        getChildren().addAll(nameLabel, totalBillLabel, cashPaidLabel, editBtn, rmvBtn);
        setSpacing(3);
        editBtnAction();
        rmvBtnAction();
    }
    private void loadEditBill() {
        Stage viewLossAndProfit = new Stage();
            viewLossAndProfit.setTitle("Edit Bill");
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/edit/editbill/EditBillingModel.fxml"));
                Pane myPane = (Pane)loader.load();
                EditBillingModelController controller = loader.getController();
                controller.setItems(nameLabel.getText(), idLabel.getText());
                Scene myScene = new Scene(myPane);
                viewLossAndProfit.setScene(myScene);
                viewLossAndProfit.setMaximized(false);
                viewLossAndProfit.setAlwaysOnTop(true);
    //            bill.setWidth(diamension.getWidth()/3.5);
    //            bill.setHeight(diamension.getHeight()/1.95);
                viewLossAndProfit.show();
            }catch (Exception ex) { System.out.println(ex); }
    }
    private void editBtnAction() {
        editBtn.setOnAction(e->{
           loadEditBill();
        });
        editBtn.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER) {
                loadEditBill();
            }
        });
    }
    private void remove() {
        try {
            String name = "FIRSTLETTER"+nameLabel.getText().toUpperCase().replaceAll("\\s+", "_").replaceAll("/", "FORWARDSLASH").replaceAll("\\.", "TMPDOT");
            SingleSellerDetails seller = new SingleSellerDetails(name);
            if(!seller.checkSales(idLabel.getText())) {
                seller.removeBill(idLabel.getText());
                InfomationPopupWithMsg msg = new InfomationPopupWithMsg("Removed", "Bill Removed");
                msg.initOwner(stage);
                msg.showAndWait();
            } else {
                ErrorMessage err = new ErrorMessage("This bill contain sales details");
                err.initOwner(stage);
                err.showAndWait();
            }
            seller.close();
        } catch (Exception ex) {
            Logger.getLogger(EditBillListView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void rmvBtnAction() {
        rmvBtn.setOnAction(e->{
            remove();
        });
        rmvBtn.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER)
                remove();
        });
    }
}
