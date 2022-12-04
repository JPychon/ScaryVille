package models;

import java.util.Objects;

public class Node {

	public int row;
	public int col;
	public Node previous;
	
	public Node(int row, int col, Node previous) {
		this.col = col;
		this.row = row;
		this.previous = previous;
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
