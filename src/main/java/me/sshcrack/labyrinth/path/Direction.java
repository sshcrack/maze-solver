package me.sshcrack.labyrinth.path;

public enum Direction {
    UP,
    LEFT,
    DOWN,
    RIGHT;

    public Direction opposite() {
        Direction dir = null;
        switch(this) {
            case LEFT -> dir = RIGHT;
            case RIGHT -> dir = LEFT;
            case UP -> dir = DOWN;
            case DOWN -> dir = UP;
        }

        return dir;
    }
}
