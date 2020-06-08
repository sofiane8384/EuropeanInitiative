package sample;

import javafx.collections.ObservableList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class European  {

    public void launch(String inputFile, String outputFile) throws ParserConfigurationException, SAXException, IOException, Exception {
        ProcessXml test = new ProcessXml();
        test.ProcessXml(inputFile, outputFile);

    }



}
