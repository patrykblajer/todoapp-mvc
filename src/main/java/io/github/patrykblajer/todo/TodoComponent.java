package io.github.patrykblajer.todo;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;

@Component
public class TodoComponent {

    private final TodoRepo todoRepo;

    public TodoComponent(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @PostConstruct
    public void addTestTasks() {
        var testTask1 = new Task("Kupić karnet", Category.ENTERTAINMENT, LocalDate.now().plusDays(13));
        var testTask2 = new Task("Przygotować śniadanie", Category.HOME, LocalDate.now());
        var testTask3 = new Task("Zrobić notatki", Category.WORK, LocalDate.now().minusDays(5));
        todoRepo.save(testTask1);
        todoRepo.save(testTask2);
        todoRepo.save(testTask3);
    }
}