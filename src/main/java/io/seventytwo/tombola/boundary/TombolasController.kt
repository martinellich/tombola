package io.seventytwo.tombola.boundary

import io.seventytwo.tombola.control.TombolaRepository
import io.seventytwo.tombola.entity.Tombola
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/tombolas")
class TombolasController(private val tombolaRepository: TombolaRepository, private val prizesController: PrizesController) {

    @GetMapping
    fun findAll(model: Model): String {
        model.addAttribute("tombolas", tombolaRepository.findAll())
        return "tombolas"
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Int, model: Model): String {
        tombolaRepository.findById(id).ifPresent { tombola: Tombola? -> model.addAttribute("tombola", tombola) }
        return "tombola"
    }

    @GetMapping("{id}/select")
    fun selectById(@PathVariable id: Int, session: HttpSession, model: Model): String? {
        tombolaRepository.findById(id).ifPresent { tombola: Tombola? -> session.setAttribute("tombola", tombola) }
        return prizesController.search(model, session)
    }

    @GetMapping("/new")
    operator fun get(model: Model): String {
        model.addAttribute("tombola", Tombola())
        return "tombola"
    }

    @PostMapping
    fun save(model: Model, tombola: Tombola): String {
        val savedTombola = tombolaRepository.save(tombola)
        model.addAttribute("tombola", savedTombola)
        return "tombola"
    }
}
