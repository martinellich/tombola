package io.seventytwo.tomobola.boundary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping
    public String get(Model model) {
        model.addAttribute("hello", "hello");

        return "index";
    }
}
