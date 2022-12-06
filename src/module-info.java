module ScaryVille {
	exports models;
	exports controllers;
	exports scaryville;

	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	requires javafx.fxml;
	requires transitive javafx.media;
	
	opens scaryville to javafx.graphics;
}