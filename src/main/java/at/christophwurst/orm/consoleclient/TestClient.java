/*
 * Copyright (C) 2016 Christoph Wurst <christoph@winzerhof-wurst.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.christophwurst.orm.consoleclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class TestClient implements CommandDispatcher {

	public static void main(String[] args) {
		try (AbstractApplicationContext factory = new ClassPathXmlApplicationContext(
			"at/christophwurst/worklog/config/spring-config.xml")) {

			TestClient client = factory.getBean(TestClient.class);

			ProjectCommands prc = factory.getBean(ProjectCommands.class);
			prc.registerCommands(client);
			StatisticsCommands stc = factory.getBean(StatisticsCommands.class);
			stc.registerCommands(client);
			ScrumCommands scc = factory.getBean(ScrumCommands.class);
			scc.registerCommands(client);

			client.run();
		}
	}

	@Inject
	private DbSeeder dbSeeder;
	private final Map<String, Command> commands;

	public TestClient() {
		commands = new HashMap<>();
	}

	@Override
	public void registerCommand(String identifier, Command command) {
		commands.put(identifier, command);
	}

	private void printAvailableCommands() {
		System.out.println("# Available commands:");
		commands.keySet().stream().forEach((id) -> {
			System.out.println(" - " + id);
		});
		System.out.println();
	}

	public void run() {
		System.out.print("Scrum project test client started");

		dbSeeder.seed();

		printAvailableCommands();

		String cmd = "";
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		while (!cmd.equals("exit")) {
			try {
				System.out.println();
				System.out.print("> ");
				cmd = buff.readLine();
				if (commands.containsKey(cmd)) {
					Command command = commands.get(cmd);
					command.execute();
				} else if (cmd.equals("?") || cmd.equals("help")) {
					printAvailableCommands();
				} else {
					System.err.println("unknown command <" + cmd + ">");
				}
			} catch (IOException ex) {
				System.err.println("error reading command");
				break;
			}
		}
	}

}
