package com.miladjafari.mancala.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represent the playground of a player which contains list of {@link Pit}
 */
public class Playground {
    private List<Pit> pits = new ArrayList<>();

    /**
     * Return list of pit after selected pit.
     * @param startPitIndex, the player selected pit.
     * @return list of {@link Stone}
     */
    public List<Pit> getSubPits(Integer startPitIndex) {
        if (startPitIndex <= 0 || startPitIndex > pits.size()) {
            throw new IllegalArgumentException(String.format("Invalid pit index. Number of pits is [%s]", pits.size()));
        }

        return pits.subList(startPitIndex - 1, pits.size());
    }

    public List<Pit> getAllPits() {
        return pits;
    }

    public List<Pit> getSmallPits() {
        return pits.stream().filter(pit -> !pit.isBigPit()).collect(Collectors.toList());
    }

    public Pit getPit(Integer pitIndex) {
        if (pitIndex <= 0 || pitIndex > pits.size()) {
            throw new IllegalArgumentException(String.format("Invalid pit index. Number of pits is [%s]", pits.size()));
        }

        return pits.get(pitIndex - 1);
    }

    public Pit getBigPit() {
        List<Pit> bigPits = pits.stream().filter(Pit::isBigPit).collect(Collectors.toList());

        return bigPits.get(0);
    }

    /**
     * Validate all stored small pit in the player playground are empty.
     * @return true in case all small pit would be empty otherwise return false
     */
    public Boolean areAllSmallPitsEmpty() {
        List<Pit> nounEmptyPits = getSmallPits()
                .stream()
                .filter(pit -> !pit.isEmpty())
                .collect(Collectors.toList());
        return nounEmptyPits.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playground that = (Playground) o;
        return Objects.equals(pits, that.pits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pits);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Create and instance of {@link Playground} with six small {@link Pit} and one big pit
     */
    public static class Builder {
        private Playground instance = new Playground();

        private Integer numberOfPits = 6;
        private Integer numberOfStonesInEachPit = 6;

        public Builder numberOfPits(Integer pitCount) {
            this.numberOfPits = pitCount;
            return this;
        }

        public Builder numberOfStonesInEachPit(Integer stoneCount) {
            this.numberOfStonesInEachPit = stoneCount;
            return this;
        }

        private void createSmallPitsAndOneBigPit() {
            for (int pitIndex = 0; pitIndex < numberOfPits; pitIndex++) {
                Pit smallPit = Pit.builder()
                        .index(pitIndex + 1)
                        .small()
                        .numberOfStones(numberOfStonesInEachPit)
                        .build();

                instance.pits.add(smallPit);
            }

            Integer bigPitIndex = numberOfPits + 1;
            createBigPit(bigPitIndex);
        }

        private void createBigPit(Integer bigPitIndex) {
            Pit bigPit = Pit.builder()
                    .index(bigPitIndex)
                    .big()
                    .numberOfStones(0)
                    .build();

            instance.pits.add(bigPit);
        }

        public Playground build() {
            createSmallPitsAndOneBigPit();
            return instance;
        }
    }
}
