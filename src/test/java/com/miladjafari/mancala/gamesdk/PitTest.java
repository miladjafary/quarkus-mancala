package com.miladjafari.mancala.gamesdk;


import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Stone;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class PitTest {

    private final static Integer SIX_STONES = 6;

    @Test
    public void testSuccessCreateSmallPitWith6Stones() {
        Pit actualPit = Pit.builder()
                .small()
                .numberOfStones(SIX_STONES)
                .build();

        assertEquals(SIX_STONES, actualPit.getCountOfStones());
        assertFalse(actualPit.isBigPit());
    }

    @Test
    public void testSuccessCreateBigPitWith6Stones() {
        Pit actualPit = Pit.builder()
                .big()
                .numberOfStones(SIX_STONES)
                .build();

        assertEquals(SIX_STONES, actualPit.getCountOfStones());
        assertTrue(actualPit.isBigPit());
    }

    @Test
    public void testSuccessPickupStones() {
        final int EXPECTED_NUMBER_OF_STONES = 6;
        final Integer EXPECTED_NUMBER_OF_STONES_AFTER_PICKED_UP = 0;
        Pit actualPit = Pit.builder()
                .numberOfStones(SIX_STONES)
                .build();

        List<Stone> actualPickedUpStones = actualPit.pickUpStones();

        assertEquals(EXPECTED_NUMBER_OF_STONES_AFTER_PICKED_UP, actualPit.getCountOfStones());
        assertEquals(EXPECTED_NUMBER_OF_STONES, actualPickedUpStones.size());
    }

    @Test
    public void testSuccessPickupStonesIfPitHasNotAnyStones() {
        final int ZERO_STONES = 0;
        final Integer EXPECTED_NUMBER_OF_STONES_AFTER_PICKED_UP = 0;

        Pit actualPit = Pit.builder()
                .numberOfStones(ZERO_STONES)
                .build();

        List<Stone> actualPopStones = actualPit.pickUpStones();

        assertEquals(EXPECTED_NUMBER_OF_STONES_AFTER_PICKED_UP, actualPit.getCountOfStones());
        assertEquals(ZERO_STONES, actualPopStones.size());

    }

    @Test
    public void testSuccessPushStone() {
        final Integer EXPECTED_NUMBER_OF_STONES = 7;
        Pit actualPit = Pit.builder()
                .small()
                .numberOfStones(SIX_STONES)
                .build();

        actualPit.pushStone(new Stone());

        assertFalse(actualPit.isEmpty());
        assertEquals(EXPECTED_NUMBER_OF_STONES, actualPit.getCountOfStones());
    }

}