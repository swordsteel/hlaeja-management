package ltd.hlaeja.exception

@Suppress("unused")
open class TypeNameDuplicateException : DeviceRegistryException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(
        message: String,
        cause: Throwable,
    ) : super(message, cause)

    constructor(
        message: String,
        cause: Throwable,
        enableSuppression: Boolean,
        writableStackTrace: Boolean,
    ) : super(message, cause, enableSuppression, writableStackTrace)
}
