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
public class RentalRateNotAvailableException extends Exception {

    /**
     * Creates a new instance of <code>RentalRateNotAvailableException</code>
     * without detail message.
     */
    public RentalRateNotAvailableException() {
    }

    /**
     * Constructs an instance of <code>RentalRateNotAvailableException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRateNotAvailableException(String msg) {
        super(msg);
    }
}
