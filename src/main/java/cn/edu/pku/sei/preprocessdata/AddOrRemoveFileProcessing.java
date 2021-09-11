package cn.edu.pku.sei.preprocessdata;

import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.memeber.ClassChangeEntity;
import cn.edu.pku.sei.generateactions.JDTParserFactory;
import com.github.gumtreediff.actions.model.Insert;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class AddOrRemoveFileProcessing {

    public ChangeEntityData ced;

    public CompilationUnit cu;

    public List<String> linesList;

    public String FILE_TYPE;


    public AddOrRemoveFileProcessing(byte[] content, String fileType){
        try {
            FILE_TYPE = fileType;
            cu = JDTParserFactory.getCompilationUnit(content);
            this.linesList = new ArrayList<>();
            BodyDeclaration bodyDeclaration = (BodyDeclaration) cu.types().get(0);
            if(bodyDeclaration instanceof TypeDeclaration){
                init((TypeDeclaration)bodyDeclaration);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void init(TypeDeclaration typeDeclaration){
        int treeType = 0;
        if(FILE_TYPE.equals(ChangeEntityDesc.StageIIIFile.DST)){
            treeType = ChangeEntityDesc.StageITreeType.DST_TREE_NODE;
        }else{
            treeType = ChangeEntityDesc.StageITreeType.SRC_TREE_NODE;
        }
        ClassChangeEntity classChangeEntity = new ClassChangeEntity(new BodyDeclarationPair(typeDeclaration,typeDeclaration.getName().toString()+"."),
                Insert.class.getSimpleName(),
                new MyRange(cu.getLineNumber(typeDeclaration.getStartPosition()),cu.getLineNumber(typeDeclaration.getStartPosition()+typeDeclaration.getLength()),
                        treeType ));
        List<ChangeEntity> mList = new ArrayList<>();
        mList.add(classChangeEntity);
        ced = new ChangeEntityData(new MiningActionData(mList));
    }
}
