package xyz.parkh.challenge.api.controlleradvice;

import lombok.Data;

@Data
public class BindingError {
    private String field;
    private String message;

    public BindingError(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
