package com.andrei.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by shubham on 10/12/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiResponse {

    @XmlElement(name = "data")
    private String data;

    @XmlElement(name = "userId")
    private String userId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
