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
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author Christoph Wurst <christoph@winzerhof-wurst.at>
 */
public class ScrumServiceImplTest {

	private SprintRepository sprintRepository;
	private ScrumServiceImpl service;

	@Before
	public void setUp() {
		sprintRepository = mock(SprintRepository.class);
		service = new ScrumServiceImpl();
		service.setSprintRepository(sprintRepository);
	}

	@Test
	public void getAllSprints() {
		List<Sprint> expected = new ArrayList<>();
		expected.add(new Sprint());
		expected.add(new Sprint());
		when(sprintRepository.findAll()).thenReturn(expected);

		assertSame(expected, service.getAllSprints());
	}

}
