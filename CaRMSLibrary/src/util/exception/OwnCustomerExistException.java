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
public class OwnCustomerExistException extends Exception {

    /**
     * Creates a new instance of <code>OwnCustomerExistException</code> without
     * detail message.
     */
    public OwnCustomerExistException() {
    }

    /**
     * Constructs an instance of <code>OwnCustomerExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OwnCustomerExistException(String msg) {
        super(msg);
    }
}
