package ltd.hlaeja.dto

@Suppress("unused")
data class Pagination(
    val page: Int,
    val size: Int,
    val items: Int,
    val defaultSize: Int,
) {
    val hasMore: Boolean = size == items
    val showSize: Boolean = size != defaultSize
    val first: Boolean = page <= 1
    val previous: Int = page - 1
    val next: Int = page + 1
    val start: Int = (page - 1) * size + 1
    val end: Int = page * size
}
