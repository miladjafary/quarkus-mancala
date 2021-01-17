package com.miladjafari.mancala.sdk;

import java.util.Objects;

public class Player implements Comparable<Player> {
    private String name;

    public String getName() {
        return name;
    }

    public Player(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(name, player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Player otherPlayer) {
        return this.name.compareTo(otherPlayer.getName());
    }
}
