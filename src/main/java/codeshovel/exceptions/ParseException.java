package codeshovel.exceptions;


//wangmin:定义了解析异常类，继承Exception
public class ParseException extends Exception {

	private String fileName;
	private String fileContent;

	public ParseException(String message, String fileName, String fileContent) {
		super(message);
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileContent() {
		return fileContent;
	}
}
