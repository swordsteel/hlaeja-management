package ltd.hlaeja.controller

import ltd.hlaeja.form.DevelopTestForm
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono

@Controller
@RequestMapping("/dev")
class DevelopController {

    @GetMapping("/form")
    fun home(model: Model): Mono<String> = Mono.just("develop/form")
        .also {
            model.addAttribute("formElements", DevelopTestForm())
            model.addAttribute("selectOptions", listOf("Option 1", "Option 2", "Option 3", "Option 4"))
            model.addAttribute("radioOptions", listOf("True", "False", "Maybe"))
        }
}
