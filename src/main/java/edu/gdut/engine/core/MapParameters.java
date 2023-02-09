package edu.gdut.engine.core;

import java.util.Map;

public class MapParameters implements Parameters {
    private final Map<String, Object> params;

    public MapParameters(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public Object get(String name) {
        return params.get(name);
    }
}
