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
public class CarCategoryExistException extends Exception {

    /**
     * Creates a new instance of <code>CarCategoryExistException</code> without
     * detail message.
     */
    public CarCategoryExistException() {
    }

    /**
     * Constructs an instance of <code>CarCategoryExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarCategoryExistException(String msg) {
        super(msg);
    }
}
