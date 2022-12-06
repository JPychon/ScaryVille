package models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import controllers.GameController;
import models.Cell.cellType;
import models.Lunatic.lunaticNextMove;

// AI Pathfinding classes, uses a variation of BFS algorithm to navigate the graph/grid & find the optimal path between two destination
// It also utilizes some aspects of the A* algorithm through the the usage of the manhattan distance in the Node class
public class GridPathFinder {

	// Refernce of the 2D grid instance used to get cell types.
	public static Cell[][] grid = GameController.GUI_CONTROLLER.getGUI().getGrid(); // Instance of the current GUI grid
	
    // Converts a node to a coordinate.
	public static Coordinate convertNodeToCoordinate(Node node) { return new Coordinate(node.row, node.col); }
	// Converts a coordiante to a node.
	public static Node convertCoordinateToNode(Coordinate coordinate, Node previous) { return new Node(coordinate.getRow(),coordinate.getColumn(), previous); }
	
	public static boolean IsWalkable(Node node) 
	{
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
		if(grid[node.row-1][node.col].getCellType() == cellType.END) return false;
		return true;
	}
	
	public static boolean IsWalkable(int row, int column) 
	{
		if(row < 2 || row > grid.length - 1) return false;
		if(column < 1 || column > grid[0].length - 1) return false;
		if(grid[row-1][column].getCellType() == cellType.WALL) return false; 
		if(grid[row-1][column].getCellType() == cellType.TOP_BORDER) return false;
		if(grid[row-1][column].getCellType() == cellType.BOTTOM_BORDER) return false;
		if(grid[row-1][column].getCellType() == cellType.LEFT_BORDER) return false;
		if(grid[row-1][column].getCellType() == cellType.RIGHT_BORDER) return false;
		if(grid[row-1][column].getCellType() == cellType.TOP_CORNER) return false;
		if(grid[row-1][column].getCellType() == cellType.BOTTOM_CORNER) return false;
		if(grid[row-1][column].getCellType() == cellType.START) return false;
		if(grid[row-1][column].getCellType() == cellType.END) return false;
		return true;
	}
	
	// Method used to generate the lunatic next move, allows for more smooth movement & avoids them being stuck on walls.
	@SuppressWarnings("incomplete-switch")
	public static boolean IsWalkable(int currentRow, int currentCol, lunaticNextMove move) 
	{ 
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
	
	// Finds the neighbors of a node that are not obstacles
	public static List<Node> FindNeighbors(Node node, Node end) {
		
	      List<Node> neighbors = new ArrayList<>();
	      Node up = node.offset(-1, 0);
	      Node down = node.offset(1, 0);
	      Node left = node.offset(0, -1);
	      Node right = node.offset(0, 1);
	      
	      if (IsWalkable(up)) neighbors.add(new Node(node.row - 1, node.col, node, end));
	      if (IsWalkable(down)) neighbors.add(new Node(node.row + 1, node.col, node, end));
	      if (IsWalkable(left)) neighbors.add(new Node(node.row, node.col - 1, node, end));
	      if (IsWalkable(right)) neighbors.add(new Node(node.row, node.col + 1, node, end));
	      return neighbors;
	}
	
	public static List<Coordinate> FindPath(Coordinate startPoint, Coordinate endPoint) {
		
	    Node start = convertCoordinateToNode(startPoint, null);
	    Node end = convertCoordinateToNode(endPoint, null);

	    // Create a priority queue to store the nodes that need to be visited
	    // The priority queue orders the nodes by their estimated distance to the destination
	    PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
	        @Override
	        public int compare(Node a, Node b) { // Comparator to compare the node's orderding based on their distance. [true if distance + heutristic A > distance + heutristic B]
	            return Integer.compare(a.distance + a.heuristic, b.distance + b.heuristic);
	        } // This ensures that the nodes in the priority queue are ordered based on their estimated-distance to the destination
	    });
	    
	    queue.add(start);

	    // Create a set to store the nodes that have been visited
	    Set<Node> visited = new HashSet<>();
	    visited.add(start);

	    // Create a map to store the previous node for each node
	    Map<Node, Node> previous = new HashMap<>();

	    // While the queue is not empty
	    while (!queue.isEmpty()) {
	        Node node = queue.remove();

	        // Check if the node is the destination
	        if (node.equals(end)) {
	            // Destination found, return the path
	            return constructPath(previous, end);
	        }

	        // Get the neighbors of the node
	        List<Node> neighbors = FindNeighbors(node, end);

	        // Add the unvisited neighbors to the queue
	        for (Node neighbor : neighbors) {
	            if (!visited.contains(neighbor)) {
	                queue.add(neighbor);
	                visited.add(neighbor);
	                previous.put(neighbor, node);
	            }
	        }
	    }

	    // No path found
	    List<Coordinate> emptyPath = new ArrayList<>();
	    emptyPath.add(startPoint);
	    return emptyPath;
	}
	
	private static List<Coordinate> constructPath(Map<Node, Node> previous, Node end) {
	    List<Coordinate> path = new ArrayList<>();

	    // Create a stack to store the nodes on the path from start to end
	    // The stack allows to build the path in reverse order starting from the end point
	    Stack<Node> stack = new Stack<>();
	    stack.push(end);
	    while (previous.containsKey(end)) {
	        end = previous.get(end);
	        stack.push(end);
	    }

	    // Pop the nodes from the stack and add them to the path
	    while (!stack.isEmpty()) {
	        path.add(convertNodeToCoordinate(stack.pop()));
	    }

	    // Skip the start point
	    path.remove(0);

	    return path;
	}
}
