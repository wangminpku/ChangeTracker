package codeshovel.exceptions;

// wangmin: 定义了解析异常类，继承Exception
public class NoParserFoundException extends Exception {

	public NoParserFoundException(String message) {
		super(message);
	}

}
