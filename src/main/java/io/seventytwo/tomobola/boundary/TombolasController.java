package io.seventytwo.tomobola.boundary;

import io.seventytwo.tomobola.entity.Tombola;
import io.seventytwo.tomobola.entity.TombolaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

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

        return "index";
    }


    @GetMapping("{id}")
    public String findById(@PathVariable Integer id, Model model) {
        Optional<Tombola> optionalTombola = tombolaRepository.findById(id);
        if (optionalTombola.isPresent()) {
            model.addAttribute("tombola", optionalTombola.get());
        } else {
            model.addAttribute("message", new Message("Tombola nicht gefunden!"));
        }

        return "tombola";
    }

    @GetMapping("{id}/select")
    public String selectById(@PathVariable Integer id, HttpSession session, Model model) {
        Optional<Tombola> optionalTombola = tombolaRepository.findById(id);
        if (optionalTombola.isPresent()) {
            session.setAttribute("tombola", optionalTombola.get());
        }

        return prizesController.findAll(model, session);
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
