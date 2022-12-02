module ScaryVille {
	exports models;
	exports controllers;
	exports scaryville;

	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	
	opens scaryville to javafx.graphics;
}