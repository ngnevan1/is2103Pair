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
public class UpdateRentalRateException extends Exception{

    /**
     * Creates a new instance of <code>UpdateRentalRateException</code> without
     * detail message.
     */
    public UpdateRentalRateException() {
    }

    /**
     * Constructs an instance of <code>UpdateRentalRateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateRentalRateException(String msg) {
        super(msg);
    }
}
