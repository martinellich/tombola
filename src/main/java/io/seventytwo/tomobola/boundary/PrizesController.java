package io.seventytwo.tomobola.boundary;

import io.seventytwo.tomobola.entity.Prize;
import io.seventytwo.tomobola.entity.PrizeRepository;
import io.seventytwo.tomobola.entity.Tombola;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/prizes")
@Controller
public class PrizesController {

    private final PrizeRepository prizeRepository;

    public PrizesController(PrizeRepository prizeRepository) {
        this.prizeRepository = prizeRepository;
    }

    @GetMapping
    public String findAll(Model model, HttpSession session) {
        Object tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            throw new IllegalStateException("No tombola selected!");
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;

            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("prizeViewModel", new PrizeViewModel());
        }

        return "prizes";
    }

    @PostMapping
    public String save(Model model, HttpSession session, PrizeViewModel prizeViewModel) {
        Object tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            throw new IllegalStateException("No tombola selected!");
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;

            Prize prize = new Prize();
            prize.setTombola(tombola);
            prize.setNumber(prizeViewModel.getNumber());
            prize.setName(prizeViewModel.getName());

            prizeRepository.saveAndFlush(prize);

            // Reset number to null but keep name for faster data entry
            prizeViewModel.setNumber(null);

            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("prizeViewModel", prizeViewModel);

            return "prizes";
        }
    }
}
