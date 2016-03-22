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

import at.christophwurst.orm.dao.SprintRepository;
import at.christophwurst.orm.domain.Sprint;
import java.util.Date;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */

public class BurnDownServiceImplTest {

	private SprintRepository sprintRepository;
	private BurnDownServiceImpl service;

	@Before
	public void setUp() {
		sprintRepository = mock(SprintRepository.class);
		service = new BurnDownServiceImpl();
		service.setSprintRepository(sprintRepository);
	}

	@Test
	public void testGetBurnDownDataWithNoData() {
		Sprint s = new Sprint();
		when(sprintRepository.getSprintAndWorklogs(13L)).thenReturn(s);
		
		Map<Date, Float> result = service.getBurnDownData(13L);
		assertEquals(0, result.size());
	}

}
