/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2017 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package com.github.gumtreediff.actions;

//import cn.edu.fudan.se.apiChangeExtractor.gumtreeParser.JGraphPanel;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.TreeContext;
import org.jgrapht.DirectedGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionClusterFinder {

    private TreeContext src;

    @SuppressWarnings("unused")
	private TreeContext dst;

    @SuppressWarnings("unused")
	private List<Action> actions;

    private DirectedGraph<Action, DefaultEdge> graph;

    private List<Set<Action>> clusters;
    
    public List<Action> startNodes = new ArrayList<>();

    public ActionClusterFinder(TreeContext src, TreeContext dst, List<Action> actions) {
        this.src = src;
        this.dst = dst;
        this.actions = actions;
        startNodes.addAll(actions);
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);

        for (Action a: actions)
            graph.addVertex(a);
        
        for (Action a1: actions) {
            for (Action a2: actions) {
                if (a1 != a2) {
                    if (embeddedInserts(a1, a2) || sameValueUpdates(a1, a2) || sameParentMoves(a1, a2) || embeddedDeletes(a1, a2)){
                    	graph.addEdge(a1, a2);
                    	startNodes.remove(a1);
                    }
                }
            }
        }

        ConnectivityInspector<Action, DefaultEdge> alg = new ConnectivityInspector<>(graph);
        clusters = alg.connectedSets();
    }
    
    public List<Action> getStartNodes() {
        return startNodes;
    }
    public List<Set<Action>> getClusters() {
        return clusters;
    }
    public void show(){
//    	JGraphPanel frame = new JGraphPanel(graph, clusters);
//    	frame.init();
    }

    private boolean embeddedInserts(Action a1, Action a2) {
        if (!(a1 instanceof Insert && a2 instanceof Insert))
            return false;
        Insert i1 = (Insert) a1;
        Insert i2 = (Insert) a2;
        if (i1.getParent().equals(i2.getNode()))
            return true;
        else
            return false;
    }
    
    private boolean embeddedAdditions(Action a1, Action a2) {
        if (!(a1 instanceof Addition && a2 instanceof Addition))
            return false;
        Addition i1 = (Addition) a1;
        Addition i2 = (Addition) a2;
        if (i1.getParent().equals(i2.getNode()))
            return true;
        else
            return false;
    }
    
    private boolean embeddedDeletes(Action a1, Action a2) {
        if (!(a1 instanceof Delete && a2 instanceof Delete))
            return false;
        Delete d1 = (Delete) a1;
        Delete d2 = (Delete) a2;
        if (d2.getNode().getParent() == null)
            return false;
        if (d2.getNode().getParent().equals(d1.getNode()))
            return true;
        else
            return false;
    }

    private boolean sameParentMoves(Action a1, Action a2) {
        if (!(a1 instanceof Move && a2 instanceof Move))
            return false;
        Move m1 = (Move) a1;
        Move m2 = (Move) a2;
        if (m1.getNode() == null)
            return false;
        if (m2.getNode() == null)
            return false;
        if (m1.getNode().getParent().equals(m2.getNode().getParent()))
            return true;
        else
            return false;
    }

    private boolean sameValueUpdates(Action a1, Action a2) {
        if (!(a1 instanceof Update && a2 instanceof Update))
            return false;
        Update u1 = (Update) a1;
        Update u2 = (Update) a2;
        if (u1.getValue().equals(u2.getValue()))
            return true;
        else
            return false;
    }

    public String getClusterLabel(Set<Action> cluster) {
        if (cluster.size() == 0)
            return "Unknown cluster type";
        Action first = cluster.iterator().next();
        if (first instanceof Insert) {
            Insert root = null;
            for (Action a : cluster)
                if (graph.inDegreeOf(a) == 0)
                    root = (Insert) a;
            return root.format(src);
        } else if (first instanceof Move) {
            Move m = (Move) first;
            return "MOVE from " + m.getParent().toPrettyString(src);
        } else if (first instanceof Update) {
            Update u = (Update) first;
            return "UPDATE from " + first.getNode().getLabel() + " to " + u.getValue();
        } else if (first instanceof Delete) {
            Delete root = null;
            for (Action a : cluster)
                if (graph.inDegreeOf(a) == 0)
                    root = (Delete) a;
            return root.format(src);
        } else
            return "Unknown cluster type";
    }

}
