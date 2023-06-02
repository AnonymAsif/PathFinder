import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;

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
    
    // Dimensions of the maze
    private int mazeHeight;
    private int mazeWidth;
    
    // Dimensions of each block
    private int blockHeight;
    private int blockWidth;

    // The index of the ranger
    MazePanel.Coordinate2D rangerIndex;
    
    // Reads maze from maze file
    public void readMaze() throws IOException {
        // Scanner to read from file
        Scanner scanner = new Scanner(new File(MAZE_FILE_PATH));
        
        // ArrayList to hold every line read
        ArrayList<String> lines = new ArrayList<>();
        
        // While there are lines to read from the file
        while (scanner.hasNextLine()) {
            // Inputs the line
            String line = scanner.nextLine();
            
            // If the line isn't the same length as the width, throw an exception
            if (line.length() != mazeWidth) 
                throw new IOException("Unexpected width of maze");
            
            
            // Add the line to the ArrayList
            lines.add(line);
        }
        
        // Verifies the length of the ArrayList is the expected height
        if (lines.size() != mazeHeight)
            throw new IOException("Unexpected height of maze");
        
        // Creates a new maze
        PathBlock[][] newMaze = new PathBlock[mazeHeight][mazeWidth];
        
        // Creates a new ranger index
        MazePanel.Coordinate2D newRangerIndex = null;
        
        // Keeps track of if a cabin was set
        boolean foundCabin = false;
        
        // Creates a new maze from file
        for (int i = 0; i < mazeHeight; i++) {
            // Gets the current line
            String line = lines.get(i);
            
            for (int j = 0; j < mazeWidth; j++) {
                // Sets the PathBlock to the appropriate type based on charater
                PathBlock newBlock;
                
                // Character represents a tree, set it to a new tree
                if (line.charAt(j) == MazeStates.TREE.getCode())
                    newBlock = new Tree(j * blockWidth, i * blockHeight, blockWidth, blockHeight);
                    
                // Character represents a trail, make a new Trail
                else {
                    newBlock = new Trail(j * blockWidth, i * blockHeight, blockWidth, blockHeight);
                    
                    // It may be a cabin or ranger
                    // If it's a cabin, set the traversal state of the Trail to CABIN
                    if (line.charAt(j) == MazeStates.CABIN.getCode()){
                        // If foundCabin is already true, there is another cabin
                        if (foundCabin) throw new IOException("Cannot have mutiple cabins");
                        
                        // Sets block as cabin and marks a cabin as found
                        ((Trail)newBlock).setTraversalState(Trail.TraversalState.CABIN);
                        foundCabin = true;
                    }
                        
                    // If it's a ranger, set the ranger index
                    // j is the x, and i is the y
                    else if (line.charAt(j) == MazeStates.RANGER.getCode()) {
                        
                        // If ranger index isn't null, then another ranger is in the maze
                        if (newRangerIndex != null) 
                            throw new IOException("Cannot have multiple rangers");
                        
                        newRangerIndex = new MazePanel.Coordinate2D(j, i);
                    }
                }
                
                // Sets the new PathBlock in the new maze
                newMaze[i][j] = newBlock;
            }
            
            // New maze is fully initialized
            // If a ranger or cabin was not found, the maze is invalid
            if (newRangerIndex == null || !foundCabin)
                throw new IOException("Maze did not have a ranger and cabin");
                
            // Saves new mze
            maze = newMaze;
        }
            
    }
    
    // Writes maze to maze file
    public void writeMaze() throws FileNotFoundException {
        // PrintWriter to write to file
        try (PrintWriter writer = new PrintWriter(MAZE_FILE_PATH)) {
            // For each block in each row
            for (int i = 0; i < mazeHeight; i++) {
                for (int j = 0; j < mazeWidth; j++) {
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
