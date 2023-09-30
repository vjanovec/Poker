package com.vojtechjanovec.communication;

import java.io.Serializable;
import java.util.List;

public class Options implements Serializable {
    private List<Actions> options;
    private int value;

    public Options(List<Actions> options, int value) {
        this.options = options;
        this.value = value;
    }

    public List<Actions> getOptions() {
        return options;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Options{" +
                "options=" + options +
                ", value=" + value +
                '}';
    }
}
