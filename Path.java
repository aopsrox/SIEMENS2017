/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Anusha
 */
public class Path {
    //
    // Maintain the path as an array of planets
    //
    private ArrayList<Planet> path = new ArrayList<Planet>();
    private int distance = 0;
    private double fitness = 0;

    // Constructor for a path connecting the planets
    //
    public Path(){
        for (int i = 0; i < Individual.numberOfPlanets(); i++) {
            path.add(null);
        }
    }

    // From the given planets, create a path (individual)
    //
    public void createPath() {
        for (int i = 0; i < Individual.numberOfPlanets(); i++) {
          setPlanet(i, Individual.getPlanet(i));
        }
        // shuffle the planets
        Collections.shuffle(path);
    }

    // Return the planet indicated by the position in the path
    //
    public Planet getPlanet(int pathPosition) {
        return (Planet)path.get(pathPosition);
    }

    // Assigns a planet in a given position within a path
    //
    public void setPlanet(int pathPosition, Planet planet) {
        path.set(pathPosition, planet);
        // Reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // This is our fitness function. Since we want to minimize
    // the path distance, we set fitness to the inverse of the
    // path distance. 
    //
    public double getFitness(double velocity) {
        return 1/(double)pathDistance(velocity); 
    }

    // Is the planet already part of this path?
    //
    public boolean containsPlanet(Planet planet){
        return path.contains(planet);
    }

    // Gets the total distance of the path. The path distance
    // depends on the velocity of the spaceship, as the destination
    // planet would be moving continuously during the journey of
    // the spaceship.
    public int pathDistance(double velocity){
        if (distance == 0) {
            int totalDistance = 0;
            Planet planet1;
            Planet planet2;
            double currentTime = 0.0;
            
            // Traverse through the entire path and add the distances
            // between adjacent planets
            for (int i=0; i < numberOfPlanets(); i++) {
                // Get the first planet in the path
                planet1 = getPlanet(i);
               
                // Note that the next planet to the last planet in the path is
                // the first planet. So, set planet2 accordingly
                if (i == numberOfPlanets()-1)
                    planet2 = getPlanet(0);
                else
                    planet2 = getPlanet(i+1);
                
                // Now add the distance between planet1 and planet2
                // to totalDistance
                //
                totalDistance = totalDistance + planet1.getDistance(planet2, velocity);
            }
            distance = totalDistance;
        }
        return distance;
    }

    // Return the number of planets currently in the path
    //
    public int numberOfPlanets() {
        return path.size();
    }

    
    // We print planet's orbital radius to identify it. Perhaps we should label
    // each planet uniquely and then print its name.
    @Override
    public String toString() {
        String chromosome = "";
        for (int i = 0; i < numberOfPlanets(); i++) {
            chromosome = chromosome + "(" + getPlanet(i).name + ")";
            if (i != numberOfPlanets()-1)
                chromosome = chromosome + "->";
        }
        return chromosome;
    }
    
}
