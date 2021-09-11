package cn.edu.pku.sei.codelinks.similariity;

import at.unisalzburg.dbresearch.apted.costmodel.PerEditOperationStringNodeDataCostModel;
import at.unisalzburg.dbresearch.apted.distance.APTED;
import at.unisalzburg.dbresearch.apted.node.Node;
import at.unisalzburg.dbresearch.apted.node.StringNodeData;
import com.github.gumtreediff.tree.Tree;

public class TreeDistance {

    private Tree srcTree;
    private Tree dstTree;
    Node<StringNodeData> t1;
    Node<StringNodeData> t2;


    public TreeDistance(){}

    public TreeDistance(Tree srcTree, Tree dstTree) {
        this.srcTree = srcTree;
        this.dstTree = dstTree;
    }

    public void setSrcTree(Tree srcTree) {
        this.srcTree = srcTree;
    }

    public void setDstTree(Tree dstTree) {
        this.dstTree = dstTree;
    }

    //计算相似度值
    public float calculateTreeDistance(){
        TreeInputParser parser = new TreeInputParser();
        t1 = parser.fromTree(srcTree);
        t2 = parser.fromTree(dstTree);
        APTED<PerEditOperationStringNodeDataCostModel, StringNodeData> apted = new APTED<>(new PerEditOperationStringNodeDataCostModel(0.4f, 0.4f, 0.6f));
        return apted.computeEditDistance(t1, t2);
    }


}
