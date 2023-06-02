import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Reads and writes Mazes to files
 * Mazes include trails, trees, location of the ranger and cabin
 * 
 *
 * @author Asif Rahman
 * @version 01/06/2023
 */
public class MazeFileHandler
{
    // Enum of possible maze characters
    private enum MazeStates {

        // Each maze state and its code
        TRAIL('_'),
        TREE('T'),
        RANGER('R'),
        CABIN('C');

        // Code that represents this state in file
        private final char CODE;

        MazeStates(char code) {
            CODE = code;
        }

        // Getter for code
        public char getCode() {
            return CODE;
        }
    }

    // File where the maze is written to
    private final String MAZE_FILE_PATH = "maze.txt";
    
    // Maze to parse, read and write from/to file
    private PathBlock[][] maze;

    // The index of the ranger
    MazePanel.Coordinate2D rangerIndex;
    
    // Reads maze from maze file
    public void readMaze() throws FileNotFoundException {
        // Scanner to read from file
        Scanner scanner = new Scanner(new File(MAZE_FILE_PATH));


    }
    
    // Writes maze to maze file
    public void writeMaze() throws FileNotFoundException {
        // PrintWriter to write to file
        try (PrintWriter writer = new PrintWriter(MAZE_FILE_PATH)) {
            // For each block in each row
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze[i].length; j++) {
                    // Saves current block
                    PathBlock block = maze[i][j];

                    // The code to be outputted for this block
                    char currentCode;

                    // If it is a Tree
                    if (block instanceof Tree)
                        currentCode = MazeStates.TREE.getCode();

                    // It is a Trail object, check if it is a cabin
                    else if (((Trail)block).getTraversalState() == Trail.TraversalState.CABIN) {
                        currentCode = MazeStates.CABIN.getCode();
                    }

                    // Check if the starting index is there, so the ranger will be there
                    else if (rangerIndex.y() == i && rangerIndex.x() == j) {
                        currentCode = MazeStates.RANGER.getCode();
                    }

                    // It is just an empty Trail
                    else currentCode = MazeStates.TRAIL.getCode();

                    // Now that the current code has been received, output it to the file
                    writer.print(currentCode);
                }

                // Skip a line after each row
                writer.println();
            }
        }
    }
}
