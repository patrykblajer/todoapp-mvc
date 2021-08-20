package io.github.patrykblajer.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class TodoController {
    private final TodoRepo todoRepo;

    @Autowired
    public TodoController(TodoRepo todoRepo) {
        this.todoRepo = todoRepo;
    }

    @GetMapping("/")
    public String index(String description, Category category, Model model) {
        var newTask = new Task(description, category, LocalDate.now());
        var notDoneList = todoRepo.findNotDoneOrderByDateAsc();
        model.addAttribute("newTask", newTask);
        model.addAttribute("notDoneList", notDoneList);

        for (Task task : notDoneList) {
            task.setStatus(updateStatusInIndex(task));
        }
        return "index";
    }

    @PostMapping("/save")
    public String add(Task task) {
        todoRepo.save(task);
        return "added";
    }

    @GetMapping("/archive")
    public String getDoneList(Model model) {
        var doneList = todoRepo.findDoneOrderByDateDesc();
        model.addAttribute("doneList", doneList);
        return "archive";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        todoRepo.deleteById(id);
        return "deleted";
    }

    @PostMapping(value = "/done/{id}")
    public String getTaskDone(@PathVariable Long id) {
        todoRepo.setFinalDate(LocalDate.now(), id);
        return "done";
    }

    private String updateStatusInIndex(Task task) {
        var daysToTask =
                ChronoUnit.DAYS.between(LocalDate.now(), task.getStartDate());
        String status = null;

        if (daysToTask == 0) {
            status = "Już dzisiaj";
        } else if (daysToTask == 1) {
            status = "Jutro";
        } else if (daysToTask > 1 && daysToTask < 7) {
            status = "W tym tygodniu";
        } else if (daysToTask > 7) {
            status = "Przyszły tydzień <chill>";
        } else if (daysToTask < 0) {
            status = "Przeterminowane!";
        }
        return status;
    }
}