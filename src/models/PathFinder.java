package models;

import java.util.ArrayList;
import java.util.List;

import controllers.GameController;
import models.Cell.cellType;
import models.Lunatic.lunaticNextMove;

public class PathFinder { // Path finder algorithm for lunatics AI
	
	public static Cell[][] grid = GameController.GUI_CONTROLLER.getGUI().getGrid(); // Instance of the current GUI grid

	public PathFinder() { }
	
	public static boolean IsWalkable(Node node) { // Returns true if the node/cell is a walkable (path/player/lunatic)
		
		if(node.row < 1 || node.row > grid.length - 1) return false;
		if(node.col < 0 || node.col > grid[0].length - 1) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.WALL) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.TOP_BORDER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.BOTTOM_BORDER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.LEFT_BORDER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.RIGHT_BORDER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.TOP_CORNER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.BOTTOM_CORNER) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.START) return false;
		if(grid[node.row-1][node.col].getCellType() == cellType.START) return false;
		return true;
	}
	
	@SuppressWarnings("incomplete-switch")
	public static boolean isCellWalkable(int currentRow, int currentCol, lunaticNextMove move) 
	{ // Method used to generate the lunatic next move, allows for more smooth movement & avoids them being stuck on walls.
		
		Cell[][] grid = GameController.GUI_CONTROLLER.getGUI().getGrid();
		int nextRow, nextCol;
		switch(move) 
		{
			case UP: 
					nextRow = currentRow-1; nextCol = currentCol;
					if((grid[nextRow][nextCol].getCellType() == cellType.PATH) 
							|| (grid[nextRow][nextCol].getCellType() == cellType.PLAYER)
							|| (grid[nextRow][nextCol].getCellType() == cellType.LUNATIC))
					{
						return true;
					}
			case DOWN: 
					nextRow = currentRow+1; nextCol = currentCol;
					if((grid[nextRow][nextCol].getCellType() == cellType.PATH)
							|| (grid[nextRow][nextCol].getCellType() == cellType.PLAYER)
							|| (grid[nextRow][nextCol].getCellType() == cellType.LUNATIC))
					{
						return true;
					}
			case LEFT: 
					nextRow = currentRow; nextCol = currentCol-1;
					if((grid[nextRow][nextCol].getCellType() == cellType.PATH)
							|| (grid[nextRow][nextCol].getCellType() == cellType.PLAYER)
							|| (grid[nextRow][nextCol].getCellType() == cellType.LUNATIC))
					{
						return true;
					}
			case RIGHT: 
					nextRow = currentRow; nextCol = currentCol+1;
					if((grid[nextRow][nextCol].getCellType() == cellType.PATH)
							|| (grid[nextRow][nextCol].getCellType() == cellType.PLAYER)
							|| (grid[nextRow][nextCol].getCellType() == cellType.LUNATIC))
					{
						return true;
					}
		}
		return false;
	}
	
	public static List<Node> FindNeighbors(Node node) { // Returns a list of the current node neighbors (up/dow/left/right) & checks if they are walkable, then adds to a list.
	      List<Node> neighbors = new ArrayList<>();
	      Node up = node.offset(-1, 0);
	      Node down = node.offset(1, 0);
	      Node left = node.offset(0, -1);
	      Node right = node.offset(0, 1);
	      if (IsWalkable(up)) neighbors.add(up);
	      if (IsWalkable(down)) neighbors.add(down);
	      if (IsWalkable(left)) neighbors.add(left);
	      if (IsWalkable(right)) neighbors.add(right);
	      return neighbors;
	    }
	
	public static List<Coordinate> FindPath(Coordinate startPoint, Coordinate endPoint) { // Algorithm by a Stack Overflow user, tweaked to work with my application.
		
        
        Node start = new Node(startPoint.getRow(), startPoint.getColumn(), null); // Convert the start coordinate to a node instance.
        Node end = new Node(endPoint.getRow(), endPoint.getColumn(), null); // Convert the end coordinate to a node instance.
        boolean finished = false;
        List<Node> used = new ArrayList<>();
        
        used.add(start); // Add the start node to the used list (since the lunatic is already there)
        while (!finished) {
            List<Node> newOpen = new ArrayList<>(); // New list which holds open nodes through traversing the neighbors.
            for(int i = 0; i < used.size(); ++i){
            	Node node = used.get(i);
                for (Node neighbor : FindNeighbors(node)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for(Node node : newOpen) {
                used.add(node);
                if (end.equals(node)) {
                    finished = true;
                    break;
                }
            }

            if (!finished && newOpen.isEmpty()) {
            	List<Coordinate> emptyPath = new ArrayList<>();
            	emptyPath.add(startPoint);
            	return emptyPath; // Returns an list with the current starting point instead of null.
            }
        }

        List<Node> path = new ArrayList<>();
        Node node = used.get(used.size() - 1);
        while(node.previous != null) {
            path.add(0, node);
            node = node.previous;
        }
        
        List<Coordinate> NPC_PATH = new ArrayList<>(); // New list for coordinates to be returned to the controller.
        for(Node n : path) {
        	Coordinate coordinate = new Coordinate(n.row, n.col); // Converts the node instances to coordinates & adds them to the list.
        	NPC_PATH.add(coordinate);
        }
        return NPC_PATH;
    }
}
