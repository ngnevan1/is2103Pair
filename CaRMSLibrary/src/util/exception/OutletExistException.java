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
public class OutletExistException extends Exception {

    /**
     * Creates a new instance of <code>OutletExistException</code> without
     * detail message.
     */
    public OutletExistException() {
    }

    /**
     * Constructs an instance of <code>OutletExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutletExistException(String msg) {
        super(msg);
    }
}
