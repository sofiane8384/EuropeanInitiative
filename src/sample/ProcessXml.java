package sample;


import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.ExcelFormat;
import sample.Group;
import sample.MyArryList;
import sample.Property;
import sample.SignatoryInfo;
import sample.Signature;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class ProcessXml {
    private static  Logger logger = Logger.getLogger(ProcessXml.class.getName());
    public ArrayList<Signature> signatures;
    public ProcessXml(){

    }

    public void ProcessXml(String fileName, String outputFile) throws IOException, SAXException, ParserConfigurationException {


        FileHandler fh;
        //System.out.println(FileSystems.getDefault().getPath(".").toAbsolutePath().toString()) ;
        fh = new FileHandler("app.log");

        SimpleFormatter formatter = new SimpleFormatter();
        logger.addHandler(fh);
        fh.setFormatter(formatter);
        signatures = new ArrayList<Signature>();

        if(CheckFile(fileName) && CheckPath(outputFile)){

            File fXmlFile = new File(fileName);
            String test = System.getProperty("user.dir");
            File schemaFile = new File(test + "\\oct-export.xsd");
                ProcessXmlFile(fXmlFile);
                System.gc();
                sample.ExcelFormat excelFormat = new ExcelFormat(signatures, outputFile);

        }


    }

    private boolean CheckPath(String outputFile) {
        int pos = outputFile.lastIndexOf("\\")+1;
        String rootName = outputFile.substring(0,pos);

        File rootFile = new File(rootName);
        if (rootFile.isDirectory()) {

           // System.out.println("File is a Directory");
        } else {
            logger.log(Level.WARNING,"Path provided is not correct");
            //System.out.println("Directory doesn't exist!!");
        }
        return true;
    }

    private boolean CheckFile(String fileName) throws IOException {

        File xmlfile = new File(fileName);
        if(xmlfile.exists()) {
            if (getFileExtension(xmlfile).equals(".xml"))
                return true;
        }

                logger.log(Level.WARNING,"File name provided is not correct");
                return false;
    }

    private static String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
            logger.log(Level.SEVERE,e.getMessage());
        }

        return extension;

    }
    private boolean validate(String xmlFile, File schemaFile) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);

            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFile)));
            return true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void ProcessXmlFile(File fXmlFile) throws ParserConfigurationException, IOException, SAXException {


            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();
           // System.out.println("Root element :" + rootElement.getNodeName().trim());
            NodeList signatureNodeList = rootElement.getElementsByTagName("signature");
            System.out.println("signatures number :"+ signatureNodeList.getLength());

            // get the first item in the list

        System.out.println("-------------Total signatures : " + signatureNodeList.getLength());
            for (int i = 0; i<  signatureNodeList.getLength();i++){

                System.out.println("-------------Signature---------------" + i);
                Signature signature = new Signature();
                HandleSignatureNode(signatureNodeList.item(i),signature);

            signature =null;
            System.gc();
            }

           // System.out.println("European initiative proceed ");

    }

    private void HandleSignatureNode(Node signature, Signature signatureInstance){

        NodeList signatureChildren = signature.getChildNodes();
        for(int i=0;i<signatureChildren.getLength();i++){
            Node node = signatureChildren.item(i);
            if (1 == node.getNodeType()){
                if (node.getNodeName().equals("signatoryInfo")) {
                    NodeList signatoryInfos = node.getChildNodes();

                    HandleSignatoryInfo(signatoryInfos, signatureInstance);
                }else{
                    switch(node.getNodeName().trim()){
                        case "submissionDate":
                            signatureInstance.submissionDate = node.getFirstChild().getTextContent().trim();
                            break;
                        case "signatureIdentifier":
                            signatureInstance.signatureidentifier = node.getFirstChild().getTextContent().trim();
                            break;
                        case "annexRevision":
                            signatureInstance.annexeRevision = node.getFirstChild().getTextContent().trim();
                            break;
                    }
                   // System.out.println(node.getNodeName().trim()+ " : " + node.getFirstChild().getTextContent().trim()) ;

                }
            }
        }
    }

    private void HandleSignatoryInfo(NodeList signatoryInfos, Signature signatureInstance) {
        for (int j = 0; j < signatoryInfos.getLength(); j++) {
            Node current = signatoryInfos.item(j);
            if (1 == current.getNodeType()) {
               // System.out.println(current.getNodeName() + "  : " + current.getFirstChild().getTextContent());
                NodeList groups = current.getChildNodes();
                HandleGroups(groups,signatureInstance);
            }
            current = null;
        }

        signatures.add(signatureInstance);

    }

    private static void HandleGroups(NodeList groups, Signature signatureInstance) {
        SignatoryInfo signatoryInfoInstance = new SignatoryInfo();
        for(int g=0;g < groups.getLength();g++) {
            Node currentGroup = groups.item(g);
            if (1 == currentGroup.getNodeType()) {
               // System.out.print(currentGroup.getNodeName() + " : " + currentGroup.getFirstChild().getTextContent().trim());
                NodeList group = currentGroup.getChildNodes();
                HandleChildGroup(group, signatoryInfoInstance);
            }
            currentGroup=null;
        }
         signatureInstance.signatureInfo = signatoryInfoInstance;
        signatoryInfoInstance=null;
    }

    private static void HandleChildGroup(NodeList group, SignatoryInfo signatuoryInfoInstance) {
        Group groupInstance = new Group();
        for (int cpt=0;cpt < group.getLength();cpt++){
            Node currentGroupChildren = group.item(cpt);
            if (1 == currentGroupChildren.getNodeType()) {
                if(currentGroupChildren.getNodeName().equals("properties")){
                    HandleProperties( currentGroupChildren.getChildNodes(), groupInstance);
                }
            }
            currentGroupChildren=null;
        }
        signatuoryInfoInstance.groups.add(groupInstance);
        groupInstance =null;
    }

    private static void HandleProperties(NodeList properties, Group groupInstance) {
        //System.out.println("-----------------------------------------");
        for (int cpt=0;cpt < properties.getLength() ;cpt++){
            Node currentproperties = properties.item(cpt);
            if(1 == currentproperties.getNodeType() ){
                String nameKey = null;
                for (int cptProperties=0;cptProperties < currentproperties.getChildNodes().getLength();cptProperties++)
                {
                    Node currentProperty = currentproperties.getChildNodes().item(cptProperties);

                    if (1 == currentProperty.getNodeType()) {
                        if (currentProperty.getNodeName().equals("key"))
                                nameKey = currentProperty.getTextContent().trim();
                        if(currentProperty.getNodeName().equals("value")){
                            Property currentProp = new Property(nameKey,
                                    currentProperty.getTextContent().trim());
                            groupInstance.properties.add(currentProp);
                            currentProp=null;
                        }
                    }
                    currentProperty=null;
                }
            }
        }
    }
}
