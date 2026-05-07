package com.edu.silva.users.infra.exceptions;

public class CustomExceptions {
    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException(String email) {
            super("Email: " + email);
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

    public static class InvalidGmailTokenException extends RuntimeException {
        public InvalidGmailTokenException(String message) {
            super(message);
        }
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String entityName, Object id) {
            super(id == null ? String.format("%s não encontrado", entityName) : String.format("%s não encontrado: %s", entityName, id));
        }
    }
}
