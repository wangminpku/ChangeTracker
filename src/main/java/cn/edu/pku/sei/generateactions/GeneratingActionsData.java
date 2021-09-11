package cn.edu.pku.sei.generateactions;

import com.github.gumtreediff.actions.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratingActionsData {

    private List<Action> insertActions;
    private List<Action> updateActions;
    private List<Action> moveActions;
    private List<Action> deleteActions;
    private List<Action> allActions;

    private Map<Action, Integer> allActionMap;

    public GeneratingActionsData() {
        super();
        this.insertActions = new ArrayList<>();
        this.moveActions = new ArrayList<>();
        this.updateActions = new ArrayList<>();
        this.deleteActions = new ArrayList<>();
        this.allActions = new ArrayList<>();
        this.allActionMap = new HashMap<>();
    }

    public void addAction(Action action) {
        if (action instanceof Insert) {
            this.insertActions.add(action);
        } else if (action instanceof Update) {
            this.updateActions.add(action);
        } else if (action instanceof Move) {
            this.moveActions.add(action);
        } else if (action instanceof Delete) {
            this.deleteActions.add(action);
        }
        this.allActions.add(action);
    }

    public List<Action> getInsertActions() {
        return insertActions;
    }


    public List<Action> getUpdateActions() {
        return updateActions;
    }


    public List<Action> getMoveActions() {
        return moveActions;
    }


    public List<Action> getDeleteActions() {
        return deleteActions;
    }





    public List<Action> getAllActions() {
        return allActions;
    }


    public void generateActionMap() {
        for (Action tmp : this.insertActions) {
            this.allActionMap.put(tmp, 0);
        }
        for (Action tmp : this.moveActions) {
            this.allActionMap.put(tmp, 0);
        }
        for (Action tmp : this.updateActions) {
            this.allActionMap.put(tmp, 0);
        }
        for (Action tmp : this.deleteActions) {
            this.allActionMap.put(tmp, 0);
        }
    }

    public Map<Action, Integer> getAllActionMap() {
        return allActionMap;
    }
}
