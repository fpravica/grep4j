package org.grep4j.core.matchers;

import java.util.Set;

import org.grep4j.core.result.SingleGrepResult;
import org.hamcrest.Matcher;

public class GrepResultMatchers {

	/**
	 * Decorates another Matcher, retaining the behaviour but centralising the matchers related to the GrepResult
	 * eg. assertThat(results, containsExpression("INFO"));
	 */
	public static <T> Matcher<Set<SingleGrepResult>> containsExpression(String expression) {
		return org.grep4j.core.matchers.GrepResultContains.containsExpression(expression);
	}

	/**
	 * Decorates another Matcher, retaining the behaviour but centralising the matchers related to the GrepResult
	 * eg. assertThat(results, doesNotContainExpression("ERROR"));
	 */
	public static <T> Matcher<Set<SingleGrepResult>> doesNotContainExpression(String expression) {
		return org.grep4j.core.matchers.GrepResultDoesNotContain.doesNotContainExpression(expression);
	}

}