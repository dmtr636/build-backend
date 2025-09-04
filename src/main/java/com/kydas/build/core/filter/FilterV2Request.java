package com.kydas.build.core.filter;

import lombok.Data;

import java.util.Map;

@Data
public class FilterV2Request {
    private Order order;
    private int limit;
    private int offset;
    private Map<String, Object> filter;

    @Data
    public static class Order {
        private String field;
        private String direction;
    }
}
