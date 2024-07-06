package dev.coder.booker.enumeration;

import lombok.Getter;

@Getter
public enum EmailTemplate {
    ACTIVATE_ACCOUNT("activate_account"),
RESET_PASSWORD("reset_password");

    private final String templateName;

    EmailTemplate(String templateName) {
        this.templateName = templateName;
    }
}
