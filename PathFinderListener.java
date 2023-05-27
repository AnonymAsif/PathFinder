import java.util.EventListener;

/**
 * PathFinderListener interface for maze solving related events
 * Listeners should be added using an addPathFinderListener() method
 *
 * @author Asif Rahman
 * @version 27/05/2023
 */

public interface PathFinderListener extends EventListener {
    /** Indicates that the maze has been solved
     * and a working path was found.
     *
     * @param e PathFinderEvent giving ranger details
     */
    void pathFound(PathFinderEvent e);

    /** Indicates that all maze paths have been explored
     * and no working path was found.
     *
     * @param e PathFinderEvent giving ranger details
     */
    void noPathFound(PathFinderEvent e);

    /** Indicates that the timer for the animation
     * has started and is running
     *
     * @param e PathFinderEvent giving ranger details
     */
    void timerStarted(PathFinderEvent e);

    /** Indicates that the timer for the animation
     * has stopped and is no longer running
     *
     * @param e PathFinderEvent giving ranger details
     */
    void timerStopped(PathFinderEvent e);


    /** Indicates that a single frame of the animation
     * has been skipped without the timer running
     *
     * @param e PathFinderEvent giving ranger details
     */
    void frameSkipped(PathFinderEvent e);

    /** Indicates that the PathFinder has reset
     * This means it is using the same maze and PathFinder object,
     * but the ranger and all traversal states are reset.
     *
     * @param e PathFinderEvent giving ranger details
     */
    void reset(PathFinderEvent e);
}
