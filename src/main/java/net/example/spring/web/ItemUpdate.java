package net.example.spring.web;

import java.util.regex.Pattern;

import jakarta.validation.constraints.NotBlank;

/**
 * Item create/update request payload.
 */
public class ItemUpdate {

    private static Pattern WS = Pattern.compile("\\s+");

    @NotBlank
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = normalizeWS(label);
    }

    private static String normalizeWS(String str) {
        return (str == null) ? null
                             : WS.matcher(str.strip()).replaceAll(" ");
    }

}
