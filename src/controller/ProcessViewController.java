package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import jdk.internal.org.xml.sax.SAXException;
import org.apache.commons.io.FilenameUtils;
import sample.European;
import sample.Main;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProcessViewController implements Initializable {


    private Main mainApp;
    boolean sourceFileValid= false;
    boolean destinationFileValid = false;
    boolean operationIsSucceded=false;
    Task<Void> task;


    @FXML
    private TextField intputFileTxt;

    @FXML
    private TextField outputFiedlTxt;

    @FXML
    private ProgressBar progressIndicator;

    @FXML
    private Label statusLabel;


    private String outFile;


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;


    }

    @FXML
    private void readInputFile() {

        FileChooser fileChooser = new FileChooser();


            File selectedFile = fileChooser.showOpenDialog(intputFileTxt.getScene().getWindow());
            if(selectedFile != null){
                if(checkExtension(selectedFile.toString(),"xml")) {
                    intputFileTxt.setText(selectedFile.toString());
                    if (intputFileTxt.getCharacters().length() > 0){
                        sourceFileValid=true;
                    }
                }else {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Check extension file: *.xml only ");
                            alert.showAndWait();
                        }
                    });
                };
            }





    }
    @FXML
    private void readOutputFile() {

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog( outputFiedlTxt.getScene().getWindow());
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files(*.csv)", "*.csv"));
        if(selectedFile != null){
            if (! hasExtension(selectedFile.toString())){
                outFile = selectedFile.toString() + ".csv";
                File file = new File(outFile);
                try {
                    if(file.createNewFile()){
                        outputFiedlTxt.setText(outFile);
                        destinationFileValid=true;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }else if(checkExtensionOuputFile(selectedFile.toString(),"csv")) {
                outputFiedlTxt.setText(selectedFile.toString());
                destinationFileValid=true;
            }
            else {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("Check extension file: only .csv file");
                        alert.showAndWait();
                    }
                });
            }
        }



    }


    @FXML
    private void launchProcess() {
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                European test = new European();
                try {

                    if(destinationFileValid && sourceFileValid){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                progressIndicator.setVisible(true);
                                statusLabel.setVisible(false);
                            }
                        });

                        outFile= outputFiedlTxt.getText();
                        test.launch(intputFileTxt.getText(),outFile);
                        operationIsSucceded =true;
                    }
                    else{
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setContentText("Check input (*.xml) or output file (*.csv)");
                                alert.showAndWait();
                            }
                        });

                    }

                } catch (ParserConfigurationException ex) {
                    ex.printStackTrace();
                } catch (SAXException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                catch (Exception e){
                    operationIsSucceded = false;
                    e.printStackTrace();
                }
                return  null;
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if(operationIsSucceded){
                    statusLabel.setVisible(true);
                    progressIndicator.setVisible(false);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("Process is done without error !!");
                            alert.showAndWait();
                            intputFileTxt.setText("");
                            outputFiedlTxt.setText("");
                        }
                    });
                }
                progressIndicator.setVisible(false);
            }
        });
        new Thread(task).start();
        

    }
    @FXML
    private void cancelProcess() throws Exception {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure want to close this app?");
                Optional<ButtonType> option = alert.showAndWait();
              if (option.get() == ButtonType.OK) {
                    System.exit(0);
                }
            }
        });



    }



    private boolean hasExtension(String outputFile) {
        return FilenameUtils.getExtension(outputFile).length()>0;

    }

    private boolean checkExtension(String fullPathFile, String extension) {
        String extens = FilenameUtils.getExtension(fullPathFile.toLowerCase()).toLowerCase();

        if (extens.length()> 0 && ! extens.equals(extension.toLowerCase()) )
            return false;

        return extens.equals(extension.toLowerCase());

    }
    private boolean checkExtensionOuputFile(String fullPathFile, String extension) {
        String extens = FilenameUtils.getExtension(fullPathFile.toLowerCase()).toLowerCase();

        if (extens.length()> 0 && ! extens.equals(extension.toLowerCase()) )
            return false;

        return extens.equals(extension.toLowerCase());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressIndicator.setVisible(false);

    }
}
