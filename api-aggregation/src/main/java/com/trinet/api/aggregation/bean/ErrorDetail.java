package com.trinet.api.aggregation.bean;

/**
 * 
 * @author venkata_badri
 * 
 *         this class will hold error details.
 */
public class ErrorDetail {

    private int status;
    private String message;
    private String url;
    
    public ErrorDetail() {
        // No implementation
    }

    public ErrorDetail(String message, int status,String url) {
        this.status = status;
        this.message = message;
        this.url=url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
