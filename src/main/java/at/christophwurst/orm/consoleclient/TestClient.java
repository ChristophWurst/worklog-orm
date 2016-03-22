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

import at.christophwurst.orm.config.AppConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component()
public class TestClient implements Client {

	public static void main(String[] args) {
		try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext()) {
			context.register(AppConfig.class);
			context.refresh();

			Client client = context.getBean(Client.class);
			System.out.println(client.getClass().getName());

			ProjectCommands prc = context.getBean(ProjectCommands.class);
			prc.registerCommands(client);
			StatisticsCommands stc = context.getBean(StatisticsCommands.class);
			stc.registerCommands(client);
			ScrumCommands scc = context.getBean(ScrumCommands.class);
			scc.registerCommands(client);
			EmployeeCommands emc = context.getBean(EmployeeCommands.class);
			emc.registerCommands(client);

			client.run();
		}
	}

	@Inject
	private DbSeeder dbSeeder;
	@Inject
	private CommandExecutor commandExecutor;
	private final Map<String, Command> commands;

	public TestClient() {
		commands = new TreeMap<>();
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

	@Override
	public void run() {
		System.out.println("Scrum project test client started");

		dbSeeder.seed();

		printAvailableCommands();

		String cmd = "";
		BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
		ConsoleInterface consoleInterface = new ConsoleInterface() {

			private String promptFor(String name) {
				System.out.print("Enter " + name + " please> ");
				try {
					return buff.readLine();
				} catch (IOException ex) {
					return "";
				}
			}

			@Override
			public int getIntValue(String name) {
				return Integer.parseInt(promptFor(name));
			}

			@Override
			public long getLongValue(String name) {
				return Long.parseLong(promptFor(name));
			}

			@Override
			public String getStringValue(String name) {
				return promptFor(name);
			}

		};
		while (!cmd.equals("exit")) {
			try {
				System.out.println();
				System.out.print("> ");
				cmd = buff.readLine();
				if (commands.containsKey(cmd)) {
					commandExecutor.execute(commands.get(cmd), consoleInterface);
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
