/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

/**
 *
 * @author Anusha
 */
public class Spaceship {
    public double velocity; // In AU per year
	
    public Spaceship(double v) {
	velocity = v;
    }
        
    public double getVelocity() {
        return velocity;
    }
}
