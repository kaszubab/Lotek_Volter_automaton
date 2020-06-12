package sample.DEElements;

public class DESimulation {

    double foxCount;
    double rabbitCount;
    double foxDeathRate;
    double foxBirthRate;
    double rabbitBirthRate;
    double h;

    public DESimulation(DESimulationConfiguration configuration){
        h = 0.1;
        this.foxCount = configuration.getInitialFoxCount();
        this.rabbitCount = configuration.getInitialRabbitCount();
        this.foxDeathRate = configuration.getFoxDeathPropability();
        this.foxBirthRate = configuration.getFoxBirthPropability();
        this.rabbitBirthRate = configuration.getRabbitBirthPropability();
    }

    public void calculateNextDay() {
        // Runge Kutta implementation
        double k1, k2, k3, k4, m1, m2, m3, m4;
        k1 = h * foxCountFunc(this.foxCount, this.rabbitCount);
        m1 = h * rabbitCountFunc(this.rabbitCount, this.foxCount);
        k2 = h * foxCountFunc(this.foxCount + 0.5  * k1, this.rabbitCount + 0.5 * m1);
        m2 = h * rabbitCountFunc(this.rabbitCount + 0.5  * m1, this.foxCount + 0.5 * k1);
        k3 = h * foxCountFunc(this.foxCount + 0.5  * k2, this.rabbitCount + 0.5  * m2);
        m3 = h * rabbitCountFunc(this.rabbitCount + 0.5  * m2, this.foxCount + 0.5  * k2);
        k4 = h * foxCountFunc(this.foxCount +  k3, this.rabbitCount +  m3);
        m4 = h * rabbitCountFunc(this.rabbitCount +  m3, this.foxCount +  k3);


        double newFoxes = this.foxCount + (k1 + 2*k2 + 2*k3 + k4)/6;
        double newRabbits = this.rabbitCount + (m1 + 2*m2 + 2*m3 + m4)/6;

        this.foxCount = newFoxes;
        this.rabbitCount = newRabbits;
    }

    private double foxCountFunc(double foxes, double rabbits){
        return -1 * foxes + foxBirthRate/10 * foxes * rabbits;
    }

    private double rabbitCountFunc(double rabbits, double foxes){
        return rabbits * rabbitBirthRate  - foxBirthRate * foxes * rabbits;
    }


    public double getFoxCount() {
        return foxCount;
    }

    public double getRabbitCount() {
        return rabbitCount;
    }
}
