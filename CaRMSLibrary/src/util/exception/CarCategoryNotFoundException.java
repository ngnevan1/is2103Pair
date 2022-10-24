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
public class CarCategoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>CarCategoryNotFoundException</code>
     * without detail message.
     */
    public CarCategoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>CarCategoryNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarCategoryNotFoundException(String msg) {
        super(msg);
    }
}
