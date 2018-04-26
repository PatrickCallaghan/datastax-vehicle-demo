package com.datastax.vehicle.webservice.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationOutcome {

    private List<String> validationMessages = new ArrayList<>();

    public boolean isValid() {
        return validationMessages.size() == 0;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getValidationMessagesAsSingleString() {
        StringBuilder sb = new StringBuilder();
        for(String msg : validationMessages) {
            sb.append(msg).append('\n');
        }
        return sb.toString();
    }

    public void addMessage(String message) {
        if (message != null) {
            validationMessages.add(message);
        }
    }
}
