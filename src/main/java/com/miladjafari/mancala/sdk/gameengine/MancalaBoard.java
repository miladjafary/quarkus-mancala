package com.miladjafari.mancala.sdk.gameengine;

import com.miladjafari.mancala.sdk.Pit;
import com.miladjafari.mancala.sdk.Playground;
import com.miladjafari.mancala.sdk.Stone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

class MancalaBoard {
    private Playground ownerPlayground;
    private Playground opponentPlayground;

    private Integer fromPitIndex;
    private Boolean lastStoneWasPushedInBigPit = false;

    public MancalaBoard ownerPlayground(Playground ownerPlayground) {
        this.ownerPlayground = ownerPlayground;
        return this;
    }

    public MancalaBoard opponentPlayground(Playground opponentPlayground) {
        this.opponentPlayground = opponentPlayground;
        return this;
    }

    public MancalaBoard selectedPit(Integer selectedPitIndex) {
        this.fromPitIndex = selectedPitIndex;
        return this;
    }

    public Boolean isLastStonePushedInBigPit() {
        return lastStoneWasPushedInBigPit;
    }

    public MancalaBoard play() {
        List<Stone> pickedUpStones = ownerPlayground.getPit(fromPitIndex).pickUpStones();
        setLastStoneFlagAndClearOtherStonesLastFlag(pickedUpStones);
        Queue<Stone> stones = createQueueOfStones(pickedUpStones);

        playFirstRoundInOwnerPlayground(stones);

        boolean shouldPlayInOpponentPlayground = true;
        while (stones.size() > 0) {
            if (shouldPlayInOpponentPlayground) {
                playInOpponentPlayground(stones);
                shouldPlayInOpponentPlayground = false;
            } else {
                playInOwnerPlayground(stones);
                shouldPlayInOpponentPlayground = true;
            }
        }

        return this;
    }

    private void playFirstRoundInOwnerPlayground(Queue<Stone> stones) {
        fromPitIndex += 1;
        List<Pit> pits = ownerPlayground.getSubPits(fromPitIndex);
        pushStones(pits, stones, true);
    }

    private void playInOpponentPlayground(Queue<Stone> stones) {
        List<Pit> opponentSmallPits = opponentPlayground.getSmallPits();
        List<Pit> pits = new ArrayList<>(opponentSmallPits);
        pits = pits.stream()
                   .sorted(Comparator.comparingInt(Pit::getIndex).reversed())
                   .collect(Collectors.toList());

        pushStones(pits, stones, false);
    }

    private void playInOwnerPlayground(Queue<Stone> stones) {
        List<Pit> pits = ownerPlayground.getAllPits();
        pushStones(pits, stones, true);
    }

    private void pushStones(List<Pit> pits, Queue<Stone> stones, Boolean playingInOwnerPlayground) {
        pits.forEach(pit -> {
            if (!stones.isEmpty()) {
                Stone stone = stones.poll();
                lastStoneWasPushedInBigPit = stone.isLast() && pit.isBigPit();
                if (playingInOwnerPlayground) {
                    getRewardIfPitIsEmptyAndStoneIsTheLast(pit, stone);
                }

                pit.pushStone(stone);
            }
        });
    }

    private void getRewardIfPitIsEmptyAndStoneIsTheLast(Pit pit, Stone stone) {
        if (!pit.isBigPit() && pit.isEmpty() && stone.isLast()) {
            Pit opponentPit = opponentPlayground.getSmallPits().get(pit.getIndex() - 1);
            List<Stone> opponentStones = opponentPit.pickUpStones();

            Pit bigPit = ownerPlayground.getBigPit();
            opponentStones.forEach(bigPit::pushStone);
        }
    }

    private void setLastStoneFlagAndClearOtherStonesLastFlag(List<Stone> stones) {
        if (!stones.isEmpty()) {
            stones.forEach(stone -> stone.setLast(false));

            Stone lastStone = stones.get(stones.size() - 1);
            lastStone.setLast(true);
        }
    }

    private Queue<Stone> createQueueOfStones(List<Stone> stones) {
        Queue<Stone> stonesQueue = new ArrayBlockingQueue<>(stones.size());
        stonesQueue.addAll(stones);

        return stonesQueue;
    }
}