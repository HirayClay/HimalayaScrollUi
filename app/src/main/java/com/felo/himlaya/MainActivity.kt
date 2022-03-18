package com.felo.himlaya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi()
    }

    private fun initUi() {
        val simpleAdapter = SimpleAdapter(20)
        recyclerView.adapter = simpleAdapter
        recyclerView2.adapter = SimpleAdapter(20)
        val lp = bottom_container.layoutParams as CoordinatorLayout.LayoutParams
        (lp.behavior as BottomSheetBehavior).setPeekHeight(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                resources.displayMetrics
            ).toInt(), false
        )
    }
}