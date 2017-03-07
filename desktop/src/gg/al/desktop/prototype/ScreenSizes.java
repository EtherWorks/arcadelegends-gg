package gg.al.desktop.prototype;

public enum ScreenSizes {

    DEFAULT(1920, 1080),
    SMALL(960, 540);
    private final int width;
    private final int height;

    ScreenSizes(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
