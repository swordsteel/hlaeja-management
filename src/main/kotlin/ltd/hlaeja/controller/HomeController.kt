package ltd.hlaeja.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @Suppress("UnusedParameter", "FunctionOnlyReturningConstant")
    @GetMapping("/")
    fun home(model: Model): String = "home/index"
}
