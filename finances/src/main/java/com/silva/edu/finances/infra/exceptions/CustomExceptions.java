package com.silva.edu.finances.infra.exceptions;

public class CustomExceptions {
    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String entityName, Object id) {
            super(String.format("%s não encontrado: %s", entityName, id));
        }
    }

    public static class InvalidInstallmentsException extends RuntimeException {
        public InvalidInstallmentsException() {
            super("Para o tipo INSTALMENTS deve informar as parcelas");
        }
    }
}
