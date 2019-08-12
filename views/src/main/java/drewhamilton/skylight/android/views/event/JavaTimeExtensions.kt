package drewhamilton.skylight.android.views.event

import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(26)
fun SkylightEventView.setTime(time: ZonedDateTime?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(time: ZonedDateTime?, formatter: DateTimeFormatter, @StringRes fallback: Int) =
    setTime(time, formatter, context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(
    time: ZonedDateTime?,
    formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
    fallback: String = ""
) {
    if (time == null) {
        timeHint = fallback
        timeText = ""
    } else {
        timeText = formatter.format(time)
        timeHint = ""
    }
}
