package com.miladjafari.mancala.sdk;

import java.util.Objects;

public class Stone {
    private Boolean isLast = false;

    public Boolean isLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stone stone = (Stone) o;
        return Objects.equals(isLast, stone.isLast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isLast);
    }
}
