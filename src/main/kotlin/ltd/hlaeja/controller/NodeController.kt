package ltd.hlaeja.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import ltd.hlaeja.service.DeviceRegistryService
import ltd.hlaeja.util.Pagination
import ltd.hlaeja.util.Pagination.Companion.DEFAULT_PAGE
import ltd.hlaeja.util.Pagination.Companion.DEFAULT_SIZE
import ltd.hlaeja.util.Pagination.Companion.MAX
import ltd.hlaeja.util.Pagination.Companion.MIN
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import reactor.core.publisher.Mono

@Controller
class NodeController(
    private val deviceRegistryService: DeviceRegistryService,
) {
    @GetMapping(
        "/nodes",
        "/nodes/page-{page}",
        "/nodes/page-{page}/show-{show}",
    )
    fun getNodes(
        @PathVariable(required = false) @Min(MIN) page: Int = DEFAULT_PAGE,
        @PathVariable(required = false) @Min(MIN) @Max(MAX) show: Int = DEFAULT_SIZE,
        model: Model,
    ) = deviceRegistryService.getNodes(page, show)
        .collectList()
        .doOnNext { items ->
            model.addAttribute("items", items)
            model.addAttribute("pagination", Pagination(page, show, items.size, DEFAULT_SIZE))
        }
        .then(Mono.just("node/list"))
}
