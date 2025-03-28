package org.svrohith9.patientservice.exception;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException(String s) {
        super(s);
    }
}
