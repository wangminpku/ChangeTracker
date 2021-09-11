package cn.edu.pku.sei.fileutil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class DiffFileReader {

    //     读取文件指定行。
    public static String readAppointedLineNumber(File sourceFile, int startLineNumber,int endLineNumber){
        String str = "";
        try{
            FileReader in = new FileReader(sourceFile);
            LineNumberReader reader = new LineNumberReader(in);
            String s = reader.readLine();

            if (startLineNumber < 0 || endLineNumber > getTotalLines(sourceFile)) {
                System.out.println("不在文件的行数范围之内。");
            }
            {
                while (s != null) {
                    //System.out.println("当前行号为:" + reader.getLineNumber());
                    if((reader.getLineNumber() >= startLineNumber) && (reader.getLineNumber()<=endLineNumber)){
                        str += s;
                        str += "\n";
                    }
                    if(reader.getLineNumber()>endLineNumber)
                        break;
                    s = reader.readLine();
                }
            }
            reader.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;

    }

    // 文件内容的总行数。
    static int getTotalLines(File file) {
        try{
            FileReader in = new FileReader(file);
            LineNumberReader reader = new LineNumberReader(in);
            String s = reader.readLine();
            int lines = 0;
            while (s != null) {
                lines++;
                s = reader.readLine();
            }
            reader.close();
            in.close();
            return lines;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -1;

    }

    //解析range
    public static List<Integer> parserOneRange(String range){
        List<Integer> rangeNum = new ArrayList<>();
        int i = range.indexOf("(");
        int i1 = range.indexOf(",");
        int i2 = range.indexOf(")");
        Integer num1 = Integer.parseInt(range.substring(i+1,i1));
        Integer num2 = Integer.parseInt(range.substring(i1+1,i2));
        rangeNum.add(num1);
        rangeNum.add(num2);
        return rangeNum;
    }

    public static List<Integer> parserRange(String range){
        List<Integer> rangeNum;
        if(range.contains("-")){
            String[] twoRange = range.split("-");
            rangeNum = parserOneRange(twoRange[0]);
            rangeNum.addAll(parserOneRange(twoRange[1]));
            return rangeNum;
        }else{
            rangeNum = parserOneRange(range);
            return rangeNum;
        }
    }

    public static boolean isPartOf(String range1,String range2){
        List<Integer> list1 = parserRange(range1);
        if(list1.size() == 2){
            List<Integer> list2 = parserRange(range2);
            if(list2.size() == 2){
                if((list1.get(0)>= list2.get(0))&&(list1.get(1) <= list2.get(1))){
                    return true;
                }else{
                    return false;
                }
            }else{
                if(list1.get(0)>=list2.get(2) && list1.get(1)<=list2.get(3)) {
                    return true;
                }else{
                    return false;
                }
            }
        }else{
            List<Integer> list2 = parserRange(range2);
            if(list2.size() == 2){
                if(list1.get(2)>=list2.get(0) && list1.get(3)<=list2.get(1)){
                    return true;
                }else{
                    return false;
                }
            }else{
                if(list1.get(2)>=list2.get(2) && list1.get(3)<=list2.get(3)){
                    return true;
                }else{
                    return false;
                }
            }
        }
    }

    public static void main(String[] args){

        // 读取文件
        /*File sourceFile = new File("F:\\SROutput1\\intellide-graph\\4e2adeb01737308a23100665c7e9dbfbf036626e\\curr\\88d3c3a772dba1a5197e2071fcf9df90eff4e687\\src\\main\\java\\cn\\edu\\pku\\sei\\intellide\\graph\\extraction\\java\\JavaExtractor.java");
        // 获取文件的内容的总行数
        int totalNo = getTotalLines(sourceFile);
        System.out.println("There are "+totalNo+ " lines in the text!");

        // 指定读取的行号
        int lineNumber = 5;

        // 读取指定的行
        System.out.println(readAppointedLineNumber(sourceFile,5,5));*/
       /* String range = "(110,112)-(113,124)";
        List<Integer> list = parserRange(range);
        for(Integer i : list){
            System.out.println(i);
        }*/

       String filename = "asdfg.java";
       int index = filename.indexOf(".java");
       filename = filename.substring(0,index);
       System.out.println(filename);
    }

}
