/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

public class Population {
    // Holds population of individuals
    Path[] individuals;

    // Constructor for the current generation
    public Population(int numberOfIndivduals) {
        individuals = new Path[numberOfIndivduals];
        
        for (int i = 0; i < numberOfIndivduals(); i++) {
            Path newPath = new Path();
            newPath.createPath();
            registerPath(i, newPath);
        }
    }

    
    // Gets population size
    public int numberOfIndivduals() {
        return individuals.length;
    }
    
    
    // Register a path in the plan
    //
    public void registerPath(int index, Path currentPath) {
        individuals[index] = currentPath;
    }

    // Returns a path (individual) from population
    //
    public Path returnPath(int index) {
        return individuals[index];
    }

    
    // Returns the best path in the current generation
    //
    public Path returnFittest(double velocity) {
        Path fittest = individuals[0];
        // Loop through individuals to find fittest
        for (int i = 1; i < numberOfIndivduals(); i++) {
            if (fittest.getFitness(velocity) <= returnPath(i).getFitness(velocity)) {
                fittest = returnPath(i);
            }
        }
        return fittest;
    }
}
