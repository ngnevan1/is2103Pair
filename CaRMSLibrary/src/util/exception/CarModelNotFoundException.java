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
public class CarModelNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CarModelNotFoundException</code> without
     * detail message.
     */
    public CarModelNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CarModelNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelNotFoundException(String msg) {
        super(msg);
    }
}
