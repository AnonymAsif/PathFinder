import java.io.File;
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
    
    // Maze to parse, read and write from/to file
    private PathBlock[][] maze;
    
    // Dimensions of the maze
    private int mazeHeight;
    private int mazeWidth;
    
    // Dimensions of each block
    private int blockHeight;
    private int blockWidth;

    // The index of the ranger
    private MazePanel.Coordinate2D rangerIndex;

    // The index of the cabin
    private MazePanel.Coordinate2D cabinIndex;


    // Constructor that takes dimensions
    public MazeFileHandler(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
        // Sets maze and panel dimensions
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;

        // Calculates dimensions of each block
        blockHeight = panelHeight / mazeHeight;
        blockWidth = panelWidth / mazeWidth;

        // Sets default maze
        setDefaultMaze();
    }

    // Sets the maze to the "default maze"
    // Every block is a Trail, with a ranger and cabin in the
    // top left and bottom right respectively
    private void setDefaultMaze() {
        maze = new PathBlock[mazeHeight][mazeWidth];

        // For each block in each row
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                // Sets the block to a new Trail
                maze[i][j] = new Trail(j * blockWidth, i * blockHeight, blockWidth, blockHeight);
            }
        }

        // Ranger starts at 0,0 by default
        rangerIndex = new MazePanel.Coordinate2D(0, 0);

        // Cabin starts at (height - 1, width - 1) by default
        // Cast it to a Trail to set its TraversalState
        Trail cabinBlock = (Trail)maze[mazeHeight-1][mazeWidth-1];
        cabinBlock.setTraversalState(Trail.TraversalState.CABIN);

        // Saves cabin index
        cabinIndex = new MazePanel.Coordinate2D(mazeWidth - 1, mazeHeight - 1);
    }
    
    // Reads maze from maze file
    public void readMaze(File mazeFile) throws IOException {
        // ArrayList to hold every line read
        ArrayList<String> lines = new ArrayList<>();

        // Scanner to read from file
        // Try block to ensure scanner is closed
        try(Scanner scanner = new Scanner(mazeFile)) {

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
        }
        
        // Verifies the length of the ArrayList is the expected height
        if (lines.size() != mazeHeight)
            throw new IOException("Unexpected height of maze");

        
        // Creates a new maze
        PathBlock[][] newMaze = new PathBlock[mazeHeight][mazeWidth];
        
        // Creates a new ranger index
        MazePanel.Coordinate2D newRangerIndex = null;

        // Creates a new cabin index
        MazePanel.Coordinate2D newCabinIndex = null;
        
        // Creates a new maze from file
        for (int i = 0; i < mazeHeight; i++) {
            // Gets the current line
            String line = lines.get(i);

            for (int j = 0; j < mazeWidth; j++) {
                // Sets the PathBlock to the appropriate type based on character
                PathBlock newBlock;

                // Character represents a tree, set it to a new tree
                if (line.charAt(j) == MazeStates.TREE.getCode())
                    newBlock = new Tree(j * blockWidth, i * blockHeight, blockWidth, blockHeight);

                    // Character represents a trail, make a new Trail
                else {
                    newBlock = new Trail(j * blockWidth, i * blockHeight, blockWidth, blockHeight);

                    // It may be a cabin or ranger
                    // If it's a cabin, set the traversal state of the Trail to CABIN
                    if (line.charAt(j) == MazeStates.CABIN.getCode()) {
                        // If foundCabin is already true, there is another cabin
                        if (newCabinIndex != null)
                            throw new IOException("Cannot have multiple cabins");

                        // Sets block as cabin and marks a cabin as found
                        // j is the x, and i the y
                        ((Trail) newBlock).setTraversalState(Trail.TraversalState.CABIN);
                        newCabinIndex = new MazePanel.Coordinate2D(j, i);
                    }

                    // If it's a ranger, set the ranger index
                    else if (line.charAt(j) == MazeStates.RANGER.getCode()) {

                        // If ranger index isn't null, then another ranger is in the maze
                        if (newRangerIndex != null)
                            throw new IOException("Cannot have multiple rangers");

                        newRangerIndex = new MazePanel.Coordinate2D(j, i);
                    }

                    // The character should be an empty Trail then
                    // If the character is something else, throw an exception
                    else if (line.charAt(j) != MazeStates.TRAIL.getCode())
                        throw new IOException("Invalid character found.");
                }

                // Sets the new PathBlock in the new maze
                newMaze[i][j] = newBlock;
            }
        }
            
            // New maze is fully initialized
            // If a ranger or cabin was not found, the maze is invalid
            if (newRangerIndex == null || newCabinIndex == null)
                throw new IOException("Maze did not have a ranger and cabin.");
                
            // Saves new maze and indices
            maze = newMaze;
            rangerIndex = newRangerIndex;
            cabinIndex = newCabinIndex;
    }
    
    // Writes maze to maze file
    public void writeMaze(File mazeFile) throws IOException {
        // PrintWriter to write to file
        // Try block to ensure it is closed
        try (PrintWriter writer = new PrintWriter(mazeFile)) {
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

            // Throws an IOException if the writer had errors
            if (writer.checkError())
                throw new IOException("An unexpected error has occurred while writing to the file.");
        }
    }

    // Creates a deep copy of a maze and returns it
    private PathBlock[][] getMazeCopy(PathBlock[][] oldMaze) {
        // Throws exception if the oldMaze is null
        if (oldMaze == null)
            throw new IllegalArgumentException("Old maze cannot be null");

        // New maze to be copied into
        PathBlock[][] copy = new PathBlock[mazeHeight][mazeWidth];

        // For every element in the current maze
        // Create a PathBlock of the same type
        // If it's a Trail, copy its traversal state as well
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                // Old PathBlock to be copied
                PathBlock old = oldMaze[i][j];

                // If it was a Tree, create a new Tree
                // using the old Trees dimensions and continue
                if (old instanceof Tree) {
                    copy[i][j] = new Tree((int)old.getX(), (int)old.getY(), (int)old.getWidth(), (int)old.getHeight());
                    continue;
                }

                // The old block is a Trail, so create a new Trail instead
                // then copy over its traversal state
                copy[i][j] = new Trail((int)old.getX(), (int)old.getY(), (int)old.getWidth(), (int)old.getHeight());
                ((Trail) copy[i][j]).setTraversalState(((Trail) old).getTraversalState());
            }
        }

        return copy;
    }

    // Getter that returns a deep copy of the maze
    public PathBlock[][] getMaze() {
        return getMazeCopy(maze);
    }

    // Setter that saves a deep copy of a maze
    public void setMaze(PathBlock[][] maze) {
        // Throws exception if maze is null
        if (maze == null)
            throw new IllegalArgumentException("Null maze");

        // Saves a copy of the maze
        this.maze = getMazeCopy(maze);
    }

    // Getters and setters for ranger and cabin index
    public MazePanel.Coordinate2D getRangerIndex() {
        return rangerIndex;
    }

    public MazePanel.Coordinate2D getCabinIndex() {
        return cabinIndex;
    }

    public void setRangerIndex(MazePanel.Coordinate2D rangerIndex) {
        this.rangerIndex = rangerIndex;
    }
}
