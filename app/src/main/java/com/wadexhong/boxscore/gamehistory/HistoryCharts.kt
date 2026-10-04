package com.wadexhong.boxscore.gamehistory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.wadexhong.boxscore.R
import com.wadexhong.boxscore.objects.PercentFormatter

/**
 * Shooting percentage ring from legacy HistoryTeamDataAdapter.GraphViewHolder.
 * [centerTextRes] takes the formatted percentage, e.g. R.string.fieldGoalInPie.
 */
@Composable
fun PercentagePieChart(percent: Float, centerTextRes: Int, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                setDrawEntryLabels(false)
                legend.isEnabled = false
                description.isEnabled = false
                isRotationEnabled = false
                setHoleColor(ResourcesCompat.getColor(resources, R.color.tranparent, null))
                setCenterTextColor(ResourcesCompat.getColor(resources, R.color.colorButtonInMain, null))
            }
        },
        update = { chart ->
            val res = chart.resources
            // The max angle must be set before the data, or the old limit can show.
            chart.maxAngle = 360f * percent / 100
            val set = PieDataSet(listOf(PieEntry(percent, "")), "").apply {
                color = ResourcesCompat.getColor(res, R.color.colorOrange, null)
            }
            chart.data = PieData(set).apply {
                setValueTextColor(ResourcesCompat.getColor(res, R.color.colorButtonInMain, null))
                setValueFormatter(PercentFormatter())
                setDrawValues(false)
            }
            chart.centerText = res.getString(centerTextRes, String.format("%.1f", percent)) + " %"
            chart.spin(1500, -90f, 270f, Easing.EasingOption.EaseInOutQuad)
            chart.animateX(1500)
        },
        modifier = modifier
    )
}

/** Points by free throws / twos / threes for each quarter plus the total (legacy bar chart). */
@Composable
fun ScoringBarChart(stacks: List<Triple<Int, Int, Int>>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                val textColor = ResourcesCompat.getColor(resources, R.color.colorButtonInMain, null)
                xAxis.setDrawGridLines(false)
                xAxis.textSize = 10f
                xAxis.granularity = 1f
                xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf("第一節", "第二節", "第三節", "第四節", "總和"))
                xAxis.textColor = textColor
                axisLeft.axisMinimum = 0f
                axisLeft.granularity = 1f
                axisLeft.setDrawGridLines(false)
                axisLeft.setDrawZeroLine(true)
                axisLeft.textColor = textColor
                axisRight.setDrawGridLines(false)
                axisRight.setDrawLabels(false)
                description.isEnabled = false
                legend.textColor = textColor
                legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                isDragEnabled = false
                setScaleEnabled(false)
                setTouchEnabled(false)
            }
        },
        update = { chart ->
            val entries = stacks.mapIndexed { index, (ft, two, three) ->
                BarEntry(index.toFloat(), floatArrayOf(ft.toFloat(), two.toFloat(), three.toFloat()))
            }
            val set = BarDataSet(entries, "").apply {
                stackLabels = arrayOf("罰球", "二分球", "三分球")
                setColors(intArrayOf(R.color.colorPrimary, R.color.secondaryLightColor, R.color.colorAccent), chart.context)
            }
            chart.data = BarData(set).apply {
                setValueTextSize(10f)
                setValueTextColor(ResourcesCompat.getColor(chart.resources, R.color.colorButtonInMain, null))
                barWidth = 0.5f
                setValueFormatter(DefaultValueFormatter(0))
            }
            chart.animateY(2000, Easing.EasingOption.EaseInOutQuad)
        },
        modifier = modifier
    )
}
