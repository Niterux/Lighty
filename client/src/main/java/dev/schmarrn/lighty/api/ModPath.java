package dev.schmarrn.lighty.api;

import java.text.MessageFormat;
import java.util.Objects;

public class ModPath {
    private String modId;
    private String path;

    public ModPath(String modId, String path) {
        this.path = path;
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ModPath modPath = (ModPath) o;
        return Objects.equals(modId, modPath.modId) && Objects.equals(path, modPath.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modId, path);
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}:{1}", modId, path);
    }

    public static ModPath parse(String path) {
        String[] strings = path.split(":");
        return new ModPath(strings[0], strings[1]);
    }
}
