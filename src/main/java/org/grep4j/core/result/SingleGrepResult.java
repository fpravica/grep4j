package org.grep4j.core.result;

import static org.grep4j.core.fluent.Dictionary.of;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * This class contains the result of the grep in String format.
 * 
 * @author Marco Castigliego
 * @author Giovanni Gargiulo
 *
 */
public class SingleGrepResult {

	private final String profileName;

	private final String fileName;

	private final String text;

	public SingleGrepResult(String profileName, String fileName, String text) {
		super();
		this.profileName = profileName;
		this.fileName = fileName;
		this.text = text;
	}

	/** 
	 * @return the profile name associated with this grep result
	 */
	public String getProfileName() {
		return profileName;
	}

	/** 
	 * @return the file name associated with this grep result
	 */
	public String getFileName() {
		return fileName;
	}

	/** 
	 * @return the text containing the result of the grep
	 */
	public String getText() {
		return text;
	}

	/**
	 * Given an expression, it counts how many times the pattern is found in the result
	 * Example: getOccourrences(of(expression));
	 * This method will ignore the 1st and last single quotes in order to compile regex:
	 * expression.replaceAll("(^')|('$)", "")
	 * 
	 * @param expression
	 * @return total number of time the patter is found
	 */
	public int getOccourrences(String expression) {
		int occurrences = 0;
		Pattern pattern = Pattern.compile(expression.replaceAll("(^')|('$)", ""));
		java.util.regex.Matcher matcher = pattern.matcher(this.getText());
		while (matcher.find()) {
			occurrences++;
		}
		return occurrences;
	}

	
}
