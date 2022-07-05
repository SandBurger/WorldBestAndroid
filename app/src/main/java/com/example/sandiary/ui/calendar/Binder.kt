import android.renderscript.ScriptGroup
import android.view.View
import androidx.core.content.ContextCompat
import com.example.sandiary.R
import com.example.sandiary.databinding.ItemCalendarDayBinding
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate

class CalendarDayBinder(private val calendarView : CalendarView) : DayBinder<CalendarDayBinder.DayContainer> {
    private var calendar: Pair<LocalDate?, LocalDate?> = null to null
    var input: Input? = null

    fun updateCalendar(calendar: Pair<LocalDate?, LocalDate?>, ) {
        if (this.calendar == calendar) return
        this.calendar = calendar
        this.calendarView.notifyCalendarChanged()
    }

    override fun create(view: View): DayContainer {
        val binding = ItemCalendarDayBinding.bind(view)
        return DayContainer(binding)
    }

    override fun bind(container: DayContainer, day: CalendarDay) {
        val (startDate, endDate) = this.calendar

        container.binding.itemCalendarDayTv.text = day.date.dayOfMonth.toString()

        container.binding.root.setOnClickListener { _ ->
            input?.onDayClick(day.date)
        }
        if (day.owner != DayOwner.THIS_MONTH) {
            container.binding.itemCalendarDayTv.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.purple_200
                )
            )
        } else {
            container.binding.itemCalendarDayTv.setTextColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.purple_200
                )
            )
        }
        if (isInRange(day.date)) {
            container.binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    calendarView.context,
                    R.color.purple_200
                )
            )
        }
        if (startDate == day.date) {
            container.binding.root.background = (ContextCompat.getDrawable(
                calendarView.context,
                R.drawable.ic_dashboard_black_24dp
            ))
        } else if (endDate == day.date) {
            container.binding.root.background =
                (ContextCompat.getDrawable(calendarView.context, R.drawable.ic_calendar_active))
        }
    }

    private fun isInRange(date: LocalDate): Boolean {
        val (startDate, endDate) = this.calendar
        return startDate == date ||
                endDate == date ||
                (startDate != null && endDate != null && startDate < date && date < endDate)

    }

    class DayContainer(val binding: ItemCalendarDayBinding) : ViewContainer(binding.root)

    abstract class Input {
        abstract fun onDayClick(date:LocalDate)
    }
}