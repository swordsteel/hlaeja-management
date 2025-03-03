package ltd.hlaeja.form

data class DevelopTestForm(
    val text: String = "",
    val password: String = "",
    val email: String = "",
    val number: Int = 0,
    val date: String = "",
    val time: String = "",
    val datetimeLocal: String = "",
    val color: String = "#000000",
    val checkbox: Boolean = false,
    val radio: String = "",
    val selectSingle: String = "",
    val selectMultiple: List<String> = emptyList(),
    val textarea: String = "",
    val file: String? = null,
)
