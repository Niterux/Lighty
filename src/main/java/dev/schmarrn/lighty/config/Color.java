package dev.schmarrn.lighty.config;

public record Color(byte red, byte green, byte blue, byte alpha) {
	public static Color fromHex(String hexRGBA) {
		hexRGBA = hexRGBA.replaceFirst("^0[xX]", "");
		return fromInt(Integer.parseUnsignedInt(hexRGBA, 16));
	}

	public static Color fromInt(int RGBA) {
		byte red = (byte) ((RGBA & 0xFF000000) >> 24);
		byte green = (byte) ((RGBA & 0xFF000000) >> 16);
		byte blue = (byte) ((RGBA & 0xFF000000) >> 8);
		byte alpha = (byte) (RGBA & 0x000000FF);
		return new Color(red, green, blue, alpha);
	}

	public int toInt() {
		return red() << 24 | green() << 16 | blue() << 8 | alpha();
	}
}
