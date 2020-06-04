package sample.worldElements;

public class WorldConfiguration {

    private int initialFoxCount;
    private int initialRabbitCount;
    private int width;
    private int height;
    private int foxLifeExpectancy;

    public WorldConfiguration(String filename)
    {
        // TODO
    }

    public WorldConfiguration(int initialFoxCount, int initialRabbitCount, int width, int height,
                              int foxLifeExpectancy) {
        if (initialFoxCount < 0 || initialFoxCount > width * height - initialRabbitCount)
            throw new IllegalArgumentException("Fox Count must be between 0 and capacity of the map minus rabbit count");
        this.initialFoxCount = initialFoxCount;

        if (initialRabbitCount < 0 || initialRabbitCount > width * height - initialFoxCount)
            throw new IllegalArgumentException("Rabbit count must be between 0 and capacity of the map minus fox count");
        this.initialRabbitCount = initialRabbitCount;

        if (width < 0 || width > 200)
            throw new IllegalArgumentException("Width must be between 0 and 200");
        this.width = width;

        if(height < 0 || height > 200)
            throw new IllegalArgumentException("Height must be between 0 and 200");
        this.height = height;

        if (foxLifeExpectancy < 0)
            throw new IllegalArgumentException("Fox life expectancy must be higher or equal to zero");
        this.foxLifeExpectancy = foxLifeExpectancy;
    }

    public int getFoxLifeExpectancy() {
        return foxLifeExpectancy;
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
}
