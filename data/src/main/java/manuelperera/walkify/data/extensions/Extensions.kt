package manuelperera.walkify.data.extensions

import java.util.*

inline fun <reified T : Enum<T>> String.toEnum(): T = enumValueOf(this.toUpperCase(Locale.getDefault()))