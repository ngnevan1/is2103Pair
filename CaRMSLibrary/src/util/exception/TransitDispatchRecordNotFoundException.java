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
public class TransitDispatchRecordNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransitDispatchRecordNotFoundException</code> without detail
     * message.
     */
    public TransitDispatchRecordNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDispatchRecordNotFoundException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TransitDispatchRecordNotFoundException(String msg) {
        super(msg);
    }
}
