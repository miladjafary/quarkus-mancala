package com.miladjafari.mancala.sdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoneTest {

    @Test
    public void testStoneEquality() {
        Stone firstStone = new Stone();
        Stone lastStone = new Stone();
        lastStone.setLast(true);

        assertNotEquals(firstStone, lastStone);
        assertNotEquals(firstStone.hashCode(), lastStone.hashCode());
    }
}