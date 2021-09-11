package cn.edu.pku.sei.generateactions;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.tree.AbstractTree;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.github.gumtreediff.tree.TreeUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyActionGenerator {

    private ITree origSrc;

    private ITree copySrc;

    private ITree origDst;

    private MappingStore origMappings;

    private MappingStore newMappings;

    private Set<ITree> dstInOrder;

    private Set<ITree> srcInOrder;

    private int lastId;


    private TIntObjectMap<ITree> origSrcTrees;

    private TIntObjectMap<ITree> copySrcTrees;

    public MyActionGenerator(ITree src, ITree dst, MappingStore mappings) {
        this.origSrc = src;
        this.copySrc = this.origSrc.deepCopy();
        this.origDst = dst;

        origSrcTrees = new TIntObjectHashMap<>();
        for (ITree t: origSrc.getTrees())
            origSrcTrees.put(t.getId(), t);
        copySrcTrees = new TIntObjectHashMap<>();
        for (ITree t: copySrc.getTrees())
            copySrcTrees.put(t.getId(), t);

        origMappings = new MappingStore();
        for (Mapping m: mappings)
            this.origMappings.link(copySrcTrees.get(m.getFirst().getId()), m.getSecond());
        this.newMappings = origMappings.copy();
        myAgbData = new GeneratingActionsData();
    }

    public MyActionGenerator(JavaParserTreeGenerator generator) {
        this.origSrc = generator.src;
        this.copySrc = this.origSrc.deepCopy();
        this.origDst = generator.dst;

        origSrcTrees = new TIntObjectHashMap<>();
        for (ITree t: origSrc.getTrees())
            origSrcTrees.put(t.getId(), t);
        copySrcTrees = new TIntObjectHashMap<>();
        for (ITree t: copySrc.getTrees())
            copySrcTrees.put(t.getId(), t);

        origMappings = new MappingStore();
        for (Mapping m: generator.mapping)
            this.origMappings.link(copySrcTrees.get(m.getFirst().getId()), m.getSecond());
        this.newMappings = origMappings.copy();
        myAgbData = new GeneratingActionsData();
    }


    public GeneratingActionsData myAgbData;


    public GeneratingActionsData generate() {
        myAgbData = new GeneratingActionsData();
        ITree srcFakeRoot = new AbstractTree.FakeTree(copySrc);
        ITree dstFakeRoot = new AbstractTree.FakeTree(origDst);
        copySrc.setParent(srcFakeRoot);
        origDst.setParent(dstFakeRoot);

        dstInOrder = new HashSet<>();
        srcInOrder = new HashSet<>();

        lastId = copySrc.getSize() + 1;
        newMappings.link(srcFakeRoot, dstFakeRoot);

        List<ITree> bfsDst = TreeUtils.breadthFirst(origDst);
//        List<ITree> bfsDst = MyTreeUtil.layeredBreadthFirst(origDst, myAgbData.getDstLayerLastNodeIndex());
        for (int i=1;i<=bfsDst.size();i++){
            ITree dstItem = bfsDst.get(i-1);
            ITree mappedSrcNode = null;
            ITree parentOfDstNode = dstItem.getParent();
            ITree mappingSrcOfParentDst = newMappings.getSrc(parentOfDstNode);

            if (!newMappings.hasDst(dstItem)) {
                //item is not in src
                int k = findPos(dstItem);
                // Insertion case : insert new node.
                mappedSrcNode = new AbstractTree.FakeTree();
                mappedSrcNode.setId(newId());
                // In order to use the real nodes from the second tree, we
                // furnish x instead of w and fake that x has the newly
                // generated ID.
                // insert增加过程中，tree也在更新，mapping也在更新
                Action ins = new Insert(dstItem, origSrcTrees.get(mappingSrcOfParentDst.getId()), k);
                Tree tmp = (Tree) dstItem;
                tmp.setDoAction(ins);
                myAgbData.addAction(ins);
                origSrcTrees.put(mappedSrcNode.getId(), dstItem);
                newMappings.link(mappedSrcNode, dstItem);
                mappingSrcOfParentDst.getChildren().add(k, mappedSrcNode);
                mappedSrcNode.setParent(mappingSrcOfParentDst);
            } else {
                //in
                // 有mapping
                mappedSrcNode = newMappings.getSrc(dstItem);
                if (!dstItem.equals(origDst)) { // TODO => x != origDst // Case of the root
                    ITree mappedSrcNodeParent = mappedSrcNode.getParent();
                    if (!mappedSrcNode.getLabel().equals(dstItem.getLabel())) {
                        Update upd = new Update(origSrcTrees.get(mappedSrcNode.getId()), dstItem.getLabel());
                        myAgbData.addAction(upd);
//                        Tree tmp = (Tree) dstItem;
                        ITree srcNode = origSrcTrees.get(mappedSrcNode.getId());
                        Tree srcTree = (Tree) srcNode;
                        srcTree.setDoAction(upd);

                        mappedSrcNode.setLabel(dstItem.getLabel());
                    }
                    if (!mappingSrcOfParentDst.equals(mappedSrcNodeParent)) {
                        int k = findPos(dstItem);
                        Action mv = new Move(origSrcTrees.get(mappedSrcNode.getId()), origSrcTrees.get(mappingSrcOfParentDst.getId()), k);
                        Tree tmp = (Tree) origSrcTrees.get(mappedSrcNode.getId());
                        tmp.setDoAction(mv);
                        myAgbData.addAction(mv);

                        int oldk = mappedSrcNode.positionInParent();
                        mappingSrcOfParentDst.getChildren().add(k, mappedSrcNode);
                        mappedSrcNode.getParent().getChildren().remove(oldk);
                        mappedSrcNode.setParent(mappingSrcOfParentDst);
                    }
                }
            }

            //FIXME not sure why :D
            srcInOrder.add(mappedSrcNode);
            dstInOrder.add(dstItem);
            alignChildren(mappedSrcNode, dstItem,i);
        }
//        for (ITree w : copySrc.postOrder()) {
        for(ITree w :copySrc.breadthFirst()){
            if (!newMappings.hasSrc(w)) {
                Delete del = new Delete(origSrcTrees.get(w.getId()));
                Tree tmp = (Tree) origSrcTrees.get(w.getId());
                tmp.setDoAction(del);
                myAgbData.addAction(del);
            }
            //w.getParent().getChildren().remove(w);
//            }
        }
        return myAgbData;
        //FIXME should ensure isomorphism.
    }

    private void alignChildren(ITree w, ITree x,int nodeIndex) {
        srcInOrder.removeAll(w.getChildren());
        dstInOrder.removeAll(x.getChildren());

        List<ITree> s1 = new ArrayList<>();
        for (ITree c: w.getChildren())
            if (newMappings.hasSrc(c))
                if (x.getChildren().contains(newMappings.getDst(c)))
                    s1.add(c);

        List<ITree> s2 = new ArrayList<>();
        for (ITree c: x.getChildren())
            if (newMappings.hasDst(c))
                if (w.getChildren().contains(newMappings.getSrc(c)))
                    s2.add(c);

        List<Mapping> lcs = lcs(s1, s2);

        for (Mapping m : lcs) {
            srcInOrder.add(m.getFirst());
            dstInOrder.add(m.getSecond());
        }

        for (ITree a : s1) {
            for (ITree b: s2 ) {
                if (origMappings.has(a, b)) {
                    if (!lcs.contains(new Mapping(a, b))) {
                        int k = findPos(b);
                        Action mv = new Move(origSrcTrees.get(a.getId()), origSrcTrees.get(w.getId()), k);
                        Tree tmp = (Tree) origSrcTrees.get(a.getId());
                        tmp.setDoAction(mv);
                        myAgbData.addAction(mv);
                        //System.out.println(mv);
                        int oldk = a.positionInParent();
                        w.getChildren().add(k, a);
                        if (k  < oldk ) // FIXME this is an ugly way to patch the index
                            oldk ++;
                        a.getParent().getChildren().remove(oldk);
                        a.setParent(w);
                        srcInOrder.add(a);
                        dstInOrder.add(b);
                    }
                }
            }
        }
    }

    private int findPos(ITree x) {
        ITree y = x.getParent();
        List<ITree> siblings = y.getChildren();

        for (ITree c : siblings) {
            if (dstInOrder.contains(c)) {
                if (c.equals(x)) return 0;
                else break;
            }
        }

        int xpos = x.positionInParent();
        ITree v = null;
        for (int i = 0; i < xpos; i++) {
            ITree c = siblings.get(i);
            if (dstInOrder.contains(c)) v = c;
        }

        //if (v == null) throw new RuntimeException("No rightmost sibling in order");
        if (v == null) return 0;

        ITree u = newMappings.getSrc(v);
        // siblings = u.getParent().getChildren();
        // int upos = siblings.indexOf(u);
        int upos = u.positionInParent();
        // int r = 0;
        // for (int i = 0; i <= upos; i++)
        // if (srcInOrder.contains(siblings.get(i))) r++;
        return upos + 1;
    }

    private int newId() {
        return ++lastId;
    }

    private List<Mapping> lcs(List<ITree> x, List<ITree> y) {
        int m = x.size();
        int n = y.size();
        List<Mapping> lcs = new ArrayList<>();

        int[][] opt = new int[m + 1][n + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (newMappings.getSrc(y.get(j)).equals(x.get(i))) opt[i][j] = opt[i + 1][j + 1] + 1;
                else  opt[i][j] = Math.max(opt[i + 1][j], opt[i][j + 1]);
            }
        }

        int i = 0, j = 0;
        while (i < m && j < n) {
            if (newMappings.getSrc(y.get(j)).equals(x.get(i))) {
                lcs.add(new Mapping(x.get(i), y.get(j)));
                i++;
                j++;
            } else if (opt[i + 1][j] >= opt[i][j + 1]) i++;
            else j++;
        }

        return lcs;
    }
}
