package cn.edu.pku.sei.codelinks.bean;

import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.memeber.FieldChangeEntity;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.ArrayList;
import java.util.List;

public class FieldData extends LinkBean{
    public FieldData(FieldChangeEntity ce){
        if(fieldName==null){
            fieldName = new ArrayList<>();
        }
        FieldDeclaration fd = null;
        switch(ce.stageIIBean.getOpt()) {
            case ChangeEntityDesc.StageIIOpt.OPT_CHANGE:
                Tree t = ce.clusteredActionBean.fafather;
                if (t.getAstNode().getNodeType() == ASTNode.FIELD_DECLARATION) {
                    fd = (FieldDeclaration) t.getAstNode();
                }
                break;
            case ChangeEntityDesc.StageIIOpt.OPT_DELETE:
            case ChangeEntityDesc.StageIIOpt.OPT_INSERT:
                if (ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)) {
                    fd = (FieldDeclaration) ce.bodyDeclarationPair.getBodyDeclaration();

                }
                break;

        }
        if(fd!=null){
            List<VariableDeclarationFragment> list = fd.fragments();
            for(VariableDeclarationFragment vd:list){
                fieldName.add(vd.getName().toString());
            }
            fieldType = fd.getType().toString();
        }


    }

    public List<String> fieldName;

    public String fieldType;
}

