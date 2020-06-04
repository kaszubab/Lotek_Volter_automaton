package sample.worldElements;

import sample.worldElements.animals.Animal;
import sample.worldElements.animals.Fox;
import sample.worldElements.animals.Rabbit;

import java.util.*;

public class World {

    private Vector leftLower;
    private Vector rightUpper;
    private int foxLifeExpectancy;
    private List<Fox> foxList;
    private List<Rabbit> rabbitList;
    private Map<Vector, Animal> map;

    public World(WorldConfiguration config) {
        leftLower = new Vector(0,0);
        rightUpper = new Vector(config.getWidth(), config.getHeight());
        this.foxLifeExpectancy = config.getFoxLifeExpectancy();

        this.foxList = new LinkedList<>();
        this.rabbitList = new LinkedList<>();

        this.map = new HashMap<>();
        for( int i = 0; i < config.getWidth(); i++) {
            for( int j = 0; j < config.getHeight(); j++) {
                map.put(new Vector(i,j), null);
            }
        }

        Random random = new Random();

        for(int i = 0; i < config.getInitialFoxCount(); i++) {
            Vector foxPosition;
            do {
                foxPosition = new Vector(random.nextInt(config.getWidth()),
                                         random.nextInt(config.getHeight()));
            } while (this.map.get(foxPosition) != null);

            Fox newFox = new Fox(foxPosition, this.foxLifeExpectancy);
            this.map.put(foxPosition, newFox);
            this.foxList.add(newFox);
        }

        for(int i = 0; i < config.getInitialRabbitCount(); i++) {
            Vector rabbitPosition = null;
            do {
                rabbitPosition = new Vector(random.nextInt(config.getWidth()),
                        random.nextInt(config.getHeight()));
            } while (this.map.get(rabbitPosition) != null);
            Rabbit rabbit = new Rabbit(rabbitPosition);
            this.map.put(rabbitPosition, rabbit);
            this.rabbitList.add(rabbit);
        }
    }

    public void run() {

        Set<Rabbit> rabbitsToAdd = new HashSet<>();
        Random random = new Random();

        for (Rabbit rabbit : this.rabbitList) {

            List<Vector> viablePositions = getNotOccupiedPositions(rabbit.getPosition());

            if (viablePositions.size() > 0) {
                int randIndex = random.nextInt(viablePositions.size());
                this.map.put(rabbit.getPosition(), null);
                this.map.put(viablePositions.get(randIndex), rabbit);
                rabbit.move(viablePositions.get(randIndex));
                viablePositions.remove(randIndex);
            }

            while (viablePositions.size() > 0) {
                int randIndex = random.nextInt(viablePositions.size());
                Rabbit newRabbit = new Rabbit(viablePositions.get(randIndex));
                this.map.put(newRabbit.getPosition(), newRabbit);
                rabbitsToAdd.add(newRabbit);
                viablePositions.remove(randIndex);
            }


        }

        this.rabbitList.addAll(rabbitsToAdd);



        Set<Rabbit> rabbitsToDelete = new HashSet<>();
        Set<Fox> foxesToAdd = new HashSet<>();
        Set<Fox> foxesToDelete = new HashSet<>();

        System.out.println(this.foxList.size());

        for(Fox fox : this.foxList) {

            List<Vector> rabbits = getNearbyRabbits(fox.getPosition());

            if (rabbits.size() > 0) {
                //fox.eat();
                int randIndex = random.nextInt(rabbits.size());
                Fox newFox = new Fox(rabbits.get(randIndex), this.foxLifeExpectancy);
                Rabbit rabbit = (Rabbit) this.map.get(newFox.getPosition());
                this.map.put(newFox.getPosition(), newFox);
                rabbitsToDelete.add(rabbit);
                foxesToAdd.add(newFox);
            }



            if (!fox.surviveDay()) {
                this.map.put(fox.getPosition(), null);
                foxesToDelete.add(fox);
                continue;
            }

            List<Vector> viablePositions = getNotOccupiedPositions(fox.getPosition());

            if (viablePositions.size() > 0) {
                int randIndex = random.nextInt(viablePositions.size());
                this.map.put(fox.getPosition(), null);
                this.map.put(viablePositions.get(randIndex), fox);
                fox.move(viablePositions.get(randIndex));
            }


        }
        System.out.println(foxesToDelete.size());

        this.foxList.removeAll(foxesToDelete);

        System.out.println(this.foxList.size());
        this.rabbitList.removeAll(rabbitsToDelete);
        System.out.println(foxesToAdd.size());

        this.foxList.addAll(foxesToAdd);

        System.out.println(this.foxList.size());

    }


    private List<Vector> getNotOccupiedPositions(Vector position) {
        List<Vector> freePositions = new ArrayList<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Vector newPosition = new Vector(position.getX() + i, position.getY() + j);
                if(leftLower.precedes(newPosition) && rightUpper.follows(newPosition) &&
                        this.map.get(newPosition) == null) {
                    freePositions.add(newPosition);
                }
            }
        }

        return freePositions;
    }

    private List<Vector> getNearbyRabbits(Vector position) {
        List<Vector> nearbyRabbits = new ArrayList<>();

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                Vector newPosition = new Vector(position.getX() + i, position.getY() + j);
                if(leftLower.precedes(newPosition) && rightUpper.follows(newPosition) &&
                        this.map.get(newPosition) instanceof Rabbit) {
                    nearbyRabbits.add(newPosition);
                }
            }
        }

        return nearbyRabbits;
    }


    public List<Fox> getFoxList() {
        return foxList;
    }

    public List<Rabbit> getRabbitList() {
        return rabbitList;
    }
}
