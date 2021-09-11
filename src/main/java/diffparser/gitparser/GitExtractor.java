package diffparser.gitparser;

import diffparser.gitparser.commitutil.IHandleCommit;
import diffparser.gitparser.fileutil.FileRWUtil;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawText;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class GitExtractor {

    public Repository repository;
    public RevWalk revWalk;
    public String repoPath;
    public Git git;

    final static public int ALL_FILE = 0;
    final static public int JAVA_FILE = 1;
    final static public int CORE_JAVA_FILE = 2;

    public GitExtractor(String repopath){
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try{
            repository = builder.setGitDir(new File(repopath)).readEnvironment()
                    .findGitDir()
                    .build();
            revWalk = new RevWalk(repository);
            git = new Git(repository);
            repoPath = repopath;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private RevCommit revCommit;

    public List<String> getRepoCommitId(){
        List<String> commitids = new ArrayList<>();
        try (RevWalk revWalk = new RevWalk(repository)) {
            ObjectId commitId = repository.resolve("refs/heads/master");
            revWalk.markStart(revWalk.parseCommit(commitId));
            for (RevCommit commit : revWalk) {
                commitids.add(commit.getName());
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return commitids;
    }

    public Map<String, List<String>> getCommitFileList(String commmitid) {
        ObjectId commitId = ObjectId.fromString(commmitid);
        RevCommit commit = null;
        Map<String, List<String>> fileList = new HashMap<String, List<String>>();
        List<String> addList = new ArrayList<String>();
        List<String> modifyList = new ArrayList<String>();
        List<String> deleteList = new ArrayList<String>();
        try {
            commit = revWalk.parseCommit(commitId);
            RevCommit[] parentsCommits = commit.getParents();
            for (RevCommit parent : parentsCommits) {
                ObjectReader reader = git.getRepository().newObjectReader();
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                ObjectId newTree = commit.getTree().getId();
                newTreeIter.reset(reader, newTree);
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                RevCommit pCommit = revWalk.parseCommit(parent.getId());
                ObjectId oldTree = pCommit.getTree().getId();
                oldTreeIter.reset(reader, oldTree);
                DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
                diffFormatter.setRepository(git.getRepository());
                List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
                for (DiffEntry entry : entries) {
                    switch (entry.getChangeType()) {
                        case ADD:
                            addList.add(entry.getNewPath());
                            break;
                        case MODIFY:
                            modifyList.add(entry.getNewPath());
                            break;
                        case DELETE:
                            deleteList.add(entry.getNewPath());
                            break;
                        default:
                            break;
                    }
                }
                diffFormatter.close();
                fileList.put("addedFiles", addList);
                fileList.put("modifiedFiles", modifyList);
                fileList.put("deletedFiles", deleteList);
            }
            return fileList;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 带有parent id的file list
     */
    public Map<String, Map<String, List<String>>> getCommitParentMappedFileList(String commmitid) {
        Map<String, Map<String, List<String>>> result = new HashMap<String, Map<String, List<String>>>();
        ObjectId commitId = ObjectId.fromString(commmitid);
        RevCommit commit = null;
        Map<String, List<String>> fileList = null;
        List<String> addList = null;
        List<String> modifyList = null;
        List<String> deleteList = null;

        try {
            commit = revWalk.parseCommit(commitId);
            RevCommit[] parentsCommits = commit.getParents();
            for (RevCommit parent : parentsCommits) {
                fileList = new HashMap<String, List<String>>();
                addList = new ArrayList<String>();
                modifyList = new ArrayList<String>();
                deleteList = new ArrayList<String>();
                ObjectReader reader = git.getRepository().newObjectReader();
                CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
                ObjectId newTree = commit.getTree().getId();
                newTreeIter.reset(reader, newTree);
                CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
                RevCommit pCommit = revWalk.parseCommit(parent.getId());
                ObjectId oldTree = pCommit.getTree().getId();
                oldTreeIter.reset(reader, oldTree);
                DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
                diffFormatter.setRepository(git.getRepository());
                List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
                for (DiffEntry entry : entries) {
                    switch (entry.getChangeType()) {
                        case ADD:
                            addList.add(entry.getNewPath());
                            break;
                        case MODIFY:
                            modifyList.add(entry.getNewPath());
                            break;
                        case DELETE:
                            //System.out.println(entry.getNewPath());
                            //System.out.println(entry.getOldPath());
                            deleteList.add(entry.getOldPath());
                            break;
                        default:
                            break;
                    }
                }
                diffFormatter.close();
                fileList.put("addedFiles", addList);
                fileList.put("modifiedFiles", modifyList);
                fileList.put("deletedFiles", deleteList);
                result.put(pCommit.getName(), fileList);
            }
            return result;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * read commit time
     *
     * @param commitId
     * @return seconds since epoch
     */
    public int readCommitTime(ObjectId commitId) {
        try {
            RevCommit revCommit = revWalk.parseCommit(commitId);
            int time = revCommit.getCommitTime();
            return time;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * read commit time
     *
     * @param commitId
     * @return seconds since epoch
     */
    public int readCommitTime(String commitId) {
        try {
            ObjectId id = ObjectId.fromString(commitId);
            RevCommit revCommit = revWalk.parseCommit(id);
            int time = revCommit.getCommitTime();
            return time;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // out = new BufferedOutputStream(new FileOutputStream(fName));
    // DiffFormatter df = new DiffFormatter(out);
    // df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
    /**
     * get parents commit id in string
     *
     * @param commitId
     * @return
     */
    public String[] getCommitParents(String commitId) {
        ObjectId id = ObjectId.fromString(commitId);
        try {
            RevCommit commit = revWalk.parseCommit(id);
            RevCommit[] parentCommits = commit.getParents();
            String[] parentCommitsString = new String[parentCommits.length];
            for (int i = 0; i < parentCommits.length; i++) {
                parentCommitsString[i] = parentCommits[i].getName();
            }
            return parentCommitsString;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * checkout to one commit
     *
     * @param commitid
     */
    public void checkout(String commitid) {
        try {
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(commitid).call();
        } catch (RefAlreadyExistsException e) {
            e.printStackTrace();
        } catch (RefNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidRefNameException e) {
            e.printStackTrace();
        } catch (CheckoutConflictException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract file given file path and commit id
     * core/java/android/app/Activity.java
     *
     * @param fileName
     * @param revisionId
     * @return
     */
    public byte[] extract(String fileName, String revisionId) {
        if (revisionId == null || fileName == null) {
            System.err.println("revisionId/fileName is null..");
            return null;
        }
        if (repository == null || git == null || revWalk == null) {
            System.err.println("git repo is null..");
            return null;
        }
        RevWalk walk = new RevWalk(repository);
        try {
            ObjectId objId = repository.resolve(revisionId);
            if (objId == null) {
                System.err.println("The revision:" + revisionId + " does not exist.");
                walk.close();
                return null;
            }
            RevCommit commit = walk.parseCommit(repository.resolve(revisionId));
            if (commit != null) {
                RevTree tree = commit.getTree();
                TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, tree);
                ObjectId id = treeWalk.getObjectId(0);

                InputStream is = FileRWUtil.open(id, repository);
                byte[] res = FileRWUtil.toByteArray(is);
                return res;
            } else {
                System.err.println("Cannot found file(" + fileName + ") in revision(" + revisionId + "): " + revWalk);
            }
        } catch (RevisionSyntaxException e) {
            e.printStackTrace();
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (AmbiguousObjectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        walk.close();
        return null;
    }

    /**
     * extract file given file path and commit id
     * core/java/android/app/Activity.java
     *
     * @param fileName
     * @param revisionId
     * @return
     */
    public InputStream extractAndReturnInputStream(String fileName, String revisionId) {
        if (revisionId == null || fileName == null) {
            System.err.println("revisionId/fileName is null..");
            return null;
        }
        if (repository == null || git == null || revWalk == null) {
            System.err.println("git repo is null..");
            return null;
        }
        RevWalk walk = new RevWalk(repository);
        try {
            ObjectId objId = repository.resolve(revisionId);
            if (objId == null) {
                System.err.println("The revision:" + revisionId + " does not exist.");
                walk.close();
                return null;
            }
            RevCommit commit = walk.parseCommit(repository.resolve(revisionId));
            if (commit != null) {
                RevTree tree = commit.getTree();
                TreeWalk treeWalk = TreeWalk.forPath(repository, fileName, tree);
                ObjectId id = treeWalk.getObjectId(0);

                InputStream is = FileRWUtil.open(id, repository);
                return is;
            } else {
                System.err.println("Cannot found file(" + fileName + ") in revision(" + revisionId + "): " + revWalk);
            }
        } catch (RevisionSyntaxException e) {
            e.printStackTrace();
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (AmbiguousObjectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        walk.close();
        return null;
    }


    public boolean filterRule(int flag,String oldPath){
        if (GitExtractor.ALL_FILE == flag) {
            return true;
        } else if (GitExtractor.JAVA_FILE == flag && oldPath.endsWith(".java")) {
            return true;
        } else if (GitExtractor.CORE_JAVA_FILE == flag && oldPath.startsWith("core/java/") && oldPath.endsWith(".java")) {
            return true;
        }
        return false;
    }

    /**
     * edit script
     *
     * @param commmitid
     * @return
     */
    public List<String> getCommitEditScript(String commmitid, DiffEntry entry) {
//		ObjectId commitId = ObjectId.fromString(commmitid);
        RevCommit commit = null;
        try {
//			commit = revWalk.parseCommit(commitId);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter diffFormatter = new DiffFormatter(out);
            diffFormatter.format(entry);
            RawText r = new RawText(out.toByteArray());
            r.getLineDelimiter();
            System.out.println(out.toString());
            out.reset();
            diffFormatter.close();
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Ref> getAllBranches(){
        List<Ref> branches = null;
        try {
            branches = git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return branches;
    }
    public static String stampToDate(Long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDate(RevCommit rc) {
        Long s = rc.getCommitTime()*1000L;
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    public RevCommit revCommitOfBranchRef(Ref branch) {
        try {
            RevObject object;
            object = revWalk.parseAny(branch.getObjectId());
            if (object instanceof RevCommit) {
                RevCommit commit = (RevCommit) object;
                return commit;
            } else {
                System.err.println("invalid");
            }
            return null;
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void analyzeOneCommit(IHandleCommit iHandleCommit, String commitString) {
        try {

            ObjectId commitId = ObjectId.fromString(commitString);
            RevCommit commit = revWalk.parseCommit(commitId);
            if (commit.getParents() == null) {
                return;
            }
            Map<String, Map<String, List<String>>> changedFiles = this.getCommitParentMappedFileList(commit.getName());
            iHandleCommit.handleCommit(changedFiles, commitString,commit);

        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IncorrectObjectTypeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFilter(String filePathName){
        String name = filePathName.toLowerCase();
        if(!name.endsWith(".java")){
            return true;
        }
        if(name.contains("\\test\\")||name.contains("/test/")){
            return true;
        }
        String[] data = filePathName.split("/");
        String fileName = data[data.length-1];
        if(filePathName.endsWith("Test.java")||fileName.startsWith("Test")||filePathName.endsWith("Tests.java")){
            return true;
        }
        return false;
    }



}
