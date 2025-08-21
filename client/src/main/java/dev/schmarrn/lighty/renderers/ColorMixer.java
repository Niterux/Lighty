package dev.schmarrn.lighty.renderers;

public class ColorMixer {
    public static int multiplyColorAndLight(int color, int light) {
        float colorAlpha = (float) ((color & 0xFF000000) >> 24) / 255;
        float colorRed = (float) ((color & 0x00FF0000) >> 16) / 255;
        float colorGreen = (float) ((color & 0x0000FF00) >> 8) / 255;
        float colorBlue = (float) (color & 0x000000FF) / 255;
        float lightAlpha = (float) ((light & 0xFF000000) >> 24) / 255;
        float lightRed = (float) ((light & 0x00FF0000) >> 16) / 255;
        float lightGreen = (float) ((light & 0x0000FF00) >> 8) / 255;
        float lightBlue = (float) (light & 0x000000FF) / 255;
        int outAlpha = (int) (colorAlpha * lightAlpha * 255);
        int outRed = (int) (colorRed * lightRed * 255);
        int outGreen = (int) (colorGreen * lightGreen * 255);
        int outBlue = (int) (colorBlue * lightBlue * 255);
        return (outAlpha << 24) | (outRed << 16) | (outGreen << 8) | outBlue;
    }
}
