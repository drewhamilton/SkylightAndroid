package drewhamilton.skylight.android.views.event

import androidx.annotation.StringRes
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

fun SkylightEventView.setTime(time: ZonedDateTime?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

fun SkylightEventView.setTime(time: ZonedDateTime?, formatter: DateTimeFormatter, @StringRes fallback: Int) =
    setTime(time, formatter, fallback = context.getString(fallback))

fun SkylightEventView.setTime(
    time: ZonedDateTime?,
    formatter: DateTimeFormatter,
    timeZone: ZoneId,
    @StringRes fallback: Int
) = setTime(time, formatter, timeZone, context.getString(fallback))

fun SkylightEventView.setTime(
    time: ZonedDateTime?,
    formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
    timeZone: ZoneId = ZoneId.systemDefault(),
    fallback: String = ""
) {
    if (time == null) {
        timeHint = fallback
        timeText = ""
    } else {
        val timeInZone = time.withZoneSameInstant(timeZone)
        timeText = formatter.format(timeInZone)
        timeHint = ""
    }
}
