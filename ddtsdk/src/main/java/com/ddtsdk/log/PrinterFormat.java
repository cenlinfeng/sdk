package com.ddtsdk.log;

public class PrinterFormat {

    private static final String TOP_LEFT_CORNER = "┌";
    private static final String BOTTOM_LEFT_CORNER = "└";
    private static final String MIDDLE_CORNER = "├";
    private static final String HORIZONTAL_LINE = "│";
    public static final String HORIZONTAL_LINE_SPACE = "│ ";
    public static final String DOUBLE_SPACE = "  ";
    private static final String DOUBLE_DIVIDER = "──────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    public static String msgFormat(String msg, Object... args){
        StringBuilder builder = new StringBuilder();
        builder.append(" \n")
                .append(TOP_BORDER)
                .append("\n")
                .append(msg)
                .append(MIDDLE_BORDER)
                .append("\n")
                .append(Utils.obj2String(args))
                .append(BOTTOM_BORDER);
        return builder.toString();
    }

}
