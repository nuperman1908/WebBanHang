package com.devtam.commonbase.constant;

public enum CartStatus {
    IN_CART(0),
    IN_ORDER(1),
    DElETED(2);

    private static final String[] NAMING = new String[]{"IN_CART", "IN_ORDER", "DElETED"};
    private final int value;

    private CartStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static String getObjectName(int value) throws Exception {
        try {
            if (value < NAMING.length) {
                return NAMING[value];
            } else {
                throw new Exception(String.format("The value {%s} is not exist", value));
            }
        } catch (Exception e) {
            throw new Exception(String.format("The value {%s} is not exist", value));
        }

    }
}
