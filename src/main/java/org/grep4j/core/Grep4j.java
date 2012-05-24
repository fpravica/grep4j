package org.grep4j.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.grep4j.core.model.Profile;
import org.grep4j.core.task.GrepRequest;
import org.grep4j.core.task.GrepResult;
import org.grep4j.core.task.GrepTask;

import com.google.common.collect.ImmutableList;

/**
 * Base Class for using the grep4j api. Usage example:
 * 
 * <pre>
 * Grep4j grep4j = grep(expression(), on(profiles()))
 * 		.withContextControls(getContextControls()).withWildcard(getWildcard())
 * 		.build();
 * grep4j.execute().andGetResults();
 * 
 * </pre>
 * <p>
 * Based on http://marcocast.github.com/grep4j/
 * 
 * @author Marco Castigliego
 * @author Giovanni Gargiulo
 */
public final class Grep4j {

	private final String expression;
	private final ImmutableList<Profile> profiles;

	private ImmutableList<String> contextControls;
	private String wildcard;

	private final Set<GrepResult> results;

	private final List<GrepRequest> grepRequests;

	private Grep4j(String expression, ImmutableList<Profile> profiles) {
		this.grepRequests = new ArrayList<GrepRequest>();
		this.results = new HashSet<GrepResult>();
		this.expression = expression;
		this.profiles = profiles;
	}

	private void setContextControls(ImmutableList<String> contextControls) {
		this.contextControls = contextControls;
	}

	private void setWildcard(String wildcard) {
		this.wildcard = wildcard;
	}

	/**
	 * This method will:
	 * <ol>
	 * <li>Verify the input checking that the mandatory fields have been populated</li>
	 * <li>Create the {@link GrepRequest} to be executed, based on the inputs passed</li>
	 * <li>Execute the {@link GrepRequest} for all the {@link Profile} passed</li>
	 * </ol>
	 */
	public Grep4j execute() {
		verifyInputs();
		prepareCommandRequests();
		executeCommands();
		return this;
	}

	/**
	 * @return a Set of {@link GrepResult}
	 */
	public Set<GrepResult> andGetResults() {
		return results;
	}

	private void verifyInputs() {
		if (expression == null || expression.isEmpty()) {
			throw new IllegalArgumentException(
					"No expression to grep was specified");
		}
		if (profiles == null || profiles.isEmpty()) {
			throw new IllegalArgumentException("No profile to grep was specified");
		}
	}

	private void executeCommands() {
		try {
			ExecutorService executorService = Executors.newFixedThreadPool(grepRequests.size());
			Set<Future<List<GrepResult>>> grepTaskFutures = new HashSet<Future<List<GrepResult>>>();
			for (GrepRequest grepRequest : grepRequests) {
				grepTaskFutures.add(executorService.submit(new GrepTask(grepRequest)));
			}
			for (Future<List<GrepResult>> future : grepTaskFutures) {
				results.addAll(future.get());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error when executing the commands", e);
		}
	}

	protected void prepareCommandRequests() {
		for (Profile profile : profiles) {
			GrepRequest grepRequest = new GrepRequest(expression, profile);
			if (contextControls != null && !contextControls.isEmpty()) {
				grepRequest.addContextControls(contextControls);
			}
			if (wildcard != null && !wildcard.isEmpty()) {
				grepRequest.addWildcard(wildcard);
			}
			grepRequests.add(grepRequest);
		}
	}

	protected String getExpression() {
		return expression;
	}

	protected List<GrepRequest> getGrepRequests() {
		return grepRequests;
	}

	protected List<String> getContextControls() {
		return contextControls;
	}

	protected String getWildcard() {
		return wildcard;
	}

	/**
	 * Class used to build {@link Grep4j}.
	 */
	public static class Builder {

		private final Grep4j grep4j;

		/**
		 * @param expression
		 * @param profiles
		 * @return Grep4j.Builder
		 */
		public static Builder grep(String expression, List<Profile> profiles) {
			return new Builder(expression, profiles);
		}

		/**
		 * @param profiles
		 * @return
		 */
		public static List<Profile> on(List<Profile> profiles) {
			return profiles;
		}

		/**
		 * @param contextControls
		 * @return
		 */
		public Builder withContextControls(List<String> contextControls) {
			grep4j.setContextControls(ImmutableList.copyOf(contextControls));
			return this;
		}

		/**
		 * @param wildcard
		 * @return
		 */
		public Builder withWildcard(String wildcard) {
			grep4j.setWildcard(wildcard);
			return this;
		}

		/**
		 * @return
		 */
		public Grep4j build() {
			return grep4j;
		}

		private Builder(String expression, List<Profile> profiles) {
			this.grep4j = new Grep4j(expression, ImmutableList.copyOf(profiles));
		}
	}
}
