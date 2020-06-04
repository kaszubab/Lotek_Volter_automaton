package sample.worldElements.animals;

import sample.worldElements.Vector;

import java.util.Objects;

public class Rabbit implements Animal{

    private Vector position;

    public Rabbit(Vector position) {
        this.position = position;
    }

    @Override
    public void move(Vector vector) {
        this.position = vector;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rabbit rabbit = (Rabbit) o;
        return getPosition().equals(rabbit.getPosition());
    }

    @Override
    public int hashCode() {
        return getPosition().hashCode();
    }
}
