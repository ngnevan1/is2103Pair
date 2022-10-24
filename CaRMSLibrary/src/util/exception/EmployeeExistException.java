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
public class EmployeeExistException extends Exception {

    /**
     * Creates a new instance of <code>EmployeeExistException</code> without
     * detail message.
     */
    public EmployeeExistException() {
    }

    /**
     * Constructs an instance of <code>EmployeeExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmployeeExistException(String msg) {
        super(msg);
    }
}
