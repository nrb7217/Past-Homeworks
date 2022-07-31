package soltrchess.backtracking;/*
 * solitarechess.backtracking.Backtracker.java
 *
 * This file comes from the solitarechess.backtracking lab. It should be useful
 * in this project. A second method has been added that you should
 * implement.
 */

import java.util.List;
import java.util.Optional;

/**
 * This class represents the classic recursive solitarechess.backtracking algorithm.
 * It has a solver that can take a valid configuration and return a
 * solution, if one exists.
 * 
 * @author RIT CS
 */
public class Backtracker {
    
    /**
     * Try find a solution, if one exists, for a given configuration.
     * 
     * @param config A valid configuration
     * @return A solution config, or null if no solution
     */
    public Optional<Configuration> solve(Configuration config) {
        if (config.isGoal()) {
            return Optional.of(config);
        } else {
            for (Configuration child : config.getSuccessors()) {
                if (child.isValid(config)) {
                    Optional<Configuration> sol = solve(child);
                    if (sol.isPresent()) {
                        return sol;
                    }
                }
            }
            // implicit solitarechess.backtracking happens here
        } 
        return Optional.empty();
    }

    /**
     * Find a goal configuration if it exists, and how to get there.
     * @param current the starting configuration
     * @return a list of configurations to get to a goal configuration.
     *         If there are none, return null.
     */
    public List< Configuration > solveWithPath( Configuration current ) {
        // YOUR CODE HERE
        return null;
    }

}
