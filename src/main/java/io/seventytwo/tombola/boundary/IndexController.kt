package io.seventytwo.tombola.boundary

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class IndexController {

    @GetMapping
    fun get(): String {
        return "redirect:/tombolas"
    }
}
