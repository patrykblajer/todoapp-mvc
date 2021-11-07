package io.github.patrykblajer.todo;

import io.github.patrykblajer.todo.task.TaskDto;
import io.github.patrykblajer.todo.task.TaskService;
import io.github.patrykblajer.todo.task.TaskToEditDto;
import io.github.patrykblajer.todo.user.authorization.AuthService;
import io.github.patrykblajer.todo.weatherwidget.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
public class MainController {
    private final TaskService taskService;
    private final AuthService authService;
    private final WeatherService weatherService;

    public MainController(TaskService taskService, AuthService authService, WeatherService weatherService) {
        this.taskService = taskService;
        this.authService = authService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String index(Model model) {
        try {
            model.addAttribute("weather", weatherService.getWeatherForCity(authService.getLoggedUser().getCity()));
            model.addAttribute("weatherWidget", true);
        } catch (HttpClientErrorException e) {
            model.addAttribute("weatherWidget", false);
        }
        model.addAttribute("newTask", taskService.newTask(authService.getLoggedUserDto().getId()));
        model.addAttribute("notDoneList", taskService.getSortedTasksDto());
        model.addAttribute("doneList", taskService.getDoneTasksDto());
        model.addAttribute("loggedUser", authService.getLoggedUserDto());
        return "index";
    }

    @Transactional
    @PostMapping
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

    @GetMapping("/edit/{id}")
    public String getTaskEditor(@PathVariable Long id, Model model) {
        var taskToEdit = taskService.getTaskToEdit(id);
        model.addAttribute("taskToEdit", taskToEdit);
        return "edit";
    }

    @PostMapping("/edit")
    public String editTask(@Valid TaskToEditDto taskToEditDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("badDescriptionAlert", "badDescriptionAlert");
            return "redirect:/edit/" + taskToEditDto.getId();
        } else {
            taskService.editTask(taskToEditDto);
            redirectAttributes.addFlashAttribute("editTaskSuccess", "editTaskSuccess");
        }
        return "redirect:/";
    }
}