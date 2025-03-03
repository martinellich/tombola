package ch.martinelli.oss.tombola.boundary;

import ch.martinelli.oss.tombola.control.TombolaRepository;
import ch.martinelli.oss.tombola.entity.Tombola;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@RequestMapping("/tombolas")
@Controller
public class TombolasController {

	private static final String TOMBOLAS = "tombolas";

	private static final String TOMBOLA = "tombola";

	private static final String MESSAGE = "message";

	private final TombolaRepository tombolaRepository;

	private final PrizesController prizesController;

	private final MessageSource messageSource;

	public TombolasController(TombolaRepository tombolaRepository, PrizesController prizesController,
			MessageSource messageSource) {
		this.tombolaRepository = tombolaRepository;
		this.prizesController = prizesController;
		this.messageSource = messageSource;
	}

	@GetMapping
	public String findAll(Model model) {
		model.addAttribute(TOMBOLAS, tombolaRepository.findAll());

		return TOMBOLAS;
	}

	@GetMapping("{id}")
	public String findById(@PathVariable Integer id, Model model, Locale locale) {
		var optionalTombola = tombolaRepository.findById(id);
		if (optionalTombola.isPresent()) {
			model.addAttribute(TOMBOLA, optionalTombola.get());
			return TOMBOLA;
		}
		else {
			var message = messageSource.getMessage("messages.tombola_does_not_exists", new Object[] { id }, locale);
			model.addAttribute(MESSAGE, new Message(message, true));
			return TOMBOLAS;
		}
	}

	@GetMapping("{id}/select")
	public String selectById(@PathVariable Integer id, HttpSession session, Model model) {
		tombolaRepository.findById(id).ifPresent(tombola -> session.setAttribute(TOMBOLA, tombola));

		return prizesController.search(model, session);
	}

	@GetMapping("/new")
	public String get(Model model) {
		model.addAttribute(TOMBOLA, new Tombola());

		return TOMBOLA;
	}

	@PostMapping
	public String save(Model model, Tombola tombola) {
		var savedTombola = tombolaRepository.save(tombola);
		model.addAttribute(TOMBOLA, savedTombola);

		return TOMBOLA;
	}

}
