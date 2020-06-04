package sample.worldElements;

import java.util.Objects;

public class Vector {
    int x;
    int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    boolean precedes(Vector other) {
        return this.x <= other.x && this.y <= other.y;
    }

    boolean follows(Vector other) {
        return this.x >= other.x && this.y >= other.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x &&
                y == vector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
