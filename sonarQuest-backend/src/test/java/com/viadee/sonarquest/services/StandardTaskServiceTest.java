package com.viadee.sonarquest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.viadee.sonarquest.entities.StandardTask;
import com.viadee.sonarquest.entities.World;
import com.viadee.sonarquest.externalressources.SonarQubeSeverity;
import com.viadee.sonarquest.repositories.StandardTaskRepository;
import com.viadee.sonarquest.rules.SonarQuestTaskStatus;

@ExtendWith(MockitoExtension.class)
public class StandardTaskServiceTest {

    @Mock
    private StandardTaskRepository standardTaskRepository;

    @Mock
    private NamedParameterJdbcTemplate template;

    @InjectMocks
    private StandardTaskService standardTaskService;

    @Test
    public void testUpdateStandardTask() {
        StandardTask task = new StandardTask();
        task.setKey("Color of Magic!");
        task.setStatus(SonarQuestTaskStatus.OPEN);
        when(standardTaskRepository.saveAndFlush(ArgumentMatchers.any(StandardTask.class))).thenReturn(task);

        task = standardTaskService.updateStandardTask(task);

        assertEquals(SonarQuestTaskStatus.OPEN, task.getStatus());

        // case: existing created task -> no external changes
        final StandardTask createdStandardTask = new StandardTask();
        createdStandardTask.setKey("createdStandardTask");
        createdStandardTask.setStatus(SonarQuestTaskStatus.OPEN);

        standardTaskService.updateStandardTask(createdStandardTask);

        assertEquals(SonarQuestTaskStatus.OPEN, createdStandardTask.getStatus());
        // case: existing created task -> no external changes
    }

    @Test
    public void testGetLastState() {
        final StandardTask task = new StandardTask();
        final SonarQuestTaskStatus lastState = standardTaskService.getLastState(task);
        assertEquals(SonarQuestTaskStatus.OPEN, lastState);
    }

	@Test
	public void testFindByWorld() {
		// Given
		final World world = new World();
		final List<StandardTask> unsortedTasks = new ArrayList<>();
		unsortedTasks.add(mockStandardTask("BLOCKER"));
		unsortedTasks.add(mockStandardTask("MAJOR"));
		unsortedTasks.add(mockStandardTask("CRITICAL"));
		unsortedTasks.add(mockStandardTask("MAJOR"));
		when(standardTaskService.findByWorld(world)).thenReturn(unsortedTasks);
		// When
		final List<StandardTask> tasks = standardTaskService.findByWorld(world);
		// Then
		assertEquals(SonarQubeSeverity.BLOCKER.name(), tasks.get(0).getSeverity());
		assertEquals(SonarQubeSeverity.CRITICAL.name(), tasks.get(1).getSeverity());
		assertEquals(SonarQubeSeverity.MAJOR.name(), tasks.get(2).getSeverity());
		assertEquals(SonarQubeSeverity.MAJOR.name(), tasks.get(3).getSeverity());
	}

	private StandardTask mockStandardTask(final String severity) {
		final StandardTask task = new StandardTask();
		task.setSeverity(severity);
		return task;
	}

}
