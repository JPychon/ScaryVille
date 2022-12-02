package models;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Cell -> Class template that represents each individual cell within a a gridpane.
// This allows for control of each cell behavior in the gridpane.

public class Cell { // Tracks the grid-type for the GUI
	
	private final Label CELL_LABEL; // Cell node label
	private final int CELL_SIZE = 32; // Cell height/width (32x32 px)
	
	private cellType cellType;
	private Image labelImage;
	private ImageView labelImageView;
	
	//----------------------------------[CELL-MODELS-PATH]---------------------------------//
	
	private String image_path = "asylum-floor.png"; // Image for the pathway-tiles
	private String image_start = "start_door.png"; // Image for the start-tile
	private String image_end = "end_door.png"; // Image for the end-tile
	private String image_topWall = "top-wall-border.png"; // Image for top-wall
	private String image_bottomWall = "bottom-wall-border.png"; // Image for bottom-wall
	private String image_left_right_wall = "left-right-wall.png"; // Image for left/right walls & top/left corner.
	private String image_topCorners = "left-right-wall.png";  // Image for top-corners
	private String image_Wall = "wall.png"; // regular wall
	private String image_player = "player-model.png"; // Player model
	private String image_lunatic = "lunatic-model.png"; // Lunatic model
	
	//------------------------------------------------------------------------------------//
		
	public enum cellType {
		PATH,
		WALL,
		TOP_BORDER,
		BOTTOM_BORDER,
		LEFT_BORDER,
		RIGHT_BORDER,
		TOP_CORNER,
		BOTTOM_CORNER,
		START,
		END,
		PLAYER,
		LUNATIC,
	}
	
	@SuppressWarnings("static-access") // Enums are inherently static; thus, supressing the warning.
	public Cell(cellType type) {
	
		CELL_LABEL = new Label();
		
		switch(type) {
			case WALL: // Regular wall
				cellType = cellType.WALL;
				CELL_LABEL.setId("wall");
				labelImage = new Image(image_Wall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case TOP_BORDER: // Top border wall
				cellType = cellType.TOP_BORDER;
				CELL_LABEL.setId("border-wall-top");
				labelImage = new Image(image_topWall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
			
			case BOTTOM_BORDER: // Bottom border wall
				cellType = cellType.BOTTOM_BORDER;
				CELL_LABEL.setId("border-wall-bottom");
				labelImage = new Image(image_bottomWall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case LEFT_BORDER: // Left border wall
				cellType = cellType.LEFT_BORDER;
				CELL_LABEL.setId("border-wall-left");
				labelImage = new Image(image_left_right_wall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
			
			case RIGHT_BORDER: // Right border wall
				cellType = cellType.RIGHT_BORDER;
				CELL_LABEL.setId("border-wall-right");
				labelImage = new Image(image_left_right_wall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
		
			case TOP_CORNER: // Top corner border wall (left/right)
				cellType = cellType.TOP_CORNER;
				CELL_LABEL.setId("border-wall-corner");
				labelImage = new Image(image_topCorners);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case BOTTOM_CORNER: // Bottom corner border wall (left/right)
				cellType = cellType.BOTTOM_CORNER;
				CELL_LABEL.setId("border-wall-corner");
				labelImage = new Image(image_bottomWall);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
			
			case START: // Start cell (locked-door)
				cellType = cellType.START;
				CELL_LABEL.setId("start-cell");
				labelImage = new Image(image_start);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case END: // End cell (unlocked-door)
				cellType = cellType.END;
				CELL_LABEL.setId("end-cell");
				labelImage = new Image(image_end);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
			
			case PLAYER: // Player cell
				cellType = cellType.PLAYER;
				CELL_LABEL.setId("player-cell");
				labelImage = new Image(image_player);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case PATH: // Path cell
				cellType = cellType.PATH;
				CELL_LABEL.setId("path");
				labelImage = new Image(image_path);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
				
			case LUNATIC: // Lunatic cell
				cellType = cellType.LUNATIC;
				CELL_LABEL.setId("lunatic-cell");
				labelImage = new Image(image_lunatic);
				labelImageView = new ImageView(labelImage);
				labelImageView.setPreserveRatio(true);
				CELL_LABEL.setGraphic(labelImageView);
				break;
		}
				
		CELL_LABEL.setMinHeight(CELL_SIZE); // 32 pixels
		CELL_LABEL.setMinWidth(CELL_SIZE); // 32 pixels
		CELL_LABEL.setPrefSize(CELL_SIZE, CELL_SIZE); // 32x32 pixels
	}
	
	public Cell getCell() { return this; } // returns the cell
	public Node getNode() { return CELL_LABEL; } // returns the node (label)
	public cellType getCellType() { return cellType; } // returns the cell-type (enum value)
	
	
}
