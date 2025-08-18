package ltd.hlaeja.util

@Suppress("unused")
data class Pagination(
    val page: Int,
    val show: Int,
    val items: Int,
    val defaultSize: Int,
) {

    companion object {
        const val DEFAULT_PAGE: Int = 1
        const val DEFAULT_SIZE: Int = 25
        const val MIN: Long = 1
        const val MAX: Long = 100
        const val STEEP: Int = 1
    }

    val hasMore: Boolean = show == items
    val showSize: Boolean = show != defaultSize
    val first: Boolean = page <= STEEP
    val previous: Int = page - STEEP
    val next: Int = page + STEEP
    val start: Int = previous * show + STEEP
    val end: Int = page * show
    val size: Int = previous * show + items
}
