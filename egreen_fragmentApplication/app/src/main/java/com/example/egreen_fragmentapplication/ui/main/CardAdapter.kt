package com.example.egreen_fragmentapplication.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.card_view.*
import com.example.egreen_fragmentapplication.R
import kotlinx.android.synthetic.main.card_view.view.*

class CardAdapter(private val context: Context?, private val CardArrayList: ArrayList<CardModel>): PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return CardArrayList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view,container,false)

        //prende i dati
        val model = CardArrayList[position]
        val plantName = model.plantName
        val image = model.image
        val waterlevel = model.waterlevel
        val oxygen = model.oxygen

        //set data to ui views
        view.bannerIv.setImageResource(image)
        view.plantName.text = plantName

        //handles clicks
        view.setOnClickListener {
            Toast.makeText(context, "$plantName", Toast.LENGTH_SHORT).show()
        }

        //add view to container
        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}