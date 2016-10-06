/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.edit;

import backend.DBAccess;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.ListWithDeleteAndRemoveBtn;

/**
 * FXML Controller class
 *
 * @author USER
 */
public class EditInvestmentDeptController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list = FXCollections.observableArrayList();
        refreshItems();
        setItems();
        searchFieldAction();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myStage = (Stage) createBtn.getScene().getWindow();
                myStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, evt->{
                    if( evt.getCode() == KeyCode.F1 ) {
                        createInvestmentDept();
                    }
                });
            }
        });
    } 
    public void searchFieldAction() {
        searchField.textProperty().addListener(obs->{
            String filter = searchField.getText(); 
            if(filter != null || filter.length() != 0) {
                filterItems(filter.toUpperCase());
            }
        });
    }
    public void filterItems(String searchName) {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getInvestmentDeptNames();
            FilteredList<String> filteredData = new FilteredList<>(list, s -> true);
            filteredData.setPredicate(s -> s.contains(searchName));
            List<ListWithDeleteAndRemoveBtn> arrayList = new ArrayList<>();
            for(Object name : filteredData) {
                ListWithDeleteAndRemoveBtn listBtn = new ListWithDeleteAndRemoveBtn(name.toString(), "investmentDept", this);
                arrayList.add(listBtn);
            }
            ObservableList<ListWithDeleteAndRemoveBtn> myObservableList = FXCollections.observableList(arrayList);
            listView.setItems(myObservableList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void refreshItems() {
        list.clear();
        try {
            DBAccess db = new DBAccess();
            list = db.getInvestmentDeptNames();
            List<ListWithDeleteAndRemoveBtn> arrayList = new ArrayList<>();
            for(Object name : list) {
                ListWithDeleteAndRemoveBtn listBtn = new ListWithDeleteAndRemoveBtn(name.toString(), "investmentDept", this);
                arrayList.add(listBtn);
            }
            myObservableList = FXCollections.observableList(arrayList);
            db.close();
        } catch (Exception ex) {
            Logger.getLogger(EditSellerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setItems() {
        searchField.clear();
        listView.setItems(myObservableList);
    }
    @FXML public void createInvestmentDept() {
        Stage outstanding = new Stage();
        outstanding.setTitle("Create InvestmentTaken");
        outstanding.getIcons().add(new Image("/image/aas.png"));
        try{
            Pane myPane = (Pane)FXMLLoader.load(getClass().getResource("/view/CreateInvestmentDept.fxml"));
            Scene myScene = new Scene(myPane);
            outstanding.setScene(myScene);
            outstanding.setMaximized(false);
            outstanding.setMinWidth(370);
            outstanding.setMinHeight(250);
            outstanding.initOwner(myStage);
            outstanding.showAndWait();
            refreshItems();
            setItems();
        }catch (Exception e) { System.out.println(e); }
    }
    @FXML public void createInvestmentDeptOnKeyPress(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER)
            createInvestmentDept();
    }
    
    @FXML ListView<ListWithDeleteAndRemoveBtn> listView;
    @FXML TextField searchField;
    @FXML Button createBtn;
    private Stage myStage;
    private ObservableList list;
    ObservableList<ListWithDeleteAndRemoveBtn> myObservableList;
}