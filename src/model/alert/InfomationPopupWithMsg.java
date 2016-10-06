/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.alert;

import javafx.scene.control.Alert;

/**
 *
 * @author USER
 */
public class InfomationPopupWithMsg extends Alert {

    public InfomationPopupWithMsg(String title, String msg) {
        super(Alert.AlertType.INFORMATION);
        setTitle(title);
        setHeaderText(null);
        setContentText(msg);
    }
    
}
