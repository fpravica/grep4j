package org.grep4j.core;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.grep4j.core.command.ServerDetailsInterpreter;
import org.grep4j.core.command.linux.CommandExecutor;
import org.grep4j.core.command.linux.JschCommandExecutor;
import org.grep4j.core.command.linux.LocalCommandExecutor;
import org.grep4j.core.model.ServerDetails;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WhenAServeDetailIsInterpreted {

	@DataProvider(name = "correctLocalHosts")
	public Object[][] createCorrectLocalHosts() {
		return new Object[][] { { "Localhost" }, { "localhost" }, { "localHost" }, { "LOCALHOST" }, { "127.0.0.1" }, { "localHOST" },
				{ "LoCaLhoST" }, };
	}

	@DataProvider(name = "incorrectLocalHosts")
	public Object[][] createIncorrectLocalHosts() {
		return new Object[][] { { "Localhosst" }, { "localhst" }, { "ocalHost" }, { "172.12.22.22" }, { "127.0.1.1" }, };
	}

	private ServerDetails serverDetails;

	@Test(dataProvider = "correctLocalHosts")
	public void andTheHostIsLocalALocalCommandExecutorShouldBeUsed(String host) {
		serverDetails = new ServerDetails(host);
		CommandExecutor commandExecutor = ServerDetailsInterpreter.getCommandExecutor(serverDetails);
		assertThat(commandExecutor, is(LocalCommandExecutor.class));
	}

	@Test(dataProvider = "incorrectLocalHosts")
	public void andTheHostIsNotLocalASshCommandExecutorShouldBeUsed(String host) {
		serverDetails = new ServerDetails(host);
		CommandExecutor commandExecutor = ServerDetailsInterpreter.getCommandExecutor(serverDetails);
		assertThat(commandExecutor, is(JschCommandExecutor.class));
	}

}
