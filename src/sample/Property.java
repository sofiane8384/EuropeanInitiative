package sample;

public class Property {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String key;
    public String value;

    public Property (String keyProp, String valueProp){
        this.key = keyProp;
        this.value = valueProp;

    }
    public Property (){


    }
}
