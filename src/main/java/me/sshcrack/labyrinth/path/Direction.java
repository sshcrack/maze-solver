package me.sshcrack.labyrinth.path;

public enum Direction {
    TOP,
    LEFT,
    BOTTOM,
    RIGHT;

    public Direction opposite() {
        Direction dir = null;
        switch(this) {
            case LEFT -> dir = RIGHT;
            case RIGHT -> dir = LEFT;
            case TOP -> dir = BOTTOM;
            case BOTTOM -> dir = TOP;
        }

        return dir;
    }
}
