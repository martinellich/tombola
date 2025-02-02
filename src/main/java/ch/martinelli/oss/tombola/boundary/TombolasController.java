package ch.martinelli.oss.tombola.boundary;

import ch.martinelli.oss.tombola.control.TombolaRepository;
import ch.martinelli.oss.tombola.entity.Tombola;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tombolas")
@Controller
public class TombolasController {

    private final TombolaRepository tombolaRepository;
    private final PrizesController prizesController;

    public TombolasController(TombolaRepository tombolaRepository, PrizesController prizesController) {
        this.tombolaRepository = tombolaRepository;
        this.prizesController = prizesController;
    }

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("tombolas", tombolaRepository.findAll());

        return "tombolas";
    }


    @GetMapping("{id}")
    public String findById(@PathVariable Integer id, Model model) {
        tombolaRepository.findById(id).ifPresent(tombola -> model.addAttribute("tombola", tombola));

        return "tombola";
    }

    @GetMapping("{id}/select")
    public String selectById(@PathVariable Integer id, HttpSession session, Model model) {
        tombolaRepository.findById(id).ifPresent(tombola -> session.setAttribute("tombola", tombola));

        return prizesController.search(model, session);
    }

    @GetMapping("/new")
    public String get(Model model) {
        model.addAttribute("tombola", new Tombola());

        return "tombola";
    }


    @PostMapping
    public String save(Model model, Tombola tombola) {
        Tombola savedTombola = tombolaRepository.save(tombola);
        model.addAttribute("tombola", savedTombola);

        return "tombola";
    }
}
