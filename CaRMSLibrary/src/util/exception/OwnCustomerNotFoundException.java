/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author KMwong
 */
public class OwnCustomerNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>OwnCustomerNotFoundException</code>
     * without detail message.
     */
    public OwnCustomerNotFoundException() {
    }

    /**
     * Constructs an instance of <code>OwnCustomerNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public OwnCustomerNotFoundException(String msg) {
        super(msg);
    }
}
