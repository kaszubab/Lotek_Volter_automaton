package sample;

public class SimulationConfiguration {

    private double foxDeathPropability;
    private double foxBirthPropability;
    private double rabbitBirthPropability;

    public SimulationConfiguration(
                              double foxDeathPropability, double foxBirthPropability, double rabbitBirthPropability) {

        if (foxBirthPropability < 0 || foxBirthPropability > 1)
            throw new IllegalArgumentException("Fox birth propability must be between 0 and 1");
        this.foxBirthPropability = foxBirthPropability;

        if (rabbitBirthPropability < 0 || rabbitBirthPropability > 1)
            throw new IllegalArgumentException("Fox birth propability must be between 0 and 1");
        this.rabbitBirthPropability = rabbitBirthPropability;

        if (foxDeathPropability < 0 || foxDeathPropability > 1)
            throw new IllegalArgumentException("Fox death propability must be between 0 and 1");
        this.foxDeathPropability = foxDeathPropability;

    }


    public double getFoxBirthPropability() {
        return foxBirthPropability;
    }

    public double getFoxDeathPropability() {
        return foxDeathPropability;
    }

    public double getRabbitBirthPropability() {
        return rabbitBirthPropability;
    }
}
