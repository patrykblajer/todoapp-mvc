package io.github.patrykblajer.todo;

import io.github.patrykblajer.todo.user.authorization.AuthService;
import io.github.patrykblajer.todo.task.TaskDto;
import io.github.patrykblajer.todo.task.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;


@Controller
public class MainController {
    private final TaskService taskService;
    private final AuthService authService;

    public MainController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("newTask", taskService.newTask(authService.getLoggedUserDto().getId()));
        model.addAttribute("notDoneList", taskService.getSortedTasksDto());
        model.addAttribute("doneList", taskService.getDoneTasksDto());
        model.addAttribute("loggedUser", authService.getLoggedUserDto());
        return "index";
    }

    @Transactional
    @PostMapping()
    public String add(@Valid TaskDto taskDto, BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("badDescriptionAlert", "badDescriptionAlert");
            return "redirect:/";
        } else {
            taskService.save(taskDto);
            redirectAttributes.addFlashAttribute("addTaskSuccess", "addTaskSuccess");
        }
        return "redirect:/";
    }

    @GetMapping("/archive")
    public String getDoneList(Model model) {
        model.addAttribute("doneList", taskService.getDoneTasksDto());
        return "archive";
    }

    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.deleteTask(id);
        redirectAttributes.addFlashAttribute("deleteTaskSuccess", "deleteTaskSuccess");
        return "redirect:/";
    }

    @Transactional
    @PostMapping("/done/{id}")
    public String setTaskDone(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        taskService.setTaskDone(id);
        redirectAttributes.addFlashAttribute("doneTaskSuccess", "doneTaskSuccess");
        return "redirect:/";
    }
}