package com.commai.commaplayer.enums;

/**
 * videoview播放模式
 */
public enum VideoPlayModeEnum {
    LOOP(0),
    SINGLE(1),
    LISTONCE(2);

    private int value;

    VideoPlayModeEnum(int value) {
        this.value = value;
    }

    public static VideoPlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SINGLE;
            case 2:
                return LISTONCE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}
