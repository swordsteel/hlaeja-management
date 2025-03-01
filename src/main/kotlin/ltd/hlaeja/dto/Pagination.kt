package ltd.hlaeja.dto

@Suppress("unused")
data class Pagination(
    val page: Int,
    val show: Int,
    val items: Int,
    val defaultSize: Int,
) {
    val hasMore: Boolean = show == items
    val showSize: Boolean = show != defaultSize
    val first: Boolean = page <= 1
    val previous: Int = page - 1
    val next: Int = page + 1
    val start: Int = previous * show + 1
    val end: Int = page * show
    val size: Int = previous * show + items
}
