package com.miladjafari.mancala.sdk;

import java.util.Objects;

/**
 * This class resemble the stones/seeds that use in the Mancala game
 */
public class Stone {

    /**
     * Mark a stone as the last stone, therefore it would be possible to
     * recognized the last stone of a pit's stones
     */
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
