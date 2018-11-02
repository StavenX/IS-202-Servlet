/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author tobia
 */
public class InvalidSymbolException extends Exception {
    private String message;
    private String invalidField;
    
    public InvalidSymbolException (String invalidField) {
        this.invalidField = invalidField;
        this.message = "Field " + this.invalidField + " contained an invalid character";
    }
    
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    public String getInvalidField() {
        return this.invalidField;
    }
    
    
}
