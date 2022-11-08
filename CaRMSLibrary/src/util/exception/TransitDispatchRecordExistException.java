/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author nevanng
 */
public class TransitDispatchRecordExistException extends Exception {

    /**
     * Creates a new instance of
     * <code>TransitDispatchRecordExistException</code> without detail message.
     */
    public TransitDispatchRecordExistException() {
    }

    /**
     * Constructs an instance of
     * <code>TransitDispatchRecordExistException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public TransitDispatchRecordExistException(String msg) {
        super(msg);
    }
}
