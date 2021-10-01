package io.github.patrykblajer.todo;

import io.github.patrykblajer.todo.authorization.AuthService;
import io.github.patrykblajer.todo.task.TaskDto;
import io.github.patrykblajer.todo.task.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;

@Controller
public class MainController {
    private final TaskService taskService;
    private final AuthService authService;

    public MainController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("newTask", taskService.newTask(authService.getLoggedUserDto().getId()));
        model.addAttribute("notDoneList", taskService.getSortedTasksDto());
        model.addAttribute("doneList", taskService.getDoneTasksDto());
        model.addAttribute("loggedUser", authService.getLoggedUserDto());
        return "index";
    }

    @Transactional
    @PostMapping("/save")
    public String add(TaskDto taskDto, RedirectAttributes redirectAttributes) {
        taskService.save(taskDto);
        redirectAttributes.addFlashAttribute("addTaskSuccess", "addTaskSuccess");
        return "redirect:/";
    }

    @GetMapping("/archive")
    public String getDoneList(Model model) {
        model.addAttribute("doneList", taskService.getDoneTasksDto());
        return "archive";
    }

    @Transactional
    @PostMapping(value = "/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("deleteTaskSuccess", "deleteTaskSuccess");
        return "redirect:/";
    }

    @Transactional
    @PostMapping(value = "/done/{id}")
    public String setTaskDone(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.setTaskDone(id);
        redirectAttributes.addFlashAttribute("doneTaskSuccess", "doneTaskSuccess");
        return "redirect:/";
    }
}