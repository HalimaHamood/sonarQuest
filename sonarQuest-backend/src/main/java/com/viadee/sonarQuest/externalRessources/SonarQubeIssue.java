package com.viadee.sonarQuest.externalRessources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarQubeIssue {

    private String key;

    private String message;

    private String component;

    private String severity;

    private String type;

    private String debt;

    private String status;

    private String resolution;

    public SonarQubeIssue() {
    }

    public SonarQubeIssue(String key, String message, String component, String severity, String type, String debt, String status, String resolution) {
        this.key = key;
        this.message = message;
        this.component = component;
        this.severity = severity;
        this.type = type;
        this.debt = debt;
        this.status = status;
        this.resolution = resolution;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
