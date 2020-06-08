package sample;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFormat {

    List<Signature> signatures;
    String outputFile;

    MyArryList<String[]> data;
    public ExcelFormat(List<Signature> signatures, String outputFile) {
        this.signatures = signatures;
        this.outputFile=outputFile;
        try {

            WriteDataToCSV(signatures);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        } catch (CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private void WriteDataToCSV(List<Signature> signatures) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {



        File file  = new File(outputFile);
        // create FileWriter object with file as parameter
        FileWriter outputfile = new FileWriter(file);

        // create CSVWriter object filewriter object as parameter
        CSVWriter writer = new CSVWriter(outputfile,';');

        // create a List which contains String array
        data = new MyArryList<String[]>();
        // adding header to csv
        String[] header = { "submissionDate","SignatureIdentifier","AnnexRevision","Id Number", "First Name", "Last Name","Birth Date","Birth Place","Nationality","Street","Postal code","City","Country"};
        writer.writeNext(header);

        for (Signature signature : signatures){
            String idNumber = null;
            String firstname= null;
            String famillyName=null;
            String adress=null;
            String residence=null;
            String codePostal =null;
            String city=null;
            String country=null;
            String dateOfBirth=null;
            String placeOfBirth=null;
            String nationality=null;

            for (Group group : signature.signatureInfo.groups){
                  for (Property currentProperty : group.properties){
                      switch (currentProperty.key){
                         case "oct.property.national.id.number":
                              idNumber = currentProperty.value;
                              break;
                          case "oct.property.firstname":
                              firstname = currentProperty.value;
                              break;
                          case "oct.property.lastname":
                              famillyName = currentProperty.value;
                              break;
                          case "oct.property.permanent.residence":
                              residence = currentProperty.value;
                              break;
                          case "oct.property.postal.code":
                              codePostal = currentProperty.value;
                              break;
                          case "oct.property.place.of.birth":
                              placeOfBirth = currentProperty.value;
                              break;
                          case "oct.property.date.of.birth":
                              dateOfBirth = currentProperty.value;
                              break;
                          case "oct.property.nationality":
                              nationality = currentProperty.value;
                              break;
                          case "oct.property.country":
                              country = currentProperty.value;
                              break;
                          case "oct.property.city":
                              city = currentProperty.value;
                              break;
                          default:
                      }
                  }
            }
            data.add(new String[] {signature.submissionDate,signature.signatureidentifier,signature.annexeRevision, idNumber,firstname,famillyName,dateOfBirth,placeOfBirth,nationality,residence,codePostal,city,country});
            }
        writer.writeAll(data);
        writer.close();


        }



}
