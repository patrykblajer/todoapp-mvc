package io.github.patrykblajer.todo.task;

import io.github.patrykblajer.todo.user.authorization.AuthService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final AuthService authService;

    public TaskService(TaskRepository taskRepository, AuthService authService) {
        this.taskRepository = taskRepository;
        this.authService = authService;
    }

    public List<TaskDto> getSortedTasksDto() {
        Sort byStartDateAsc = Sort.by(Sort.Direction.ASC, "startDate");
        return taskRepository.getSortedTasks(false, authService.getLoggedUser().getId(), byStartDateAsc).stream()
                .map(task -> TaskDto.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .category(task.getCategory())
                        .startDate(task.getStartDate())
                        .status(updateStatusInIndex(task))
                        .build())
                .collect(Collectors.toList());
    }

    public List<TaskDto> getDoneTasksDto() {
        Sort byFinalDateDesc = Sort.by(Sort.Direction.DESC, "finalDate");
        return taskRepository.getSortedTasks(true, authService.getLoggedUser().getId(), byFinalDateDesc).stream()
                .map(task -> TaskDto.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .category(task.getCategory())
                        .startDate(task.getStartDate())
                        .finalDate(task.getFinalDate())
                        .build())
                .collect(Collectors.toList());
    }

    public TaskDto newTask(Long userId) {
        return TaskDto.builder()
                .userId(userId)
                .startDate(LocalDate.now())
                .done(false)
                .build();
    }

    public void save(TaskDto taskDto) {
        taskRepository.save(Task.builder()
                .id(taskDto.getId())
                .user(authService.getLoggedUser())
                .description(taskDto.getDescription())
                .category(taskDto.getCategory())
                .startDate(taskDto.getStartDate())
                .build());
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public void setTaskDone(Long id) {
        var task = taskRepository.getById(id);
        task.setDone(true);
        task.setFinalDate(LocalDate.now());
    }

    private String updateStatusInIndex(Task task) {
        var daysToTask =
                ChronoUnit.DAYS.between(LocalDate.now(), task.getStartDate());
        String status;
        if (daysToTask == 0) {
            status = "today";
        } else if (daysToTask == 1) {
            status = "tomorrow";
        } else if (daysToTask > 1 && daysToTask < 7) {
            status = "thisWeek";
        } else if (daysToTask >= 7) {
            status = "nextWeek";
        } else {
            status = "expired";
        }
        return status;
    }
}