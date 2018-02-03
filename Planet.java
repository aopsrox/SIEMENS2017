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
public class Planet {
    public double radius;         //in au
    public double GM;             //in solar masses
    public double initAngle;      //in radians
    public String name; 
	
    public Planet(double rad, double gm, double startAngle, String label) {
	radius = rad;
	GM = gm;
	initAngle = startAngle;
        name = label;           // Unique name for the planet
    }
	
    // 
    // Return the position of the planet in polar coordinates at a given time
    // The first position of coords gives the orbital radius of the planet and
    // the second position of the coords gives the angle of the planet in 
    // radian
    //
    public double[] getCoords(double time) {  //return polar coordinates of planet 
	double[] coords = new double[2];
        
	coords[0] = radius;
        // Current angle = omega*time + initAngle
        coords[1]=((getAngularVelocity())*time + initAngle) % 2*Math.PI;
        
        return coords;
    }
    
    //
    // Return the radius of the planet
    //
    public double getRadius() {
        return radius;
    }
    
    // 
    // Return the initial angle of the planet
    //
    public double getInitAngle() {
        return initAngle;
    }
    
    // Return the distance to a given planet from the
    // current planet
    public int getDistanceOLD(Planet planet){
        // polar to Cartesian
        double x = Math.cos(planet.getInitAngle()) * planet.getRadius();
        double y = Math.sin(planet.getInitAngle()) * planet.getRadius();
       
        double deltaX = Math.abs(Math.cos(getInitAngle())*radius - x);
        double deltaY = Math.abs(Math.sin(getInitAngle())*radius - y);
       
        return (int)Math.round(Math.sqrt((deltaX*deltaX) + (deltaY*deltaY)));
    }
    
    
    // ******* ANUSHA ********
    
    //
    // Return the angular velocity of the planet
    //
    public double getAngularVelocity() {
        return (Math.sqrt(GM/(Math.pow(radius, 3))));
    }
    
    // Return f(x)=px^2+q*cos(x+k)-r
    public double fx(double x, Planet A, Planet B, double velocity, double solarMass) {
        double r = Math.pow(A.getRadius(),2)+Math.pow(B.getRadius(), 2);
	double q = 2*(A.getRadius())*(B.getRadius());
	double p = (Math.pow(velocity, 2))*(Math.pow(B.getRadius(), 3))/(solarMass*Math.pow(2*Math.PI,2));
	
        return (p*Math.pow(x, 2)+q*Math.cos(x + B.getInitAngle() - A.getInitAngle()) - r);
    }
    
    
    public double fxprime(double x, Planet A, Planet B, double velocity, double solarMass) {
        double p = (Math.pow(velocity, 2))*(Math.pow(B.getRadius(), 3))/(solarMass*Math.pow(2*Math.PI,2));
        double q = 2*(A.getRadius())*(B.getRadius());
        
        // Return the derivative of fx() at x
        return (2*x*p - q*(Math.sin(x + B.getInitAngle() - A.getInitAngle()))); 
    }
    
    public double solveFx(double x, Planet A, Planet B, double velocity, double solarMass) {
        double tolerance = .000000001; // Stop if you're close enough
        int max_count = 2000; // Maximum number of Newton's method iterations

        for( int count=1;
            //Carry on till we're close, or we've run it 200 times.
            (Math.abs(fx(x, A, B, velocity, solarMass)) > tolerance) && ( count < max_count); 
             count ++)  {

            x= x - fx(x, A, B, velocity, solarMass)/fxprime(x, A, B, velocity, solarMass);  //Newtons method.
        }     
        
       /* if (x < 0)
        {
            System.out.println("fx(x) =" + fx(x, A, B, velocity, solarMass));
            System.out.println("fx(-x) =" + fx(2*Math.PI+x, A, B, velocity, solarMass));
        } */
        
        // We have identified the root. Return it.      
        if (Math.abs(fx(x, A, B, velocity, solarMass)) <= tolerance) {
            // System.out.println("**** CONVERGED ****");
            return x;
        }
        else {
            return 0;
        }
        
    }
    
    // Return the distance to a given planet from the
    // current planet
    public int getDistance(Planet planet, double velocity){
        
        Planet P1 = this;
        Planet P2 = planet;
        
        double alpha = solveFx(0.01, P1, P2, velocity, 10);
        
        // System.out.println("alpha = " + alpha);
        
        // polar to Cartesian
        double x = Math.cos(planet.getInitAngle()+alpha) * planet.getRadius();
        double y = Math.sin(planet.getInitAngle()+alpha) * planet.getRadius();
       
        double deltaX = Math.abs(Math.cos(getInitAngle())*radius - x);
        double deltaY = Math.abs(Math.sin(getInitAngle())*radius - y);
       
        return (int)Math.round(Math.sqrt((deltaX*deltaX) + (deltaY*deltaY)));
    }
    
    // ******* ANUSHA *********
    
    
    
    public String getName(Planet planet) {
        return name;
    }
}
