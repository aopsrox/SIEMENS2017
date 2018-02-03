/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

public class SolarSystem {
    Planet[] planetArray; // Array of planets in the solar system
    static double massOfSun; // Mass of the star
    static int noOfPlanets; // Number of planets in the solar system
    
    public SolarSystem(int numberOfPlanets, double massOfStar) { 
		
	planetArray = new Planet[numberOfPlanets];
        int MAX_ORBITAL_RADIUS = 2000;
                
        // Make sure that numberOfPlanets is <= MAX_ORBITAL_RADIUS.
        // This is because there could be only upto MAX_ORBITAL_RADIUS number of
        // distinct radii in the solar system as we assume radius to be integer
        // And we don't want to put two planets in the same orbit!
        //
        if (numberOfPlanets > MAX_ORBITAL_RADIUS)
            numberOfPlanets = MAX_ORBITAL_RADIUS;
        
        for (int i = 0; i < numberOfPlanets; i++) {
            int orbitalRadius = (int) Math.ceil(Math.random()*MAX_ORBITAL_RADIUS);
            
            // If the current orbit is already occupied by a planet
            // create another one
            while (orbitOccupied(orbitalRadius, i))
                orbitalRadius = (int) Math.ceil(Math.random()*MAX_ORBITAL_RADIUS);
            
            double initAngle = (double) Math.random()*2*Math.PI;
            
            String planetName = "P" + i;
            planetArray[i] = new Planet(orbitalRadius, massOfStar, initAngle, planetName);
        }    
	massOfSun = massOfStar;
        noOfPlanets = numberOfPlanets;
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
    
    //
    // Return the closest planet to the star. This planet has the highest
    // angular velocity. We want to make sure that our spaceship is faster than
    // the linear velocity of this planet.
    //
     public Planet closestPlanet() {
        Planet closest = getPlanet(0);
        for (int i=1; i < noOfPlanets; i++) {
            Planet tmpPlanet = getPlanet(i);
            if (tmpPlanet.getRadius() < closest.getRadius())
                closest = tmpPlanet;
        }
        return closest;
    }


}
