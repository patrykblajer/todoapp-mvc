package io.github.patrykblajer.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class TodoController {
    private final TodoRepository todoRepository;

    @Autowired
    public TodoController(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @GetMapping("/")
    public String index(String description, Category category, Model model) {
        var newTask = new Task(description, category, LocalDate.now(), false);
        Sort byStartDateAsc = Sort.by(Sort.Direction.ASC, "startDate");
        var notDoneList = todoRepository.getSortedTasks(false, byStartDateAsc);

        model.addAttribute("newTask", newTask);
        model.addAttribute("notDoneList", notDoneList);

        for (Task task : notDoneList) {
            task.setStatus(updateStatusInIndex(task));
        }
        return "index";
    }

    @PostMapping("/save")
    public String add(Task task) {
        todoRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/archive")
    public String getDoneList(Model model) {
        Sort byFinalDateDesc = Sort.by(Sort.Direction.DESC, "finalDate");
        var doneList = todoRepository.getSortedTasks(true, byFinalDateDesc);
        model.addAttribute("doneList", doneList);
        return "archive";
    }

    @PostMapping(value = "/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping(value = "/done/{id}")
    public String getTaskDone(@PathVariable Long id) {
        var taskToDone = todoRepository.getById(id);
        taskToDone.setDone(true);
        taskToDone.setFinalDate(LocalDate.now());
        todoRepository.save(taskToDone);
        return "redirect:/";
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