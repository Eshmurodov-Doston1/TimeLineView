package uz.dostonbek.timelineview

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

class TimelineDecorator(
    val indicatorStyle: TimelineView.IndicatorStyle = TimelineView.IndicatorStyle.Filled,
    val indicatorDrawable: Drawable? = null,
    @DrawableRes val indicatorDrawableRes: Int? = null,
    val indicatorSize: Float = 12.toPx().toFloat(),
    val indicatorYPosition: Float = 0.5f,
    val checkedIndicatorSize: Float? = null,
    val checkedIndicatorStrokeWidth: Float = 4.toPx().toFloat(),
    val lineStyle: TimelineView.LineStyle? = null,
    val linePadding: Float? = null,
    val lineDashLength: Float? = null,
    val lineDashGap: Float? = null,
    val lineWidth: Float? = null,
    val padding: Float = 16.toPx().toFloat(),
    val position: Position = Position.Left,
    @ColorInt val indicatorColor: Int? = null,
    @ColorInt val lineColor: Int? = null
) : RecyclerView.ItemDecoration() {

    val width = ((indicatorSize * 2) + (padding * 2))

    enum class Position {
        Left,
        Right
    }

    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        s: RecyclerView.State
    ) {
        val size = when(indicatorStyle){
            TimelineView.IndicatorStyle.Filled -> width
            TimelineView.IndicatorStyle.Empty -> width + checkedIndicatorStrokeWidth
            TimelineView.IndicatorStyle.Checked -> width + checkedIndicatorStrokeWidth
        }.toInt()

        when (position) {
            Position.Left ->
                rect.left = size
            Position.Right ->
                rect.right = size
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        c.clipRect(
            parent.paddingLeft,
            parent.paddingTop,
            parent.width,
            parent.height - parent.paddingBottom
        )

        (parent.adapter as? TimelineAdapter).let { adapter ->
            parent.children.forEach {
                val itemPosition = parent.getChildAdapterPosition(it)

                if (itemPosition == RecyclerView.NO_POSITION)
                    return

                val timelineView = TimelineView(context = parent.context)

                adapter?.getTimelineViewType(itemPosition)?.let {
                    timelineView.viewType = it
                } ?: timelineView.setType(itemPosition, parent.adapter?.itemCount ?: -1)

                (adapter?.getIndicatorDrawable(itemPosition) ?: indicatorDrawable)?.let {
                    timelineView.indicatorDrawable = it
                } ?: (adapter?.getIndicatorDrawableRes(itemPosition) ?: indicatorDrawableRes)?.let {
                    timelineView.indicatorDrawable = ContextCompat.getDrawable(parent.context, it)
                }

                (adapter?.getIndicatorColor(itemPosition) ?: indicatorColor)?.let {
                    timelineView.indicatorColor = it
                }

                (adapter?.getLineColor(itemPosition) ?: lineColor)?.let {
                    timelineView.lineColor = it
                }

                (adapter?.getIndicatorStyle(itemPosition) ?: indicatorStyle)?.let {
                    timelineView.indicatorStyle = it
                }

                (adapter?.getLineStyle(itemPosition) ?: lineStyle)?.let {
                    timelineView.lineStyle = it
                }

                (adapter?.getLinePadding(itemPosition) ?: linePadding)?.let {
                    timelineView.linePadding = it
                }

                timelineView.indicatorSize = indicatorSize

                timelineView.indicatorYPosition = indicatorYPosition

                checkedIndicatorSize?.let {
                    timelineView.checkedIndicatorSize = it
                }

                checkedIndicatorStrokeWidth.let {
                    timelineView.checkedIndicatorStrokeWidth = it
                }

                lineDashLength?.let {
                    timelineView.lineDashLength = it
                }

                lineDashGap?.let {
                    timelineView.lineDashGap = it
                }

                lineWidth?.let {
                    timelineView.lineWidth = it
                }

                timelineView.measure(
                    View.MeasureSpec.getSize(width.toInt()),
                    View.MeasureSpec.getSize(it.measuredHeight)
                )

                c.save()
                when (position) {
                    Position.Left -> {
                        c.translate(padding + parent.paddingLeft, it.top.toFloat())
                        timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                    }
                    Position.Right -> {
                        c.translate(
                            it.measuredWidth + padding + parent.paddingLeft,
                            it.top.toFloat()
                        )
                        timelineView.layout(0, 0, timelineView.measuredWidth, it.measuredHeight)
                    }
                }
                timelineView.draw(c)
                c.restore()
            }
        }

        c.restore()
    }
}