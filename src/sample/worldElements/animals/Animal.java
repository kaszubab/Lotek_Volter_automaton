package sample.worldElements.animals;

import sample.worldElements.Vector;

public interface Animal {
    public void move(Vector vector);
    public Vector getPosition();
}
