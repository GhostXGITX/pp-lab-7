import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class Main extends Application {

    private TextField directoryPathField;
    private TextField searchField;
    private TextArea resultArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Browser and Search");

        TextField directoryPathField = new TextField("Enter directory path");
        TextField searchField = new TextField("Enter search phrase");

        TextArea resultArea = new TextArea();
        resultArea.setPrefHeight(400);

        Button browseButton = new Button("Browse");
        browseButton.setOnAction(e -> browseDirectory());

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchFiles());

        HBox hBox = new HBox(10, directoryPathField, browseButton);
        VBox vBox = new VBox(10, hBox, searchField, searchButton, resultArea);

        Scene scene = new Scene(vBox, 600, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }

    private void browseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(null);
        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    private void searchFiles() {
        String directoryPath = directoryPathField.getText();
        String searchPhrase = searchField.getText();
        if(directoryPath.isEmpty()) {
            resultArea.setText("Please provide a directory path.");
            return;
        }

        File directory = new File(directoryPath);
        if(!directory.isDirectory()) {
            resultArea.setText("The provided path is not a directory.");
            return;
        }

        StringBuilder results = new StringBuilder();
        searchInDirectory(directory, results, searchPhrase);
        resultArea.setText(results.toString());
    }

    private void searchInDirectory(File directory, StringBuilder results, String searchPhrase) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (containsPhrase(file, searchPhrase)){
                        searchInDirectory(directory, results, searchPhrase);
                    }
                } else {
                    results.append(file.getAbsolutePath()).append("\n");
                }
            }
        }
    }

    private boolean containsPhrase(File file, String searchPhrase) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(searchPhrase)) {
                    return true;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
