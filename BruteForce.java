/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tssp;

import java.util.ArrayList;
import java.util.List;

public class BruteForce {
    
    public static int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    
    public Path bruteforce(ArrayList<Planet> thisPath, double velocity) {
        List<ArrayList<Planet>> results = new ArrayList<ArrayList<Planet>>();
        ArrayList<Planet> tmpList = new ArrayList<Planet>();
        tmpList.add(thisPath.get(0));
        results.add(tmpList);
        Path bestPath = new Path();
        bestPath.setPath(thisPath);
        int n = factorial(thisPath.size()-1);
        double bestDistance = bestPath.pathDistance(velocity);
        
        for (int j=1; j< thisPath.size(); j++) {
            Planet p = thisPath.get(j);
            int cur_size = results.size();
           
            //
            //create new permutations combining planet 'p' with each of the existing permutations
            //
            for (int i=cur_size-1; i>=0; i--) {
                ArrayList<Planet> str = results.remove(i);
                for(int l=0; l<= str.size(); l++) {
                    ArrayList<Planet> pathList = new ArrayList<Planet>(str.subList(0, l));
                    pathList.add(p);
                    for (int k=l; k<str.size(); k++)
                        pathList.add(str.get(k));
                    // We add the pathList to results only when cur_size is smaller
                    // than the number of possibilities, as we still need to grow 
                    // the path list
                    if (cur_size < n)
                        results.add(pathList); 

                    if (pathList.size() == thisPath.size()) {
                        Path tmpPath = new Path();
                        tmpPath.setPath(pathList);
                        double tmpPathDist = tmpPath.pathDistance(velocity);
                        //
                        // We only save the best path so far or we will quickly
                        // run out of memory
                        //
                        if (tmpPathDist < bestDistance) {
                            bestDistance = tmpPathDist;
                            bestPath = tmpPath;
                        }
                    }
                }
            }
        }
        System.out.println("Number of Permutations: " + n*thisPath.size());
        return bestPath;
     }
}
