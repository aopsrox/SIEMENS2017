/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

import java.util.ArrayList;

public class Individual {

    // Number of planets is initialized in TSP.java. This array
    // holds these planets
    private static ArrayList<Planet> planetsToVisit = new ArrayList<Planet>();

    // Return the number of planets in the array
    //
    public static int numberOfPlanets(){
        return planetsToVisit.size();
    }
    
    // Add a planet in the path
    public static void addPlanet(Planet planet) {
        planetsToVisit.add(planet);
    }

    // Get a planet from the array
    public static Planet getPlanet(int index){
        return (Planet)planetsToVisit.get(index);
    }
}
