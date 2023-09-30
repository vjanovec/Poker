package com.vojtechjanovec.communication;

import java.io.Serializable;

public class Response implements Serializable {
    private Actions action;
    private int value;

    public Response(Actions action, int value) {
        this.action = action;
        this.value = value;
    }

    public Response(Actions action) {
        this.action = action;
    }

    public Actions getAction() {
        return action;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Response{" +
                "action=" + action +
                ", value=" + value +
                '}';
    }
}
