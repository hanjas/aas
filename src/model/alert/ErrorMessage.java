/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.alert;

import javafx.scene.control.Alert;

/**
 *
 * @author BCz
 */
public class ErrorMessage extends Alert {
    
    public ErrorMessage(String message) {
        super(AlertType.ERROR);
        
        setTitle("Error Message");
        setHeaderText(null);
        setContentText(message);
    }
    
}
