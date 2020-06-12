package sample.DEElements;

import sample.SimulationConfiguration;

public class DESimulationConfiguration extends SimulationConfiguration {

    private final int initialFoxCount;
    private final int initialRabbitCount;

    public DESimulationConfiguration(int initialFoxCount, int initialRabbitCount,
                              double foxDeathPropability, double foxBirthPropability, double rabbitBirthPropability) {

        super(foxDeathPropability, foxBirthPropability, rabbitBirthPropability);
        this.initialFoxCount = initialFoxCount;
        this.initialRabbitCount = initialRabbitCount;
    }

    public int getInitialFoxCount() {
        return initialFoxCount;
    }

    public int getInitialRabbitCount() {
        return initialRabbitCount;
    }
}
