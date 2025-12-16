package com.silva.edu.finances.infra.exceptions;

public class CustomExceptions {
    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String email) {
            super("Email já cadastrado: " + email);
        }
    }

    public static class InvalidRoleException extends RuntimeException {
        public InvalidRoleException(String message) {
            super(message);
        }
    }

    public static class InvalidStatusException extends RuntimeException {
        public InvalidStatusException(String message) {
            super(message);
        }
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String entityName, Object id) {
            super(String.format("%s não encontrado: %s", entityName, id));
        }
    }
}
