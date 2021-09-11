package cn.edu.pku.sei.fileutil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.WorkingTreeOptions;

public class FileRWUtil {

    public static List<String> getLines(File f){
        FileInputStream fos;
        List<String> result = new ArrayList<String>();
        String line;
        try {
            fos = new FileInputStream(f);
            InputStreamReader ir = new InputStreamReader(fos);
            BufferedReader br = new BufferedReader(ir);
            while((line = br.readLine())!= null){
                result.add(line.trim());
            }
            br.close();
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static List<String> getLines(byte[] buffer){
        InputStream sbs = new ByteArrayInputStream(buffer);
        List<String> result = new ArrayList<String>();
        String line;
        try {
            InputStreamReader ir = new InputStreamReader(sbs);
            BufferedReader br = new BufferedReader(ir);
            while((line = br.readLine())!= null){
                result.add(line.trim());
            }
            br.close();
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    public static boolean fileOutput(byte[] a,String output){
        try {
            FileOutputStream fos=new FileOutputStream(new File(output));
            fos.write(a);
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static InputStream open(ObjectId blobId, Repository db) throws IOException, IncorrectObjectTypeException {
        if (blobId == null)
            return new ByteArrayInputStream(new byte[0]);

        try {
            WorkingTreeOptions workingTreeOptions = db.getConfig().get(WorkingTreeOptions.KEY);
            switch (workingTreeOptions.getAutoCRLF()) {
                case INPUT:
                    // When autocrlf == input the working tree could be either CRLF
                    // or LF, i.e. the comparison
                    // itself should ignore line endings.
                case FALSE:
                    return db.open(blobId, Constants.OBJ_BLOB).openStream();
                case TRUE:
                default:
                    return db.open(blobId, Constants.OBJ_BLOB).openStream();
//				return new AutoCRLFInputStream(db.open(blobId, Constants.OBJ_BLOB).openStream(), true);
            }
        } catch (MissingObjectException notFound) {
            return null;
        }
    }

    public static File getFileFromeBytes(byte[] b,String outputFile){
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

}
