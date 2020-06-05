package sample.worldElements;

import sample.worldElements.animals.Animal;
import sample.worldElements.animals.Fox;
import sample.worldElements.animals.Rabbit;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class World {

    private final Vector leftLower;
    private final Vector rightUpper;
    private double foxDeathPropability;
    private double huntPropability;
    private double rabbitBirthPropability;
    private Map<Vector, Animal> map;

    public World(WorldConfiguration config) {
        leftLower = new Vector(0,0);
        rightUpper = new Vector(config.getWidth(), config.getHeight());
        this.foxDeathPropability = config.getFoxDeathPropability();
        this.huntPropability = config.getFoxBirthPropability();
        this.rabbitBirthPropability = config.getRabbitBirthPropability();
        this.map = new HashMap<>();

        Random random = new Random();

        for(int i = 0; i < config.getInitialFoxCount(); i++) {
            Vector foxPosition;
            do {
                foxPosition = new Vector(random.nextInt(config.getWidth()),
                                         random.nextInt(config.getHeight()));
            } while (this.map.getOrDefault(foxPosition, null) != null);
            Fox newFox = new Fox(foxPosition);
            this.map.put(foxPosition, newFox);
        }

        for(int i = 0; i < config.getInitialRabbitCount(); i++) {
            Vector rabbitPosition = null;
            do {
                rabbitPosition = new Vector(random.nextInt(config.getWidth()),
                        random.nextInt(config.getHeight()));
            } while (this.map.getOrDefault(rabbitPosition, null) != null);
            Rabbit rabbit = new Rabbit(rabbitPosition);
            this.map.put(rabbitPosition, rabbit);
        }
    }

    public void run() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());

        List<Vector> toDelete  = new LinkedList<>();
        final List<Animal> toAdd = new LinkedList<>();

        List<Animal> animals = new LinkedList<>(this.map.values()){};

        animals.forEach( k ->{
            if( k instanceof Fox) {
                if (rand.nextDouble() > this.foxDeathPropability) {
                    List<Vector> rabbits = getNearbyRabbits(k.getPosition());
                    if (rabbits != null) {
                        if(rand.nextDouble() < huntPropability) {
                            Vector eatenRabbit = rabbits.get(rand.nextInt(rabbits.size()));
                            this.map.put(eatenRabbit, new Fox(eatenRabbit));
                        }
                    } else {
                        this.map.remove(k.getPosition());
                    }
                }
                else {
                    this.map.remove(k.getPosition());
                }
            }
        });

        animals = new LinkedList<>(this.map.values()){};

        animals.forEach( k ->{
            if( k instanceof Rabbit) {
                List<Vector> emptyPositions = getNotOccupiedPositions(k.getPosition());
                if (emptyPositions != null) {
                    if (rand.nextDouble() < this.rabbitBirthPropability) {
                        Vector newPosition = emptyPositions.get(rand.nextInt(emptyPositions.size()));
                        this.map.put(newPosition, new Rabbit(newPosition));
                    }

                }
            }
        });

        animals = new LinkedList<>(this.map.values()){};

        animals.forEach( k ->{
            List<Vector> emptyPositions = getNotOccupiedPositions(k.getPosition());
            if (emptyPositions != null) {
                Vector newPosition = emptyPositions.get(rand.nextInt(emptyPositions.size()));
                this.map.remove(k.getPosition());
                k.move(newPosition);
                this.map.put(newPosition, k);
            }
        });

    }


    private List<Vector> getNotOccupiedPositions(Vector position) {
        List<Vector> freePositions = new ArrayList<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Vector newPosition = new Vector(position.getX() + i, position.getY() + j);
                if(leftLower.precedes(newPosition) && rightUpper.follows(newPosition) &&
                        !this.map.containsKey(newPosition)) {
                    freePositions.add(newPosition);
                }
            }
        }

        if (freePositions.size() > 0) {
            return freePositions;
        }
        else {
            return null;
        }
    }

    private List<Vector> getNearbyRabbits(Vector position) {
        List<Vector> nearbyRabbits = new ArrayList<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Vector newPosition = new Vector(position.getX() + i, position.getY() + j);
                if(leftLower.precedes(newPosition) && rightUpper.follows(newPosition) &&
                        this.map.getOrDefault(newPosition, null) instanceof Rabbit) {
                    nearbyRabbits.add(newPosition);
                }
            }
        }

        if (nearbyRabbits.size() > 0)
            return nearbyRabbits;
        else
            return null;
    }

    public List<Animal> getAnimalList() {
        final List<Animal> animals = new LinkedList<>();
        animals.addAll(this.map.values());
        return animals;
    }


}
