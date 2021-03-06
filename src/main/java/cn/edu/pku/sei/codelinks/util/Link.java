package cn.edu.pku.sei.codelinks.util;

import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import org.json.JSONObject;

public class Link {

    private String fileA;

    private String fileB;

    private ChangeEntity changeEntity1;

    private ChangeEntity changeEntity2;

    private String type;

    private String keyWord;

    public String getFileA() {
        return fileA;
    }

    public String getFileB() {
        return fileB;
    }

    public ChangeEntity getChangeEntity1() {
        return changeEntity1;
    }

    public ChangeEntity getChangeEntity2() {
        return changeEntity2;
    }

    public String getType() {
        return type;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public Link(String fileName, ChangeEntity changeEntity1, ChangeEntity changeEntity2, String type, String keyWord){
        this.changeEntity1 = changeEntity1;
        this.changeEntity2 = changeEntity2;
        this.type = type;
        this.keyWord = keyWord;
        this.fileA = fileName;
    }

    /**
     * file A B 按照字母顺序 固定
     * @param fileA
     * @param fileB
     * @param changeEntity1
     * @param changeEntity2
     */
    public Link(String fileA, String fileB, ChangeEntity changeEntity1, ChangeEntity changeEntity2, String type, String keyWord){
        if(fileA.compareTo(fileB)<0){
            this.fileA = fileA;
            this.fileB = fileB;
            this.changeEntity1 = changeEntity1;
            this.changeEntity2 = changeEntity2;
        }else{
            this.fileA = fileB;
            this.fileB = fileA;
            this.changeEntity1 = changeEntity2;
            this.changeEntity2 = changeEntity1;
        }
        this.type = type;
        this.keyWord = keyWord;
    }



    public String toString(){
        String result = changeEntity1.getChangeEntityId()+". "+changeEntity1.getClass().getSimpleName()
                +" -> "+changeEntity2.getChangeEntityId()+". "+changeEntity2.getClass().getSimpleName()+" : " +type ;//+" "+keyWord;
        if(this.fileB==null){
            result+=" in " + fileA;
        }
        else{
            result+= " among "+ fileA + " and " + fileB;
        }
        return result;

    }

    public JSONObject linkJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from",changeEntity1.getChangeEntityId());
        jsonObject.put("to",changeEntity2.getChangeEntityId());
        if(this.keyWord==null){
            jsonObject.put("desc",this.type);
        }else{
            jsonObject.put("desc",this.type+" "+this.keyWord);
        }
        return jsonObject;
    }
}
