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
public class ConfirmDelete extends Alert {

    public ConfirmDelete() {
        super(AlertType.CONFIRMATION);
        
        setTitle("Confirmation");
        setHeaderText(null);
        setContentText("Are you sure you want to delete?");
    }
}
