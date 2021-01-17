package com.miladjafari.mancala.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pit {
    private Integer index;
    private Boolean isBigPit = false;
    private List<Stone> stones = new ArrayList<>();

    public Integer getIndex() {
        return index;
    }

    public Boolean isBigPit() {
        return isBigPit;
    }

    public Integer getCountOfStones() {
        return stones.size();
    }

    public Boolean isEmpty() {
        return stones.isEmpty();
    }

    public void pushStone(Stone stone) {
        stones.add(stone);
    }

    public List<Stone> pickUpStones() {
        List<Stone> popStones = new ArrayList<>(stones);
        stones.clear();

        return popStones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pit pit = (Pit) o;
        return Objects.equals(index, pit.index) &&
                Objects.equals(isBigPit, pit.isBigPit) &&
                Objects.equals(stones, pit.stones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, isBigPit, stones);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Pit instance = new Pit();
        private Integer numberOfStones = 6;

        public Builder index(Integer index) {
            instance.index = index;
            return this;
        }

        public Builder big() {
            instance.isBigPit = true;
            return this;
        }

        public Builder small() {
            instance.isBigPit = false;
            return this;
        }

        public Builder numberOfStones(Integer numberOfStones) {
            this.numberOfStones = numberOfStones;
            return this;
        }

        public Builder empty() {
            this.numberOfStones = 0;
            return this;
        }

        private void createStones() {
            for (int stoneIndex = 0; stoneIndex < numberOfStones; stoneIndex++) {
                instance.stones.add(new Stone());
            }
        }

        public Pit build() {
            createStones();
            return instance;
        }
    }
}
