package sample.worldElements;

public class WorldConfiguration {

    private int initialFoxCount;
    private int initialRabbitCount;
    private int width;
    private int height;
    private double foxDeathPropability;
    private double foxBirthPropability;
    private double rabbitBirthPropability;


    public WorldConfiguration(String filename)
    {
        // TODO
    }

    public WorldConfiguration(double foxPercentage, double rabbitPercentage, int width, int height,
                              double foxDeathPropability, double foxBirthPropability, double rabbitBirthPropability) {

        if (width < 0 || width > 200)
            throw new IllegalArgumentException("Width must be between 0 and 200");
        this.width = width;

        if(height < 0 || height > 200)
            throw new IllegalArgumentException("Height must be between 0 and 200");
        this.height = height;

        if (foxBirthPropability < 0 || foxBirthPropability > 1)
            throw new IllegalArgumentException("Fox birth propability must be between 0 and 1");
        this.foxBirthPropability = foxBirthPropability;

        if (rabbitBirthPropability < 0 || rabbitBirthPropability > 1)
            throw new IllegalArgumentException("Fox birth propability must be between 0 and 1");
        this.rabbitBirthPropability = rabbitBirthPropability;

        if (foxDeathPropability < 0 || foxDeathPropability > 1)
            throw new IllegalArgumentException("Fox death propability must be between 0 and 1");
        this.foxDeathPropability = foxDeathPropability;

        if (foxPercentage < 0 || foxPercentage > 1)
            throw new IllegalArgumentException("Fox percentage  must be between 0 and 1");
        System.out.println(foxPercentage);
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
        return foxBirthPropability;
    }

    public double getFoxDeathPropability() {
        return foxDeathPropability;
    }

    public double getRabbitBirthPropability() {
        return rabbitBirthPropability;
    }
}
