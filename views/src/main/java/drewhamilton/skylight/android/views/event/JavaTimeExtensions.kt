package drewhamilton.skylight.android.views.event

import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import java.time.Instant
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(26)
fun SkylightEventView.setTime(time: OffsetTime?, @StringRes fallback: Int) =
    setTime(time, fallback = context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(time: OffsetTime?, formatter: DateTimeFormatter, @StringRes fallback: Int) =
    setTime(time, formatter, fallback = context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(
    time: OffsetTime?,
    formatter: DateTimeFormatter,
    timeZone: ZoneOffset,
    @StringRes fallback: Int
) = setTime(time, formatter, timeZone, context.getString(fallback))

@RequiresApi(26)
fun SkylightEventView.setTime(
    time: OffsetTime?,
    formatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
    timeZone: ZoneOffset = ZoneId.systemDefault().rules.getOffset(Instant.now()),
    fallback: String = ""
) {
    if (time == null) {
        timeHint = fallback
        timeText = ""
    } else {
        val timeInZone = time.withOffsetSameInstant(timeZone)
        timeText = formatter.format(timeInZone)
        timeHint = ""
    }
}
