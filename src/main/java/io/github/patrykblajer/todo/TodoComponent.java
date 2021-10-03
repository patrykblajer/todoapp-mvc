package io.github.patrykblajer.todo;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class TodoComponent {

    private final TodoRepository todoRepository;

    public TodoComponent(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @PostConstruct
    public void addTestTasks() {
        var testTask1 = new Task("Kupić karnet", Category.ENTERTAINMENT, LocalDate.now().plusDays(13), false);
        var testTask2 = new Task("Przygotować śniadanie", Category.HOME, LocalDate.now(), false);
        var testTask3 = new Task("Zrobić notatki", Category.WORK, LocalDate.now().minusDays(5), false);
        todoRepository.save(testTask1);
        todoRepository.save(testTask2);
        todoRepository.save(testTask3);
    }
}