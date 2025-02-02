package ch.martinelli.oss.tombola.boundary;

import ch.martinelli.oss.tombola.control.PrizeRepository;
import ch.martinelli.oss.tombola.entity.Prize;
import ch.martinelli.oss.tombola.entity.Tombola;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RequestMapping("/prizes")
@Controller
public class PrizesController {

	private static final String PRIZES = "prizes";

	private static final String PRIZE = "prize";

	private static final String REDIRECT_TOMBOLAS = "redirect:/tombolas";

	private static final String TOMBOLA = "tombola";

	private static final String PRIZE_VIEW_MODEL = "prizeViewModel";

	private static final String MESSAGE = "message";

	private static final String TOTAL_NUMBER_OF_PRIZES = "totalNumberOfPrizes";

	private final PrizeRepository prizeRepository;

	private final MessageSource messageSource;

	public PrizesController(PrizeRepository prizeRepository, MessageSource messageSource) {
		this.prizeRepository = prizeRepository;
		this.messageSource = messageSource;
	}

	@GetMapping
	public String search(Model model, HttpSession session) {
		var tombolaFromSession = session.getAttribute(TOMBOLA);
		if (tombolaFromSession == null) {
			return REDIRECT_TOMBOLAS;
		}
		else {
			var tombola = (Tombola) tombolaFromSession;

			model.addAttribute(PRIZES, prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
			model.addAttribute(TOTAL_NUMBER_OF_PRIZES, prizeRepository.countByTombola(tombola));
			model.addAttribute(PRIZE_VIEW_MODEL, new PrizeViewModel());

			return PRIZES;
		}
	}

	@GetMapping("/search")
	public String search(@RequestParam String searchTerm, Model model, HttpSession session) {
		var tombolaFromSession = session.getAttribute(TOMBOLA);
		if (tombolaFromSession == null) {
			return REDIRECT_TOMBOLAS;
		}
		else {
			var tombola = (Tombola) tombolaFromSession;
			List<Prize> prizes;
			if (StringUtils.isBlank(searchTerm)) {
				prizes = prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola);
			}
			else {
				if (StringUtils.isNumeric(searchTerm)) {
					prizes = prizeRepository.findAllByTombolaAndNumberOrderByCreatedDateDesc(tombola,
							Integer.parseInt(searchTerm));
				}
				else {
					prizes = prizeRepository.findAllByTombolaAndNameContainsIgnoreCaseOrderByCreatedDateDesc(tombola,
							searchTerm);
				}
			}
			model.addAttribute(PRIZES, prizes);
			model.addAttribute(TOTAL_NUMBER_OF_PRIZES, prizeRepository.countByTombola(tombola));
			model.addAttribute(PRIZE_VIEW_MODEL, new PrizeViewModel());

			return PRIZES;
		}
	}

	@GetMapping("{id}")
	public String findById(@PathVariable Integer id, Model model) {
		prizeRepository.findById(id).ifPresent(prize -> model.addAttribute(PRIZE, prize));

		return PRIZE;
	}

	@PostMapping
	public String save(Prize prize, Model model, Locale locale) {
		var optionalPrize = prizeRepository.findById(prize.getId());
		if (optionalPrize.isPresent()) {
			var prizeFromDb = optionalPrize.get();

			prizeFromDb.setNumber(prize.getNumber());
			prizeFromDb.setName(prize.getName());

			var savedPrize = prizeRepository.saveAndFlush(prizeFromDb);

			model.addAttribute(PRIZE, savedPrize);
		}
		else {
			var message = messageSource.getMessage("messages.prize_does_not_exists", new Object[] { prize.getNumber() },
					locale);
			model.addAttribute(MESSAGE, new Message(message, true));
		}
		return PRIZE;
	}

	@PostMapping("/add")
	public String saveFromViewModel(Model model, HttpSession session, Locale locale, PrizeViewModel prizeViewModel) {
		var tombolaFromSession = session.getAttribute(TOMBOLA);
		if (tombolaFromSession == null) {
			return REDIRECT_TOMBOLAS;
		}
		else {
			var tombola = (Tombola) tombolaFromSession;

			var prize = new Prize();
			prize.setTombola(tombola);
			prize.setNumber(prizeViewModel.getNumber());
			prize.setName(prizeViewModel.getName());

			var optionalPrize = prizeRepository.findByTombolaAndNumber(tombola, prizeViewModel.getNumber());
			if (optionalPrize.isPresent()) {
				var message = messageSource.getMessage("messages.number_exists",
						new Object[] { optionalPrize.get().getNumber(), optionalPrize.get().getName() }, locale);
				model.addAttribute(MESSAGE, new Message(message, true));
			}
			else {
				prizeRepository.saveAndFlush(prize);

				// Reset number to null but keep name for faster data entry
				prizeViewModel.setNumber(null);
			}

			model.addAttribute(PRIZES, prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola));
			model.addAttribute(TOTAL_NUMBER_OF_PRIZES, prizeRepository.countByTombola(tombola));
			model.addAttribute(PRIZE_VIEW_MODEL, prizeViewModel);

			return PRIZES;
		}
	}

}
