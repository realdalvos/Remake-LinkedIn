package org.hbrs.se2.project.services.db.exception;

public class DatabaseLayerException extends Exception {
    private String reason = null;
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public DatabaseLayerException(String reason ) {
        this.reason = reason;
    }

}
