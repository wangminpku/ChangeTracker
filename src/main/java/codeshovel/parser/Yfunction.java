package codeshovel.parser;

import codeshovel.entities.Yexceptions;
import codeshovel.entities.Ymodifiers;
import codeshovel.entities.Yparameter;
import codeshovel.entities.Yreturn;
import codeshovel.wrappers.Commit;
import org.eclipse.jgit.lib.Repository;

import java.util.List;

//wangmin: 定义了一个方法的操作接口，包括获取该方法的一些基本构成成分的接口，以及与该方法相关的文件，commit之类的数据获取接口
public interface Yfunction {

	/**
	 * @return Method body as string
	 */
	String getBody();

	/**
	 * @return Method name as string
	 */
	String getName();

	/**
	 * @return List of parameters
	 */
	List<Yparameter> getParameters();

	/**
	 * @return Statement/type of the method. Should be {@code Yreturn.NONE} for untyped languages.
	 */
	Yreturn getReturnStmt();

	/**
	 * @return Method modifiers. Should be {@code Ymodifiers.NONE} for languages that don't have method modifiers.
	 */
	Ymodifiers getModifiers();

	/**
	 * @return Thrown exception types. Should be {@code Yexceptions.NONE} for untyped languages.
	 */
	Yexceptions getExceptions();

	/**
	 * @return Line number in which the method name appears.
	 */
	int getNameLineNumber();

	/**
	 * @return Line number in which the method ends (e.g. with `}`)
	 */
	int getEndLineNumber();

	/**
	 * @return Full commit SHA of the commit that has this method
	 */
	String getCommitName();

	/**
	 * @return Short 6-digit commit SHA of the commit that has this method
	 */
	String getCommitNameShort();

	/**
	 * @return Commit object that has this method
	 */
	Commit getCommit();

	/**
	 * @return Unique identifier for the method within the file
	 */
	String getId();

	/**
	 * Some AST parsers use paths for methods, especially for parent/child scope relationships. If the parser/language
	 * doesn't support this, return null.
	 *
	 * @return Path to the method in the file
	 */
	String getFunctionPath();

	/**
	 * @return Content of the source file as string where this method is in
	 */
	String getSourceFileContent();

	/**
	 * @return File path to the source file relative to the repository root directory
	 */
	String getSourceFilePath();

	/**
	 * @return The source code fragment for this method as string
	 */
	String getSourceFragment();

	/**
	 * @return If this method has some kind of parent (e.g. class, module), return its name. Otherwise return null.
	 */
	String getParentName();

}
