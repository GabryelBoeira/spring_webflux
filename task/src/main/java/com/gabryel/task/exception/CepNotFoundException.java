package com.gabryel.task.exception;

public class CepNotFoundException extends RuntimeException {
    public CepNotFoundException() {
        super("Cep not found");
    }
}
