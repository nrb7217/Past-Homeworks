package soltrchess.backtracking;

import java.util.Collection;

/**
 * The representation of a single configuration for a puzzle.
 * The solitarechess.backtracking.Backtracker depends on these routines in order to
 * solve a puzzle.  Therefore, all puzzles must implement this
 * interface.
 *
 * @author RIT CS
 */
public interface Configuration {
    /**
     * Get the collection of successors from the current one.
     *
     * @return All successors, valid and invalid
     */
    public Collection< Configuration > getSuccessors();

    /**
     * Is the current configuration valid or not?
     *
     * @return true if valid; false otherwise
     */
    public boolean isValid(Configuration config);

    /**
     * Is the current configuration a goal?
     * @return true if goal; false otherwise
     */
    public boolean isGoal();

    /**
     * Get Board
     * @return
     */
    public String[][] getBoard();
}