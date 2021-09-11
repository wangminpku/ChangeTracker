package patternmining;

import java.util.List;

public class FPItem {

    private int count;
    private String header;
    private List<String> elementsList;

    public FPItem(){

    }

    public FPItem(int count, String header, List<String> eleList){
        this.count = count;
        this.header = header;
        this.elementsList = eleList;

    }

    public void setCount(int count){
        this.count = count;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void setElementsList(List<String> elementsList){
        this.elementsList = elementsList;
    }

    public int getCount(){
        return this.count;
    }

    public String getHeader(){
        return this.header;
    }

    public List<String> getElementsList(){
        return this.elementsList;
    }
}
