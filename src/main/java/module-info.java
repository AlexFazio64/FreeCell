module yorha.freecell {
	requires javafx.controls;
	requires javafx.fxml;
	
	
	opens yorha.freecell to javafx.fxml;
	exports yorha.freecell;
}