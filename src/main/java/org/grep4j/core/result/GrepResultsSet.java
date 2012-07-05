package org.grep4j.core.result;

import static org.grep4j.core.fluent.Dictionary.of;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class contains an HashSet with all the results coming from the grep task
 * 
 * @author Marco Castigliego
 *
 */
public class GrepResultsSet implements Collection<GrepResult> {

	private final Set<GrepResult> grepResults;

	/**
	 * GlobalGrepResult is a container of different {@link GrepResult}  
	 * 
	 * @param the expression used to grep
	 */
	public GrepResultsSet() {
		grepResults = Collections.synchronizedSet(new HashSet<GrepResult>());
	}

	/**
	 * it counts how many times the pattern is found in all the results
	 * 
	 * @return total number of time the patter is found in all the GrepResults
	 */
	public int totalOccurrences() {
		int occurrences = 0;
		for (GrepResult result : grepResults) {
			occurrences += result.getOccourrences();
		}
		return occurrences;
	}

	/**
	 * it counts how many times the expressionToSearch pattern is found in all the results
	 * 
	 * 
	 * @param expressionToSearch
	 * @return total number of time the patter is found in all the GrepResults
	 */
	public int totalOccurrences(String expressionToSearch) {
		int occurrences = 0;
		for (GrepResult result : grepResults) {
			occurrences += result.getOccourrences(of(expressionToSearch));
		}
		return occurrences;
	}

	/**
	 * extract the lines that match with the passed regularExpression 
	 * @param expression
	 * @return the lines that match with the passed regularExpression 
	 */
	public GrepResultsSet filterByRE(String expression) {
		GrepResultsSet grepResultsSet = new GrepResultsSet();

		for (GrepResult result : grepResults) {
			GrepResult extractResult = result.filterByRE(expression);
			if (!extractResult.getText().isEmpty()) {
				grepResultsSet.add(extractResult);
			}
		}
		return grepResultsSet;
	}

	/**
	 * extract the lines that match with the passed regularExpression 
	 * @param expression
	 * @return the lines that match with the passed regularExpression 
	 */
	public GrepResultsSet filterBy(String expression) {
		GrepResultsSet grepResultsSet = new GrepResultsSet();

		for (GrepResult result : grepResults) {
			GrepResult extractResult = result.filterBy(expression);
			if (!extractResult.getText().isEmpty()) {
				grepResultsSet.add(extractResult);
			}
		}
		return grepResultsSet;
	}

	/**
	 * Add a {@link GrepResult} to the Set of results
	 * 
	 * @param grepResult
	 */
	@Override
	public boolean add(GrepResult e) {
		grepResults.add(e);
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends GrepResult> c) {
		grepResults.addAll(c);
		return true;
	}

	@Override
	public void clear() {
		grepResults.clear();

	}

	@Override
	public boolean contains(Object o) {
		return grepResults.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return grepResults.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return grepResults.isEmpty();
	}

	@Override
	public Iterator<GrepResult> iterator() {
		return grepResults.iterator();
	}

	@Override
	public boolean remove(Object o) {
		grepResults.remove(o);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		grepResults.removeAll(c);
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		grepResults.retainAll(c);
		return false;
	}

	@Override
	public int size() {
		return grepResults.size();
	}

	@Override
	public Object[] toArray() {
		return grepResults.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return grepResults.toArray(a);
	}

}