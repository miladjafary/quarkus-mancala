package com.miladjafari.mancala.gamesdk;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Playground;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.miladjafari.mancala.gamesdk.GameAssert.assertPit;
import static com.miladjafari.mancala.gamesdk.GameAssert.assertPits;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaygroundTest {

    private static final Integer SIX_PITS = 6;
    private static final Integer SIX_STONES_IN_EACH_PITS = 6;

    private List<Pit> createSmallPitsAndOneBigPit(Integer numberOfPits, Integer numberOfStonesInEachPit) {
        List<Pit> pits = new ArrayList<>();
        for (int index = 0; index < numberOfPits; index++) {
            Pit pit = Pit.builder().index(index + 1).numberOfStones(numberOfStonesInEachPit).build();
            pits.add(pit);
        }

        Integer bigPitIndex = numberOfPits + 1;
        Pit bigPit = Pit.builder().index(bigPitIndex).big().numberOfStones(0).build();
        pits.add(bigPit);

        return pits;
    }

    @Test
    public void testSuccessCreatePlaygroundWithSixPitAndSixStonesInEachPit() {
        List<Pit> expectedPits = createSmallPitsAndOneBigPit(SIX_PITS, SIX_STONES_IN_EACH_PITS);

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertPits(expectedPits, actualPlayground.getAllPits());
    }

    @Test
    public void testSuccessGetPitIfIndexHasBeenValid() {
        final Integer EXPECTED_PIT_INDEX = 3;
        Pit expectedPit = Pit.builder()
                             .index(EXPECTED_PIT_INDEX)
                             .small()
                             .numberOfStones(SIX_STONES_IN_EACH_PITS)
                             .build();

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        Pit actualPit = actualPlayground.getPit(EXPECTED_PIT_INDEX);

        assertPit(expectedPit, actualPit);
    }

    @Test
    public void testSuccessGetPitIfIndexHasBeenEqualToSizeOfPits() {
        final Integer PIT_INDEX_EQUAL_WITH_PIT_SIZE = 7;
        Pit expectedPit = Pit.builder()
                             .index(PIT_INDEX_EQUAL_WITH_PIT_SIZE)
                             .big()
                             .empty()
                             .build();

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        Pit actualPit = actualPlayground.getPit(PIT_INDEX_EQUAL_WITH_PIT_SIZE);

        assertPit(expectedPit, actualPit);
    }

    @Test
    public void testFailGetPitIfIndexHasBeenPositiveAndOutOfBoundIndex() {
        final Integer OUT_OF_BOUND_INDEX = 8;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getPit(OUT_OF_BOUND_INDEX));
    }

    @Test
    public void testFailGetPitIfIndexHasBeenNegative() {
        final Integer NEGATIVE_OUT_OF_BOUND_INDEX = -1;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getPit(NEGATIVE_OUT_OF_BOUND_INDEX));
    }

    @Test
    public void testFailGetPitIfIndexHasBeenZero() {
        final Integer ZERO_INDEX = 0;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getPit(ZERO_INDEX));
    }

    @Test
    public void testSuccessGetBigPit() {
        final Integer BIG_PIT_INDEX = 7;
        Pit expectedPit = Pit.builder()
                             .index(BIG_PIT_INDEX)
                             .big()
                             .empty()
                             .build();

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        Pit actualBigPit = actualPlayground.getBigPit();

        assertPit(expectedPit, actualBigPit);
    }

    @Test
    public void testSuccessGetSubPits() {
        final Integer START_PIT_INDEX = 5;

        List<Pit> expectedPits = new ArrayList<>() {{
            add(Pit.builder().index(5).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(6).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(7).big().empty().build());
        }};

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        List<Pit> actualSubPits = actualPlayground.getSubPits(START_PIT_INDEX);
        assertPits(expectedPits, actualSubPits);
    }

    @Test
    public void testSuccessGetSubPitsIfStartIndexHasBeenEqualToSizeOfPits() {
        final Integer START_INDEX_EQUAL_WITH_PITS_SIZE = 7;

        List<Pit> expectedPits = new ArrayList<>() {{
            add(Pit.builder().index(7).big().empty().build());
        }};

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        List<Pit> actualSubPits = actualPlayground.getSubPits(START_INDEX_EQUAL_WITH_PITS_SIZE);
        assertPits(expectedPits, actualSubPits);
    }

    @Test
    public void testFailGetSubPitsIfStartIndexHasBeenPositiveAndOutOfBoundIndex() {
        final Integer OUT_OF_BOUND_START_INDEX = 8;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getSubPits(OUT_OF_BOUND_START_INDEX));
    }

    @Test
    public void testFailGetSubPitsIfStartIndexHasBeenNegative() {
        final Integer NEGATIVE_START_INDEX = -1;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getSubPits(NEGATIVE_START_INDEX));
    }

    @Test
    public void testFailGetSubPitsIfStartIndexHasBeenZero() {
        final Integer ZERO_INDEX = 0;

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertThrows(IllegalArgumentException.class, () -> actualPlayground.getSubPits(ZERO_INDEX));
    }

    @Test
    public void testSuccessGetAllPits() {
        List<Pit> expectedPits = new ArrayList<>() {{
            add(Pit.builder().index(1).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(2).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(3).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(4).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(5).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(6).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(7).big().empty().build());
        }};

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        List<Pit> actualPits = actualPlayground.getAllPits();
        assertPits(expectedPits, actualPits);
    }

    @Test
    public void testSuccessGetSmallPits() {
        List<Pit> expectedPits = new ArrayList<>() {{
            add(Pit.builder().index(1).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(2).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(3).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(4).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(5).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
            add(Pit.builder().index(6).small().numberOfStones(SIX_STONES_IN_EACH_PITS).build());
        }};

        Playground actualPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        List<Pit> actualPits = actualPlayground.getSmallPits();
        assertPits(expectedPits, actualPits);
    }

    @Test
    public void testSuccessCreateEqualPlayground() {
        Playground firstPlayground = Playground.builder()
                                               .numberOfPits(SIX_PITS)
                                               .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                               .build();
        Playground secondPlayground = Playground.builder()
                                                .numberOfPits(SIX_PITS)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertEquals(firstPlayground, secondPlayground);
        assertEquals(firstPlayground.hashCode(), secondPlayground.hashCode());
    }

    @Test
    public void testSuccessCreateNotEqualPlayground() {
        Playground firstPlayground = Playground.builder()
                                               .numberOfPits(7)
                                               .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                               .build();
        Playground secondPlayground = Playground.builder()
                                                .numberOfPits(6)
                                                .numberOfStonesInEachPit(SIX_STONES_IN_EACH_PITS)
                                                .build();

        assertNotEquals(firstPlayground, secondPlayground);
        assertNotEquals(firstPlayground.hashCode(), secondPlayground.hashCode());
    }

}