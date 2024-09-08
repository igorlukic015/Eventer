package com.eventer.admin.contracts.message;

import java.io.Serializable;
import java.time.Instant;

public class Message implements Serializable {
    private String name;
    private Instant dispatchedAt;
    private String entityType;
    private String action;
    private Object data;

    public Message() {}

    public Message(
            String name, Instant dispatchedAt, String entityType, String action, Object data) {
        this.name = name;
        this.dispatchedAt = dispatchedAt;
        this.entityType = entityType;
        this.action = action;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDispatchedAt() {
        return dispatchedAt;
    }

    public void setDispatchedAt(Instant dispatchedAt) {
        this.dispatchedAt = dispatchedAt;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
