package com.viadee.sonarquest.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.viadee.sonarquest.dto.StandardTaskDTO;
import com.viadee.sonarquest.entities.StandardTask;
import com.viadee.sonarquest.entities.World;
import com.viadee.sonarquest.externalressources.SonarQubeSeverity;
import com.viadee.sonarquest.repositories.StandardTaskRepository;
import com.viadee.sonarquest.rules.SonarQuestStatus;
import com.viadee.sonarquest.skillTree.services.UserSkillService;
import com.viadee.sonarquest.utils.mapper.StandardTaskDtoEntityMapper;

@RunWith(MockitoJUnitRunner.class)
public class StandardTaskServiceTest {

	@Mock
	private StandardTaskRepository standardTaskRepository;

	@Mock
	private StandardTaskDtoEntityMapper standardTaskMapper;

	@Mock
	private GratificationService gratificationService;

	@Mock
	private UserSkillService userSkillService;

	@Mock
	private NamedParameterJdbcTemplate template;

	@InjectMocks
	private StandardTaskService standardTaskService;

	@Test
	public void testUpdateStandardTask() {
		StandardTask task = new StandardTask();
		task.setKey("Color of Magic!");
		task.setStatus(SonarQuestStatus.OPEN);
		when(standardTaskRepository.save(Matchers.any(StandardTask.class))).thenReturn(task);
		when(standardTaskRepository.saveAndFlush(Matchers.any(StandardTask.class))).thenReturn(task);
		when(standardTaskRepository.findByKey(task.getKey())).thenReturn(task);
		when(template.queryForObject(Matchers.anyString(), Matchers.any(SqlParameterSource.class),
				Matchers.<Class<String>>any())).thenReturn("OPEN");

		task = standardTaskService.updateStandardTask(task);

		assertEquals(SonarQuestStatus.OPEN, task.getStatus());

		// case: existing created task -> no external changes
		final StandardTask createdStandardTask = new StandardTask();
		createdStandardTask.setKey("createdStandardTask");
		createdStandardTask.setStatus(SonarQuestStatus.OPEN);

		when(standardTaskRepository.findByKey(createdStandardTask.getKey())).thenReturn(createdStandardTask);
		standardTaskService.updateStandardTask(createdStandardTask);

		assertEquals(SonarQuestStatus.OPEN, createdStandardTask.getStatus());

		// case: existing created task -> no external changes
	}

	@Test
	public void testGetLastState() throws Exception {
		StandardTask task = new StandardTask();
		SonarQuestStatus lastState = standardTaskService.getLastState(task);
		assertEquals(SonarQuestStatus.OPEN, lastState);
	}

	@Test
	public void testFindByWorld() throws Exception {
		// Given
		World world = new World();

		List<StandardTask> standardTasks = new ArrayList<>();
		standardTasks.add(mockStandardTask("BLOCKER"));
		standardTasks.add(mockStandardTask("MAJOR"));
		standardTasks.add(mockStandardTask("CRITICAL"));
		standardTasks.add(mockStandardTask("MAJOR"));

		List<StandardTaskDTO> unsortedTasks = new ArrayList<>();

		unsortedTasks.add(mockStandardTaskDTO("BLOCKER"));
		unsortedTasks.add(mockStandardTaskDTO("MAJOR"));
		unsortedTasks.add(mockStandardTaskDTO("CRITICAL"));
		unsortedTasks.add(mockStandardTaskDTO("MAJOR"));

		when(standardTaskMapper.enitityToDto(Matchers.any(StandardTask.class))).thenReturn(unsortedTasks.get(0))
				.thenReturn(unsortedTasks.get(1)).thenReturn(unsortedTasks.get(2)).thenReturn(unsortedTasks.get(3));
		
		when(standardTaskRepository.findByWorld(Matchers.any(World.class))).thenReturn(standardTasks);

		when(userSkillService.getScoringForRuleFromTeam(anyString(), Matchers.anyListOf(String.class))).thenReturn(1.0);

		//when(standardTaskService.getByWorld(world)).thenReturn(unsortedTasks);

		// When
		List<StandardTaskDTO> tasks = standardTaskService.getByWorld(world);
		// Then
		assertEquals(SonarQubeSeverity.BLOCKER.name(), tasks.get(0).getSeverity());
		assertEquals(SonarQubeSeverity.CRITICAL.name(), tasks.get(1).getSeverity());
		assertEquals(SonarQubeSeverity.MAJOR.name(), tasks.get(2).getSeverity());
		assertEquals(SonarQubeSeverity.MAJOR.name(), tasks.get(3).getSeverity());
	}

	private StandardTask mockStandardTask(String severity) {
		StandardTask task = new StandardTask();
		task.setSeverity(severity);
		return task;
	}

	private StandardTaskDTO mockStandardTaskDTO(String severity) {
		StandardTaskDTO task = new StandardTaskDTO();
		task.setSeverity(severity);
		return task;
	}

}
