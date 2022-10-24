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
public class CarExistException extends Exception {

    /**
     * Creates a new instance of <code>CarExistException</code> without detail
     * message.
     */
    public CarExistException() {
    }

    /**
     * Constructs an instance of <code>CarExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarExistException(String msg) {
        super(msg);
    }
}
