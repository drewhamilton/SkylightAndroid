package drewhamilton.skylight.android.views.event

import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(26)
fun SkylightEventView.setTime(time: ZonedDateTime?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(time: ZonedDateTime?, formatter: DateTimeFormatter, @StringRes fallback: Int) =
    setTime(time, formatter, fallback = context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(
    time: ZonedDateTime?,
    formatter: DateTimeFormatter,
    timeZone: ZoneId,
    @StringRes fallback: Int
) = setTime(time, formatter, timeZone, context.getString(fallback))

@RequiresApi(26)
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
