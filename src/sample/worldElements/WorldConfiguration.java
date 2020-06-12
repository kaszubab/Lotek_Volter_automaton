package sample.worldElements;

import sample.SimulationConfiguration;

public class WorldConfiguration extends SimulationConfiguration {

    private final int width;
    private final int height;
    private final int initialFoxCount;
    private final int initialRabbitCount;

    public WorldConfiguration(double foxPercentage, double rabbitPercentage, int width, int height,
                              double foxDeathPropability, double foxBirthPropability, double rabbitBirthPropability) {

        super(foxDeathPropability, foxBirthPropability, rabbitBirthPropability);
        if (width < 0 || width > 200)
            throw new IllegalArgumentException("Width must be between 0 and 200");
        this.width = width;

        if(height < 0 || height > 200)
            throw new IllegalArgumentException("Height must be between 0 and 200");
        this.height = height;

        if (foxPercentage < 0 || foxPercentage > 1)
            throw new IllegalArgumentException("Fox percentage  must be between 0 and 1");
        this.initialFoxCount = (int) Math.floor(width * height * foxPercentage);

        if (rabbitPercentage < 0 || rabbitPercentage > 1)
            throw new IllegalArgumentException("Rabbit percentage must be between 0 and 1");
        this.initialRabbitCount = Math.min(width * height - initialFoxCount, (int) Math.floor(rabbitPercentage * width * height));

    }


    public int getHeight() {
        return height;
    }

    public int getInitialFoxCount() {
        return initialFoxCount;
    }

    public int getInitialRabbitCount() {
        return initialRabbitCount;
    }

    public int getWidth() {
        return width;
    }

    public double getFoxBirthPropability() {
        return super.getFoxBirthPropability();
    }

    public double getFoxDeathPropability() {
        return super.getFoxDeathPropability();
    }

    public double getRabbitBirthPropability() {
        return super.getRabbitBirthPropability();
    }
}
