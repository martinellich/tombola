package io.seventytwo.tombola.boundary

import io.seventytwo.tombola.control.PrizeRepository
import io.seventytwo.tombola.entity.Prize
import io.seventytwo.tombola.entity.Tombola
import org.apache.commons.lang3.StringUtils
import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpSession

@Controller
@RequestMapping("/prizes")
class PrizesController(private val prizeRepository: PrizeRepository, private val messageSource: MessageSource) {

    @GetMapping
    fun search(model: Model, session: HttpSession): String {
        val tombolaFromSession = session.getAttribute("tombola")
        return if (tombolaFromSession == null) {
            "redirect:/tombolas"
        } else {
            val tombola = tombolaFromSession as Tombola
            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola))
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola))
            model.addAttribute("prizeViewModel", PrizeViewModel())
            "prizes"
        }
    }

    @GetMapping("/search")
    fun search(@RequestParam searchTerm: String, model: Model, session: HttpSession): String {
        val tombolaFromSession = session.getAttribute("tombola")
        return if (tombolaFromSession == null) {
            "redirect:/tombolas"
        } else {
            val tombola = tombolaFromSession as Tombola
            val prizes: List<Prize?>?
            prizes = if (StringUtils.isBlank(searchTerm)) {
                prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola)
            } else {
                if (StringUtils.isNumeric(searchTerm)) {
                    prizeRepository.findAllByTombolaAndNumberOrderByCreatedDateDesc(tombola, searchTerm.toInt())
                } else {
                    prizeRepository.findAllByTombolaAndNameContainsIgnoreCaseOrderByCreatedDateDesc(tombola, searchTerm)
                }
            }
            model.addAttribute("prizes", prizes)
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola))
            model.addAttribute("prizeViewModel", PrizeViewModel())
            "prizes"
        }
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Int, model: Model): String {
        prizeRepository.findById(id).ifPresent { prize: Prize? -> model.addAttribute("prize", prize) }
        return "prize"
    }

    @PostMapping
    fun save(prize: Prize, model: Model, locale: Locale): String {
        val optionalPrize = prizeRepository.findById(prize.id!!)
        if (optionalPrize.isPresent) {
            val prizeFromDb = optionalPrize.get()
            prizeFromDb.number = prize.number
            prizeFromDb.name = prize.name
            val savedPrize = prizeRepository.saveAndFlush(prizeFromDb)
            model.addAttribute("prize", savedPrize)
        } else {
            val message = messageSource.getMessage("messages.prize_does_not_exists", arrayOf<Any?>(prize.number), locale)
            model.addAttribute("message", Message(message, true))
        }
        return "prize"
    }

    @PostMapping("/add")
    fun saveFromViewModel(model: Model, session: HttpSession, locale: Locale, prizeViewModel: PrizeViewModel): String {
        val tombolaFromSession = session.getAttribute("tombola")
        return if (tombolaFromSession == null) {
            "redirect:/tombolas"
        } else {
            val tombola = tombolaFromSession as Tombola
            val prize = Prize()
            prize.tombola = tombola
            prize.number = prizeViewModel.number
            prize.name = prizeViewModel.name
            val optionalPrize = prizeRepository.findByTombolaAndNumber(tombola, prizeViewModel.number)
            if (optionalPrize!!.isPresent) {
                val message = messageSource.getMessage("messages.number_exists", arrayOf<Any?>(optionalPrize.get().number, optionalPrize.get().name), locale)
                model.addAttribute("message", Message(message, true))
            } else {
                prizeRepository.saveAndFlush(prize)

                // Reset number to null but keep name for faster data entry
                prizeViewModel.number = null
            }
            model.addAttribute("prizes", prizeRepository.findAllByTombolaOrderByCreatedDateDesc(tombola))
            model.addAttribute("totalNumberOfPrizes", prizeRepository.countByTombola(tombola))
            model.addAttribute("prizeViewModel", prizeViewModel)
            "prizes"
        }
    }
}
