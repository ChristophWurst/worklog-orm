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

import at.christophwurst.orm.service.BurnDownService;
import java.util.Date;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
@Component
public class ScrumCommands {

	@Inject
	private BurnDownService burnDownService;

	public void setBurnDownService(BurnDownService burnDownService) {
		this.burnDownService = burnDownService;
	}

	public void registerCommands(Client client) {
		client.registerCommand("scrum:burndown", (consoleInterface) -> {
			Long id = consoleInterface.getValue("Sprint ID");
			burnDownService.getBurnDownData(id).forEach((Date d, Float val) -> {
				System.out.println("    - " + d + ": " + val);
			});
		});
	}

}
