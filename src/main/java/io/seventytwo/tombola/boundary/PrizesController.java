package io.seventytwo.tombola.boundary;

import io.seventytwo.tombola.entity.Prize;
import io.seventytwo.tombola.entity.PrizeRepository;
import io.seventytwo.tombola.entity.Tombola;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

@RequestMapping("/prizes")
@Controller
public class PrizesController {

    private final PrizeRepository prizeRepository;
    private final MessageSource messageSource;

    public PrizesController(PrizeRepository prizeRepository, MessageSource messageSource) {
        this.prizeRepository = prizeRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public String findAll(Model model, HttpSession session) {
        var tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            return "redirect:/tombolas";
        } else {
            var tombola = (Tombola) tombolaFromSession;

            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola));
            model.addAttribute("prizeViewModel", new PrizeViewModel());

            return "prizes";
        }
    }

    @GetMapping("/search")
    public String findAll(@RequestParam String searchTerm, Model model, HttpSession session) {
        var tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            return "redirect:/tombolas";
        } else {
            var tombola = (Tombola) tombolaFromSession;
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
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola));
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
        var prizeFromDb = prizeRepository.findById(prize.getId()).get();

        prizeFromDb.setNumber(prize.getNumber());
        prizeFromDb.setName(prize.getName());

        var savedPrize = prizeRepository.saveAndFlush(prizeFromDb);

        model.addAttribute("prize", savedPrize);

        return "prize";
    }

    @PostMapping("/add")
    public String saveFromViewModel(Model model, HttpSession session, Locale locale, PrizeViewModel prizeViewModel) {
        var tombolaFromSession = session.getAttribute("tombola");
        if (tombolaFromSession == null) {
            return "redirect:/tombolas";
        } else {
            Tombola tombola = (Tombola) tombolaFromSession;

            var prize = new Prize();
            prize.setTombola(tombola);
            prize.setNumber(prizeViewModel.getNumber());
            prize.setName(prizeViewModel.getName());

            var optionalPrize = prizeRepository.findByTombolaAndNumber(tombola, prizeViewModel.getNumber());
            if (optionalPrize.isPresent()) {
                var message = messageSource.getMessage("messages.number_exists",
                        new Object[]{optionalPrize.get().getNumber(), optionalPrize.get().getName()},
                        locale);
                model.addAttribute("message", new Message(message, true));
            } else {
                prizeRepository.saveAndFlush(prize);

                // Reset number to null but keep name for faster data entry
                prizeViewModel.setNumber(null);
            }

            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola));
            model.addAttribute("prizeViewModel", prizeViewModel);

            return "prizes";
        }
    }
}
