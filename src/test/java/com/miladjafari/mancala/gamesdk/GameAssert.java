package com.miladjafari.mancala.gamesdk;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Player;
import com.miladjafari.mancala.sdk.Playground;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameAssert {

    public static void assertPlayer(Player expected, Player actual) {
        assertEquals(expected.getName(), actual.getName());
    }

    public static void assertPlayground(Playground expectedPlayground, Playground actualPlayground) {
        assertEquals(expectedPlayground.getAllPits().size(), actualPlayground.getAllPits().size());
        assertPits(expectedPlayground.getAllPits(), actualPlayground.getAllPits());
    }

    public static void assertPits(List<Pit> expectedPits, List<Pit> actualPits) {
        assertEquals(expectedPits.size(), actualPits.size());

        expectedPits = sort(expectedPits);
        actualPits = sort(actualPits);

        for (int index = 0; index < expectedPits.size(); index++) {
            Pit expectedPit = expectedPits.get(index);
            Pit actualPit = actualPits.get(index);

            assertPit(expectedPit, actualPit);
        }
    }

    public static void assertPit(Pit expectedPit, Pit actualPit) {
        assertEquals(expectedPit.getIndex(), actualPit.getIndex());
        assertEquals(expectedPit.getCountOfStones(), actualPit.getCountOfStones());
        assertEquals(expectedPit.isBigPit(), actualPit.isBigPit());
    }

    public static List<Pit> sort(List<Pit> pits) {
        return pits.stream().sorted(Comparator.comparing(Pit::getIndex)).collect(Collectors.toList());
    }

}