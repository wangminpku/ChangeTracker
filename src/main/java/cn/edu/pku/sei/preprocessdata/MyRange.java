package cn.edu.pku.sei.preprocessdata;

public class MyRange {

    /**
     * type insert/delete
     */
    public int type;

    public int startLineNo;
    public int endLineNo;

    public MyRange(int start,int end,int type){
        this.startLineNo = start;
        this.endLineNo = end;
        this.type = type;
    }

    @Override
    public String toString(){
        return "("+this.startLineNo+","+this.endLineNo+")";
    }

    //判断当前的range是否在参数的range之内，如果是 返回1 如果相反 返回-1,如果交集 返回0
    public int isRangeWithin(MyRange myRange){
        if(this.startLineNo<=myRange.startLineNo
                && this.endLineNo >= myRange.endLineNo){
            return -1;
        }else if(myRange.startLineNo <= this.startLineNo
                && this.endLineNo <=myRange.endLineNo){
            return 1;
        }else{
            return 0;
        }
    }
}
