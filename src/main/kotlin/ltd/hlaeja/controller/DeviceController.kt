package ltd.hlaeja.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import ltd.hlaeja.util.Pagination
import ltd.hlaeja.service.DeviceRegistryService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import reactor.core.publisher.Mono

@Controller
class DeviceController(
    private val deviceRegistryService: DeviceRegistryService,
) {
    companion object {
        const val DEFAULT_PAGE: Int = 1
        const val DEFAULT_SIZE: Int = 25
        const val MIN: Long = 1
        const val MAX: Long = 100
    }

    @GetMapping(
        "/device",
        "/device/page-{page}",
        "/device/page-{page}/show-{show}",
    )
    fun getDevice(
        @PathVariable(required = false) @Min(MIN) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(MIN) @Max(MAX) show: Int = DEFAULT_SIZE,
        model: Model,
    ) = deviceRegistryService.getDevices(page, show)
        .collectList()
        .doOnNext { items ->
            model.addAttribute("items", items)
            model.addAttribute("pagination", Pagination(page, show, items.size, DEFAULT_SIZE))
        }
        .then(Mono.just("device/list"))
}
