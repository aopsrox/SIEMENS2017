/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

public class TSSP {

    private static final double G = 6.67; // We need to find a good way to represent G
    private static double mutationRate = 0.05;
    private static final int groupSize = 5;
    
    // Find partner (parent) for crossover. We select a smaller subset of the
    // population first, and then return the best individual in that subset. This 
    // is analogous to how partners are selected by individuals in nature
    //
    private static Path findPartner(Population pop, double velocity) {
        // Create a tournament population
        Population group = new Population(groupSize);
        // For each place in the tournament get a random candidate tour and
        // add it
        for (int i = 0; i < groupSize; i++) {
            int randomId = (int) (Math.random() * pop.numberOfIndivduals());
            group.registerPath(i, pop.returnPath(randomId));
        }
        // Find the best tour (one with the shortest distance) and return it
        Path fittest = group.returnFittest(velocity);
        return fittest;
    }
    
    // Generate an offspring by applying crossover on the parents
    //
    public static Path crossover(Path mother, Path father) {
        // Create new offSpring tour
        Path offSpring = new Path();

        // Find a segment on mother's chromosome to apply crossover.
        // We randomly generate a startPos and endPos to identify the
        // segment that will undergo crossover
        // Crossover is applied to the planets from loc1 to loc2, inclusive
        //
        int loc1 = (int) (Math.random() * mother.numberOfPlanets());
        int loc2 = (int) (Math.random() * mother.numberOfPlanets());
        
        if (loc1 > loc2) {
            int tempLoc = loc2;
            loc2 = loc1;
            loc1 = tempLoc;
        }

        // Loop and add the sub tour from mother to our offSpring
        for (int i = 0; i < offSpring.numberOfPlanets(); i++) {
            if (i > loc1 && i < loc2) {
                offSpring.setPlanet(i, mother.getPlanet(i));
            } 
        }

        // Now, we go through father's path, and add his planets to the
        // offspring if offspring doesn't have this planet. This mimics
        // crossovers in nature
        for (int i = 0; i < father.numberOfPlanets(); i++) {
            // Only if offSpring doesn't have the planet add it
            if (!offSpring.containsPlanet(father.getPlanet(i))) {
                // Check for empty positions in the offSpring's tour and
                // father's planet i
                for (int j = 0; j  < offSpring.numberOfPlanets(); j++) {
                    // Spare position found, add planet
                    if (offSpring.getPlanet(j) == null) {
                        offSpring.setPlanet(j, father.getPlanet(i));
                        break;
                    }
                }
            }
        }
        return offSpring;
    }
    
    
    // Mutate an offspring
    private static void mutate(Path tour) {
        for(int loc1=0; loc1 < tour.numberOfPlanets(); loc1++){
            if(Math.random() < mutationRate){
                
                // We will select a random position on the path and
                // swap the planets
                int loc2 = (int) (tour.numberOfPlanets() * Math.random());
                
                // Save the planet at loc2 at tmpPlanet
                Planet tmpPlanet = tour.getPlanet(loc2);
                
                // Swap the planets, which is equivalent to mutating
                // the current path (chromosome)
                tour.setPlanet(loc2, tour.getPlanet(loc1));
                tour.setPlanet(loc1, tmpPlanet);
            }
        }
    }
    
    
    // Evolves the current population by applying crossover and
    // mutation. The population returned by this method is guaranteed to
    // be at least equal or better than the previous generation
    //
    public static Population evolveGeneration(Population pop, double velocity) {
        Population nextGeneration = new Population(pop.numberOfIndivduals());

        // We save the best individual (tour) at position 0, so as to
        // apply the concept of elitism here. 
        nextGeneration.registerPath(0, pop.returnFittest(velocity));
   
        // Apply crossover among individuals of the current population.
        // The resulting offsprings form the next generation
        // After applying a crossover, apply a little bit of mutation
        // to the current path
        //
        for (int i = 1; i < nextGeneration.numberOfIndivduals(); i++) {
            // Select parents for crossover
            Path mother = findPartner(pop, velocity);
            Path father = findPartner(pop, velocity);
            // Now crossover them
            Path offSpring = crossover(mother, father);
            // Add offSpring to the new population
            nextGeneration.registerPath(i, offSpring);
            
            // Based on the pre-set mutation rate, mutate the individuals
            // in the new population
            //
            mutate(nextGeneration.returnPath(i));
        }

        return nextGeneration;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double massOfStar = 1000; // In solar mass
        int noOfPlanets = 1000;
        double speedOfClosestPlanet = 0.0;
        
        // Create a solar system for our problem
        SolarSystem ss = new SolarSystem(noOfPlanets, massOfStar);
        
        Planet closest = ss.closestPlanet();
        double closestVelocity = (closest.getRadius())*(closest.getAngularVelocity());
        Spaceship rocket = new Spaceship(Math.ceil(closestVelocity));
        
        //
        // Add the planets in the solar system to create a single 
        // individual
        for (int i = 0; i < noOfPlanets; i++) {
            Individual.addPlanet(ss.getPlanet(i));
        }
        
        // Get the velocity of the rocket
        double v = rocket.getVelocity();
        
        // Start with a small population
        Population pop = new Population(300);
       
        // Apply GA until sufficient convergence
        int prevBest = 100000000;
        
        long startTime = System.currentTimeMillis();
        
        int iter = 0;
        
        // We try 5 times to make sure population has indeed converged. For a fair
        // comparision with the brute-force method, we should only try once
        for (int i = 0; i < 5; i++) {   
            pop = evolveGeneration(pop, v);
            while (prevBest - pop.returnFittest(v).pathDistance(v) > 0.0000001) {
                prevBest = pop.returnFittest(v).pathDistance(v);
                pop = evolveGeneration(pop, v);
                System.out.println("Generation: " + iter + " Best distance so far: " + pop.returnFittest(v).pathDistance(v));
                iter++;
            }
            prevBest = pop.returnFittest(v).pathDistance(v);
            mutationRate = mutationRate + 0.003;   // Tweak the mutation rate for next generation
        }
        
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        //
        // When the number of planets, N >= 9, we see the power of
        // TSSP-GA immediately. At N = 9, TSSP-GA almost always finds the
        // best path at a fraction of time than TSSP-BF.
        //
        // Print tthe best path info for GA
        System.out.println(" ");
        System.out.println("********************** TSSP-GA **********************");
        System.out.println("Number of generations: " + iter);
        System.out.println("Best distance: " + pop.returnFittest(v).pathDistance(v));
        System.out.println("Best path: " + pop.returnFittest(v)); 
        System.out.println("Time taken by TSSP-GA: " + totalTime/5 + " ms");
        System.out.println("*****************************************************");
        System.out.println(" ");
        
        // Print tthe best path info for BF
        System.out.println("********************** TSSP-BF **********************");
        BruteForce permutation = new BruteForce();
        startTime = System.currentTimeMillis();
        // Path bestPath = permutation.bruteforce(pop.returnPath(0).getPath(), rocket.getVelocity());
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        //System.out.println("Best distance: " + bestPath.pathDistance(v));
       // System.out.println("Best path: " + bestPath); 
        //System.out.println("Time taken by TSSP-BF: " + totalTime + " ms");
        System.out.println("*****************************************************"); 
        System.out.println("No of planets = " + noOfPlanets);  
    }
    
}
