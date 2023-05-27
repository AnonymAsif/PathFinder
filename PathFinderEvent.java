import java.util.EventObject;

/**
 * PathFinderEvent class for maze solving related events
 * to give information to event listeners
 *
 * @author Asif Rahman
 * @version 27/05/2023
 */

public class PathFinderEvent extends EventObject {

    /** The location of the Ranger in the maze,
     * given with an X and Y index at the time of the event.*/
    public PathFinder.Coordinate2D rangerLocation;

    /** The direction that the Ranger was facing
     * at the time of the event. */
    public PathFinder.Directions rangerDirection;

    /**
     * Constructs a PathFinder Event, saving the PathFinder source
     * as well as Ranger details
     *
     * @param source the PathFinder on which the Event initially occurred
     * @throws IllegalArgumentException if source, rangerLocation or rangerDirection are null
     */
    public PathFinderEvent(PathFinder source, PathFinder.Coordinate2D rangerLocation, PathFinder.Directions rangerDirection) {
        // EventObject constructor
        super(source);

        // Throws errors if any parameters are null
        if (rangerLocation == null) {
            throw new IllegalArgumentException("Ranger Location cannot be null");
        }
        if (rangerDirection == null) {
            throw new IllegalArgumentException("Ranger direction cannot be null");
        }

        // Saves ranger details
        this.rangerLocation = rangerLocation;
        this.rangerDirection = rangerDirection;

    }
}
