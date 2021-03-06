package cn.edu.pku.sei.changeentity;

import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.List;

public class ChangeEntityPrinter {

    public static void printContainerEntity(LayeredChangeEntityContainer container,CompilationUnit cu) {

        System.out.println("\nMember Key Size:" + container.getLayerMap().size());
        List<BodyDeclarationPair> keyList = container.getKeyIndex();
        for(BodyDeclarationPair bodyDeclarationPair : keyList){
            List<ChangeEntity> mList = container.getLayerMap().get(bodyDeclarationPair);
            if (mList == null || mList.size() == 0) {
                continue;
            }
            int startL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getStartPosition());
            int endL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getLength() + bodyDeclarationPair.getBodyDeclaration().getStartPosition() - 1);
            System.out.println(bodyDeclarationPair.toString() + " (" + startL + "," + endL + ")"+ " listSize:"+mList.size());
            for (ChangeEntity ce : mList) {
                System.out.println(ce.toString());
            }
            System.out.println("");
        }
    }

    public static void printContainerEntityNatural(LayeredChangeEntityContainer container,CompilationUnit cu) {
        System.out.println("\nMember key size:" + container.getLayerMap().size());
        System.out.println("Change entity size:" + container.getChangeEntitySize());
        List<BodyDeclarationPair> keyList = container.getKeyIndex();
        for(BodyDeclarationPair bodyDeclarationPair : keyList){
            List<ChangeEntity> mList = container.getLayerMap().get(bodyDeclarationPair);
            if (mList == null || mList.size() == 0) {
                continue;
            }
            int startL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getStartPosition());
            int endL = cu.getLineNumber(bodyDeclarationPair.getBodyDeclaration().getLength() + bodyDeclarationPair.getBodyDeclaration().getStartPosition() - 1);
            System.out.println(bodyDeclarationPair.toString() + " (" + startL + "," + endL + ")" + " listSize:"+mList.size());
            for (ChangeEntity ce : mList) {
                System.out.println(ce.toString2() +" "+ ce.getLineRange());
            }
            System.out.println("");
        }
    }
}
