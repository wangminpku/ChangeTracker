package cn.edu.pku.sei.changeentity.base;

import com.github.gumtreediff.actions.model.Action;

public class ChangeEntityDesc {

    public static class StageITreeType{
        public static final int SRC_TREE_NODE = 3;
        public static final int DST_TREE_NODE = 4;


    }
    public static class StageITraverseType{
        public static final int TRAVERSE_UP_DOWN = 1;
        public static final int TRAVERSE_DOWN_UP = 2;
    }

    public static class StageIIOpt {

        public static final String OPT_INSERT = "Insert";

        public static final String OPT_MOVE = "Move";

        public static final String OPT_DELETE = "Delete";

        public static final String OPT_CHANGE = "Update";

        public static final String OPT_CHANGE_MOVE = "Change.Move";
    }


    public static class StageIIENTITY {

        public static final String ENTITY_CLASS = "ClassDeclaration";

        public static final String ENTITY_INNER_CLASS = "InnerClassDeclaration";

        public static final String ENTITY_INTERFACE = "Interface";

        public static final String ENTITY_FIELD = "FieldDeclaration";

        public static final String ENTITY_ENUM = "Enum";

        public static final String ENTITY_INITIALIZER = "Initializer";

        public static final String ENTITY_METHOD = "MethodDeclaration";

        public static final String ENTITY_ASSERT = "Assert";

        public static final String ENTITY_BREAK = "Break";

        public static final String ENTITY_CONTINUE = "Continue";

        public static final String ENTITY_EXPRESSION_STMT = "ExpressionStatement";

        public static final String ENTITY_CONSTRUCTOR_INVOCATION = "ConstructorInvocation";

        public static final String ENTITY_SUPER_CONSTRUCTOR_INVOCATION = "SuperConstructorInvocation";

        public static final String ENTITY_LABELED_STATEMENT = "LabeledStatement";

        public static final String ENTITY_FOR_STMT = "For";

        public static final String ENTITY_IF_STMT = "If";

        public static final String ENTITY_RETURN_STMT = "Return";

        public static final String ENTITY_SWITCH_STMT = "Switch";

        public static final String ENTITY_SWITCH_CASE = "SwitchCase";

        public static final String ENTITY_SYNCHRONIZED_STMT = "Synchronized";

        public static final String ENTITY_TRY_STMT = "Try";

        public static final String ENTITY_VARIABLE_STMT = "VariableDeclaration";

        public static final String ENTITY_WHILE_STMT = "While";

        public static final String ENTITY_DO_STMT = "Do";

        public static final String ENTITY_EMPTY_STMT = "Empty";

        public static final String ENTITY_LABELED_STMT = "Labeled";

        public static final String ENTITY_THROW_STMT = "Throw";

        public static final String ENTITY_ENHANCED_FOR_STMT = "EnhancedFor";

        public static final String ENTITY_TYPE_DECLARATION_STMT = "TypeDeclaration(ClassDeclaration)";

    }

    public static class StageIIOpt2 {

        public static final String OPT2_INSERT = "Insert";

        public static final String OPT2_DELETE = "Delete";

        public static final String OPT2_MOVE = "Move";

        public static final String OPT2_UPDATE = "Update";

        public static final String OPT2_CHANGE = "Update";

    }

    //wang 4/12
    public static class StageIIOpt2Exp{
        public static final String EXP_MODIFIER = "Modifier";
        public static final String EXP_TYPE = "Type";
        public static final String EXP_PARAMETER = "Parameter";
        public static final String EXP_THROWN_EXCEPTION = "Thrown Exception";
        public static final String EXP_EXTENDED_TYPE = "Extended Type";
        public static final String EXP_IMPLEMENTED_TYPE = "Implemented Type";
        public static final String EXP_TYPE_PARAMETER = "Type Parameter";
        public static final String EXP_SUPER_CLASS = "Type Parameter";
    }

    public static String getChangeEntityDescString(Action a){
        switch (a.getClass().getSimpleName()){
            case "Insert":return StageIIOpt.OPT_INSERT;
            case "Move":return StageIIOpt.OPT_MOVE;
            case "Delete":return StageIIOpt.OPT_DELETE;
            case "Update":break;
        }
        return null;
    }

    public static String getKeyNameByValue(String type){
        return type;
    }



