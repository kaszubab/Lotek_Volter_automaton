package sample.worldElements.animals;

import sample.worldElements.Vector;

import java.util.Objects;

public class Fox implements Animal{

    private Vector position;
    private int currentEnergy;
    private int maxEnergy;

    public Fox(Vector position, int lifeExpectancy) {
        this.position = position;
        this.currentEnergy = lifeExpectancy;
        this.maxEnergy = lifeExpectancy;
    }

    @Override
    public void move(Vector vector) {
        this.position = vector;
    }

    @Override
    public Vector getPosition() {
        return position;
    }

    public boolean surviveDay() {
        this.currentEnergy--;
        return this.currentEnergy >= 0;
    }

    public void eat() {
        this.currentEnergy = this.maxEnergy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fox fox = (Fox) o;
        return Objects.equals(getPosition(), fox.getPosition());
    }

    @Override
    public int hashCode() {
        return getPosition().hashCode();
    }
}
