package org.svrohith9.patientservice.exception;

public class PatientNotFoundException extends RuntimeException{
    public PatientNotFoundException(String s) {
        super(s);
    }
}
