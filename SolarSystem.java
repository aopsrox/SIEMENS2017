/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tsp;

/**
 *
 * @author Anusha
 */
public class SolarSystem {
    
    Planet[] planetArray; // Array of planets in the solar system
    static double massOfSun; // Mass of the star
    
    public SolarSystem(int numberOfPlanets, double massOfStar) { 
		
	planetArray = new Planet[numberOfPlanets];
                
        for (int i = 0; i < numberOfPlanets; i++) {
            int orbitalRadius = (int) Math.ceil(Math.random()*100);
            
            // If the current orbit is already occupied by a planet
            // create another one
            while (orbitOccupied(orbitalRadius, i))
                orbitalRadius = (int) Math.ceil(Math.random()*100);
            
            double initAngle = (double) Math.random()*2*Math.PI;
            
            String planetName = "P" + i;
            planetArray[i] = new Planet(orbitalRadius, massOfStar, initAngle, planetName);
        }    
	massOfSun = massOfStar;
    }
    
    //
    // Return the mass of the star for this solar system
    //
    public double getMassOfStar() {
        return massOfSun;
    }
    
    //
    // Check if this orbit is already occupied by a planet
    // This method is useful because we don't want to have two planets in the
    // same orbit
    //
    private boolean orbitOccupied(int orbitalRadius, int maxPlanets) {
        boolean found = false;
        int i = 0;
        while (!found && i < maxPlanets) {
          if (planetArray[i].getRadius() == orbitalRadius)
              found = true;
          i++;
        }
        return found;
    }
    
    public Planet getPlanet(int i) {
        return planetArray[i];
    }
}
