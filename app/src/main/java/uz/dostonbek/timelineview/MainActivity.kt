package uz.dostonbek.timelineview

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.dostonbek.timelineview.databinding.ActivityRecyclerviewDecorationBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRecyclerviewDecorationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewDecorationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {

            timelineRv.let {
                it.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                it.adapter = BaseAdapter((0..10).toList())

                val colorPrimary = TypedValue()
                val theme: Resources.Theme = getTheme()
                theme.resolveAttribute(R.color.purple_200, colorPrimary, true)
                it.addItemDecoration(
                    TimelineDecorator(
                        position = TimelineDecorator.Position.Left,
                        indicatorColor = colorPrimary.data,
                        lineColor = colorPrimary.data
                    )
                )
                it.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        (it.layoutManager as? LinearLayoutManager)?.let {
                            if (it.findFirstCompletelyVisibleItemPosition() == 0)
                                binding.fab.extend()
                            else
                                binding.fab.shrink()
                        }
                    }
                })
            }


            timelineRv.addItemDecoration(
                TimelineDecorator(
                    indicatorSize = 24f,
                    lineWidth = 15f,
                    padding = 48f,
                    position = TimelineDecorator.Position.Left,
                    indicatorColor = Color.RED,
                    lineColor = Color.RED
                )
            )
        }
    }
}

interface TimelineAdapter {
    fun getTimelineViewType(position: Int): TimelineView.ViewType? = null
    fun getIndicatorDrawable(position: Int): Drawable? = null
    @DrawableRes fun getIndicatorDrawableRes(position: Int): Int? = null
    fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? = null
    fun getIndicatorColor(position: Int): Int? = null
    fun getLineColor(position: Int): Int? = null
    fun getLineStyle(position: Int): TimelineView.LineStyle? = null
    fun getLinePadding(position: Int): Float? = null
}