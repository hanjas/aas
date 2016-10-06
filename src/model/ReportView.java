/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import backend.DBAccess;
import java.awt.Label;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author BCz
 */
public class ReportView extends BorderPane {

    public ReportView() {
        try {
            DBAccess db = new DBAccess();
            ok = new Button("Ok");
            ok.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    try {
                        InputStream input = new FileInputStream("src/reports/Sellers.jrxml");
                        System.out.println("done");
                        JasperDesign jasperDesign = JRXmlLoader.load(input);
                        JasperReport jr = JasperCompileManager.compileReport(jasperDesign);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(jr, null, db.connection);
                        JasperViewer jasperViewer = new JasperViewer(jasperPrint,false);
                        jasperViewer.setTitle("Sellers Report");
                        jasperViewer.setVisible(true);
                    } catch(Exception e) {System.out.println("this"+e);}
                }
            });
        } catch(Exception e) {System.out.println(e);}
        setTop(ok);
    }
    
    private Button ok;
    private Label linerNameLabel,truckNoLabel, billNoLabel, dateLabel, partyNameField;
    private TableView tableView;
}
