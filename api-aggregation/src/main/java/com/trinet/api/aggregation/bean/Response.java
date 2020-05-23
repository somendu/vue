package com.trinet.api.aggregation.bean;

/**
 * 
 */


/**
 * @author laxmi_pabbaraju
 * This is the response object 
 */
public class Response {

    /* hold data of individual services  */
    private Object data;
    
    /* the response status  */
    private String statusCode;
    
    /* The status  Text */
    private String statusText;
    
    /* The status message */
    private String statusMessage;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    
   
}
