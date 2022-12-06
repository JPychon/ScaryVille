package models;

import java.util.Objects;

public class Node {

	public int row; // Row
	public int col; // Column 
	
	public int distance; // holds the distance between two nodes
    public int heuristic; // holds the estimate of the minimum cost from any vertex n (current cell) to the goal (end-cell) [A* algo http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html]
    
	public Node previous; // Prevous visited node
	
	public Node(int row, int col, Node previous) 
	{
		this.col = col;
		this.row = row;
		this.previous = previous;
	}
	
	 public Node(int row, int col, Node previous, Node end) 
	 {
		 this.row = row;
	     this.col = col;
	     this.previous = previous;

	     if (previous == null) 
	     {
	    	 this.distance = 0;  // If the node is the starting node, the actual distance is 0
	     } 
	     else 
	     {
	         this.distance = previous.distance + 1; // If the node is not the starting node, the actual distance is the previous node's distance + 1
	     }

	     // Calculate the estimated distance from the node to the destination
	     // using the Manhattan distance
	     this.heuristic = manhattanDistance(end);
	}
	 
    // Calculates the Manhattan distance between two points (x1, y1) and (x2, y2)
	// The sum of the horizontal and vertical distnace between two points
    public int manhattanDistance(Node destination) {
        return Math.abs(col - destination.col) + Math.abs(row - destination.row);
    }
	
	@Override
	public String toString() { return String.format("(%d, %d)", row, col); }
	
	@Override
	public boolean equals(Object o) {
		Node node = (Node) o;
		return row == node.row && col == node.col;
	}
	
	@Override
	public int hashCode() { return Objects.hash(row, col); }
	
	public Node offset(int oy, int ox) { return new Node(row + oy, col + ox, this); }
	
}
