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
package at.christophwurst.orm.service;

import at.christophwurst.orm.domain.LogbookEntry;
import at.christophwurst.orm.domain.Sprint;
import at.christophwurst.orm.domain.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import at.christophwurst.orm.dao.SprintRepository;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
class BurnDownServiceImpl implements BurnDownService {

	@Inject
	private SprintRepository sprintRepository;

	public void setSprintDao(SprintRepository sprintDao) {
		this.sprintRepository = sprintDao;
	}

	private Date stripDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	@Override
	public Map<Date, Float> getBurnDownData(Long sprintId) {
		Map<Date, Float> result = new HashMap<>();
		Sprint sprint = sprintRepository.getSprintAndWorklogs(sprintId);

		int totalTime = sprint.getRequirements().stream().mapToInt((s) -> {
			return s.getTasks().stream().mapToInt(Task::getEstimatedTime).sum();
		}).sum();

		SortedMap<Date, List<LogbookEntry>> workLogs = new TreeMap<>();
		sprint.getRequirements().forEach((r) -> {
			r.getTasks().forEach((t) -> {
				t.getLogbookEntries().forEach((e) -> {
					Date day = stripDate(e.getStartTime());
					if (!workLogs.containsKey(day)) {
						workLogs.put(day, new ArrayList<>());
					}
					workLogs.get(day).add(e);
				});
			});
		});

		int left = totalTime;
		for (Map.Entry<Date, List<LogbookEntry>> entry : workLogs.entrySet()) {
			long Ldone = entry.getValue().stream().mapToLong(LogbookEntry::getTotalTime).sum();
			int done = (int) (Ldone / (1000 * 3600));
			left -= done;
			result.put(entry.getKey(), (float) left / totalTime);
		}
		return result;
	}

}
