package sample;

public class Signature {

    String signatureidentifier;
    String submissionDate;
    String annexeRevision;
    SignatoryInfo signatureInfo;

    public Signature()
    {
        signatureInfo = new SignatoryInfo();
    }


}
