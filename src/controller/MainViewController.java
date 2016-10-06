/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import backend.DBAccess;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Haxx
 */
public class MainViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        diamension = Screen.getPrimary().getVisualBounds();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                stage = (Stage) menuBar.getScene().getWindow();
            }
        });
    }    
    
    @FXML public void newBill() {
        Stage bill = new Stage();
        bill.setTitle("Create Bill");
        bill.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertBill.fxml"));
            Scene myScene = new Scene(myPane);
            bill.setScene(myScene);
            bill.setMaximized(false);
            bill.setMinHeight(450);
            bill.setMinWidth(450);
            bill.centerOnScreen();
            bill.initOwner(stage);
            bill.show();
        }catch (Exception e) { 
//            System.out.println(e); 
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @FXML public void billDetails() {
        Stage otherDetails = new Stage();
        otherDetails.setTitle("Add Sales");
        otherDetails.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/AddSales.fxml"));
            Scene myScene = new Scene(myPane);
            otherDetails.setScene(myScene);
            otherDetails.setMaximized(false);
            otherDetails.setMinHeight(450);
            otherDetails.setMinWidth(450);
            otherDetails.initOwner(stage);
            otherDetails.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void insertExpence() {
        Stage insertExpence = new Stage();
        insertExpence.setTitle("Insert Expence");
        insertExpence.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertExpence.fxml"));
            Scene myScene = new Scene(myPane);
            insertExpence.setScene(myScene);
            insertExpence.setMaximized(false);
            insertExpence.initOwner(stage);
            insertExpence.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void insertIncome() {
        Stage insertIncome = new Stage();
        insertIncome.setTitle("Insert Income");
        insertIncome.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertIncome.fxml"));
            Scene myScene = new Scene(myPane);
            insertIncome.setScene(myScene);
            insertIncome.setMaximized(false);
            insertIncome.initOwner(stage);
            insertIncome.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void insertOutstanding() {
        Stage insertOutstanding = new Stage();
        insertOutstanding.setTitle("Insert Outstanding");
        insertOutstanding.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertOutstanding.fxml"));
            Scene myScene = new Scene(myPane);
            insertOutstanding.setScene(myScene);
            insertOutstanding.setMaximized(false);
            insertOutstanding.initOwner(stage);
            insertOutstanding.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void insertInvestment() {
        Stage insertInvestment = new Stage();
        insertInvestment.setTitle("Insert InvestmentGiven");
        insertInvestment.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertInvestment.fxml"));
            Scene myScene = new Scene(myPane);
            insertInvestment.setScene(myScene);
            insertInvestment.setMaximized(false);
            insertInvestment.setAlwaysOnTop(true);
            insertInvestment.initOwner(stage);
            insertInvestment.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void insertInvestmentDept() {
        Stage insertInvestmentDept = new Stage();
        insertInvestmentDept.setTitle("Insert InvestmentTaken");
        insertInvestmentDept.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/insert/InsertInvestmentDept.fxml"));
            Scene myScene = new Scene(myPane);
            insertInvestmentDept.setScene(myScene);
            insertInvestmentDept.setMaximized(false);
            insertInvestmentDept.initOwner(stage);
            insertInvestmentDept.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewShowBill() {
        Stage viewShowBill = new Stage();
        viewShowBill.setTitle("Individual Seller");
        viewShowBill.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/ShowInsertedBill.fxml"));
            Scene myScene = new Scene(myPane);
            viewShowBill.setScene(myScene);
            viewShowBill.setMaximized(false);
            viewShowBill.initOwner(stage);
            viewShowBill.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewSalesReport() {
        Stage viewSalesReport = new Stage();
        viewSalesReport.setTitle("Sales Report");
        viewSalesReport.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/SalesReport.fxml"));
            Scene myScene = new Scene(myPane);
            viewSalesReport.setScene(myScene);
            viewSalesReport.setMaximized(false);
            viewSalesReport.initOwner(stage);
            viewSalesReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewIndividualSeller() {
        Stage viewIndividualParty = new Stage();
        viewIndividualParty.setTitle("Individual Party");
        viewIndividualParty.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/IndividualParty.fxml"));
            Scene myScene = new Scene(myPane);
            viewIndividualParty.setScene(myScene);
            viewIndividualParty.setMaximized(false);
            viewIndividualParty.initOwner(stage);
            viewIndividualParty.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewAllSeller() {
        Stage viewAllBuyersReport = new Stage();
        viewAllBuyersReport.setTitle("All Seller Report");
        viewAllBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllSellerReport.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewDayView() {
        Stage viewDayView = new Stage();
        viewDayView.setTitle("DayView");
        viewDayView.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/DayView.fxml"));
            Scene myScene = new Scene(myPane);
            viewDayView.setScene(myScene);
            viewDayView.setMaximized(false);
            viewDayView.initOwner(stage);
            viewDayView.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewAdvAndPercent() {
        Stage viewAdvAndPercent = new Stage();
        viewAdvAndPercent.setTitle("Adv and Percent Cutting");
        viewAdvAndPercent.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AdvCuttingAndPercentCutting.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewAllBuyersReport() {
        Stage viewAllBuyersReport = new Stage();
        viewAllBuyersReport.setTitle("All Buyers Report");
        viewAllBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllBuyerReport.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewIndividualBuyersReport() {
        Stage viewIndividualBuyersReport = new Stage();
        viewIndividualBuyersReport.setTitle("Individual Buyers Report");
        viewIndividualBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/IndividualBuyersReport.fxml"));
            Scene myScene = new Scene(myPane);
            viewIndividualBuyersReport.setScene(myScene);
            viewIndividualBuyersReport.setMaximized(false);
            viewIndividualBuyersReport.initOwner(stage);
            viewIndividualBuyersReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewCapitalAdvDetails() {
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllCapitalAdvanceDetails.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewBalanceSheet() {
        try{
            DBAccess db = new DBAccess();
            db.updateBalanceSheet();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\FinalBalanceSheet.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        } catch (Exception ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML public void viewDayBook() {
        Stage viewDayBook = new Stage();
        viewDayBook.setTitle("Day Book");
        viewDayBook.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/DayBook.fxml"));
            Scene myScene = new Scene(myPane);
            viewDayBook.setScene(myScene);
            viewDayBook.setMaximized(false);
            viewDayBook.initOwner(stage);
            viewDayBook.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewOutstandingReport() {
        Stage outstandinReport = new Stage();
        outstandinReport.setTitle("Outstanding Report");
        outstandinReport.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/OutstandingReport.fxml"));
            Scene myScene = new Scene(myPane);
            outstandinReport.setScene(myScene);
            outstandinReport.setMaximized(false);
            outstandinReport.initOwner(stage);
            outstandinReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewAllOutstanding() {
        Stage viewAllBuyersReport = new Stage();
        viewAllBuyersReport.setTitle("All Outstanding Report");
        viewAllBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllOutstandingDetails.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewLossAndProfit() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Loss and Profit");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/display/LossAndProfit.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewInvestment() {
        Stage viewAllBuyersReport = new Stage();
        viewAllBuyersReport.setTitle("InvestmentGive");
        viewAllBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllInvestment.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void viewInvestmentDept() {
        Stage viewAllBuyersReport = new Stage();
        viewAllBuyersReport.setTitle("Investment Taken");
        viewAllBuyersReport.getIcons().add(new Image("/image/aas.png"));
        try{
            DBAccess db = new DBAccess();
            HashMap param = new HashMap();
            JasperPrint print = JasperFillManager.fillReport("src\\reports\\AllInvestmentDept.jasper", param, db.connection);
            db.close();
            JRViewer viewer = new JRViewer(print);
            SwingNode node = new SwingNode();
            node.setContent(viewer);
            BorderPane jasperViewer = new BorderPane();
            jasperViewer.setCenter(node);
            Scene scene = new Scene(jasperViewer);
            Stage jasperReport = new Stage();
            jasperReport.setScene(scene);
            jasperReport.initModality(Modality.NONE);
            jasperReport.initOwner(stage);
            jasperReport.getIcons().add(new Image("/image/aas.png"));
            jasperReport.setHeight(700); jasperReport.setResizable(false); jasperReport.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editIncome() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Income Details");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditIncome.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editExpence() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Expence Details");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditExpence.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editOutstandingData() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Outstander Data");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/data/EditOutstanderDatas.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editInvestmentData() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit InvestmentGiven Data");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/data/EditInvestmentData.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editInvestmentDeptData() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit InvestmentTaken Data");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/data/EditInvestmentDeptData.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editSellers() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Seller");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditSeller.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editBuyers() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Buyers");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditBuyer.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editOutstander() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit OutStander");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditOutstanding.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editInvestment() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit InvestmentGiven Account");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditInvestment.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editInvestmentDept() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit InvestmentTaken Account");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditInvestmentDept.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editBill() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Bill");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditBill.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editGroup() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Group");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditGroup.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML public void editFish() {
        Stage viewLossAndProfit = new Stage();
        viewLossAndProfit.setTitle("Edit Fish");
        viewLossAndProfit.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/edit/EditFish.fxml"));
            Scene myScene = new Scene(myPane);
            viewLossAndProfit.setScene(myScene);
            viewLossAndProfit.setMaximized(false);
            viewLossAndProfit.centerOnScreen();
            viewLossAndProfit.initOwner(stage);
            viewLossAndProfit.show();
        }catch (Exception e) { System.out.println(e); }
    }
    
    @FXML MenuItem about;
    @FXML MenuBar menuBar;
    private Rectangle2D diamension;
    private Stage stage;
}