    public static class StageIISub {

        public static final String SUB_DECLARATION = "declaration";


        /**
         * ???????????????if??????
         */
        public static final String SUB_CONDITION = "condition";

        public static final String SUB_CONDITION_AND_BODY = "condition and body";

        public static final String SUB_CONDITION_AND_PARTIAL_BODY = "condition and body(with moved statements)";

        public static final String SUB_ELSE = "Else";

        /**
         * ??????class
         */

        public static final String SUB_SIGNATURE = "Signature";

        public static final String SUB_SIGNATURE_AND_BODY = "Signature and body";

        /**
         * Try
         */
        public static final String SUB_CATCH_CLAUSE = "Catch clause";
        public static final String SUB_BODY_AND_CATCH_CLAUSE = "Body and Catch Clause";

        public static final String SUB_BODY_AND_CATCH_CLAUSE_AND_FINALLY = "Body and Catch Clause and Finally";

        public static final String SUB_FINALLY = "Finally";


        /**
         * switch
         */
        public static final String SUB_SWITCH_CASE = "switch case";
        public static final String SUB_SWITCH_CASE_DEFAULT = "default switch case";

    }


    public static class StageIIGranularity{
        public static final String GRANULARITY_MEMBER = "Member";

        public static final String GRANULARITY_CLASS = "ClassOrInterface";

        public static final String GRANULARITY_STATEMENT = "Statement";
    }




    public static class StageIIGenStage{

        public static final String ENTITY_GENERATION_STAGE_PRE_DIFF = "PRE_DIFF";
        public static final String ENTITY_GENERATION_STAGE_GT_UD = "UP_DOWN";
        public static final String ENTITY_GENERATION_STAGE_GT_DUD = "DOWN_UP_DOWN";
    }


    public static class StageIIIAssociationType{

        //        public static final String TYPE_SAME_VARIABLE = "a same variable as b";
//
//        public static final String TYPE_CONTROL = "a control dependency b";
//
//        public static final String TYPE_FIELD_ACCESS = "a access field b";
//
//        public static final String TYPE_CALL_METHOD = "a call method b";
//
//        public static final String TYPE_CROSS_FILE_CALL_METHOD = "a call method b";
//
//        public static final String TYPE_PARAMETER_CHANGE_VAR_CHANGE = "a parameter change -> b variable change";
//
//        public static final String TYPE_SHARE_FIELD = "a field acess same as b";
//
//        public static final String TYPE_CLASS_CREATION = "a class creation of b";
//
//        public static final String TYPE_METHOD_OVERRIDE = "a overrides method of b";
        public static final String DEF_INNER_USE = "Def-Inner-Use: %s invoked in %s";
        public static final String DEF_OUTER_USE = "Def-Outer-Use: %s invoked in %s";
        public static final String CLASS_INSTANCE = "Class-Instance: %s instanced in %s";
        public static final String CLASS_METHOD_INVOKE = "Class_Method_Invoke : %s invoked by instance %s";
        public static final String METHOD_INNER_INVOKE = "Method-Inner-Invoke: %s invoked in %s";
        public static final String METHOD_OUTER_INVOKE = "Method-Outer-Invoke: %s invoked in %s";
        public static final String ABSTRACT_METHOD = "Abstract-Method: %s implemented abstract method in %s";
        public static final String OVERRIDE_METHOD = "Override-Method: %s overridden method in %s";
        public static final String IMPLEMENT_METHOD = "Implement-Method: %s implemented interface in %s";
        public static final String SIMSILAR_CHANGE = "Similar-Change";

    }

    public static class StageIIIKeys{
        public static final String ID = "id";
        public static final String KEYY = "key";
        public static final String FILE = "file";
        public static final String RANGE = "range";
        public static final String TYPE1 = "type1";
        public static final String TYPE2 = "type2";
        public static final String DESCRIPTION = "description";
        public static final String SUB_RANGE = "sub-range";
        public static final String SUB_RANGE_CODE = "sub-range-code";
        public static final String SUB_TYPE  = "sub-type";

        public static final String OPT2EXP2 = "opt2-exp2";
    }



    public static class StageIIIFile{
        public static final String SRC = "src";
        public static final String DST = "dst";
        public static final String SRC_DST = "src-dst";
    }

}
