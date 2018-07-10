package io.seventytwo.tomobola.boundary;

import io.seventytwo.tomobola.entity.Prize;
import io.seventytwo.tomobola.entity.PrizeRepository;
import io.seventytwo.tomobola.entity.Tombola;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
            return "redirect:/tombolas";
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;

            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("prizeViewModel", new PrizeViewModel());

            return "prizes";
        }
    }

    @GetMapping("/search")
    public String findAll(@RequestParam String searchTerm, Model model, HttpSession session) {
        Object tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            return "redirect:/tombolas";
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;
            List<Prize> prizes;
            if (StringUtils.isBlank(searchTerm)) {
                prizes = prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola);
            } else {
                if (StringUtils.isNumeric(searchTerm)) {
                    prizes = prizeRepository.findAllByTombolaAndNumberOrderByCreatedDateDesc(tombola, Integer.parseInt(searchTerm));
                } else {
                    prizes = prizeRepository.findAllByTombolaAndNameLikeOrderByCreatedDateDesc(tombola, searchTerm);
                }
            }
            model.addAttribute("prizes", prizes);
            model.addAttribute("prizeViewModel", new PrizeViewModel());

            return "prizes";
        }
    }

    @GetMapping("{id}")
    public String findById(@PathVariable Integer id, Model model) {
        prizeRepository.findById(id).ifPresent(prize -> model.addAttribute("prize", prize));

        return "prize";
    }

    @PostMapping
    public String save(Prize prize, Model model) {
        Prize savedPrize = prizeRepository.saveAndFlush(prize);

        model.addAttribute("prize", savedPrize);

        return "prize";
    }

    @PostMapping("/add")
    public String saveFromViewModel(Model model, HttpSession session, PrizeViewModel prizeViewModel) {
        Object tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            return "redirect:/tombolas";
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;

            Prize prize = new Prize();
            prize.setTombola(tombola);
            prize.setNumber(prizeViewModel.getNumber());
            prize.setName(prizeViewModel.getName());

            try {
                prizeRepository.saveAndFlush(prize);

                // Reset number to null but keep name for faster data entry
                prizeViewModel.setNumber(null);

            } catch (DataIntegrityViolationException e) {
                model.addAttribute("message", new Message(NestedExceptionUtils.getMostSpecificCause(e).getMessage(), true));
            }


            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("prizeViewModel", prizeViewModel);

            return "prizes";
        }
    }
}
