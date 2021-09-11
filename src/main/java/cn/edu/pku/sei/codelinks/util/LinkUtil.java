package cn.edu.pku.sei.codelinks.util;

import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.preprocessdata.MyRange;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class LinkUtil {

    public static int isRangeWithin(ChangeEntity ce1, ChangeEntity ce2) {
        MyRange myRange1 = ce1.getLineRange();
        MyRange myRange2 = ce2.getLineRange();
        int res= myRange1.isRangeWithin(myRange2);
        if ( res!= 0) {
            return res;
        } else {
            return res;
        }
    }

    public static String findResidingMethodName(Tree t){

        while(true){
            if(t.getAstNode().getClass().toString().endsWith("CompilationUnit")){
                break;
            }
            if(t.getAstNode().getClass().toString().endsWith("MethodDeclaration")){
                MethodDeclaration md = (MethodDeclaration) t.getAstNode();
                return md.getName().toString();
            }
            t = (Tree) t.getParent();
        }
        return null;

    }


    public static String[] findResidingClassAndSuperClass(Tree t){

        while(true){
            if(t.getAstNode().getClass().toString().endsWith("CompilationUnit")){
                break;
            }
            if(t.getAstNode().getClass().toString().endsWith("Declaration")){
                MethodDeclaration md = (MethodDeclaration) t.getAstNode();
//                return md.getName().toString();
            }
            t = (Tree) t.getParent();
        }
        return null;

    }
}
