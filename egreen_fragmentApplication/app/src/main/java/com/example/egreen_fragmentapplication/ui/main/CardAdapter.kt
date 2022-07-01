package com.example.egreen_fragmentapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.card_view.*
import com.example.egreen_fragmentapplication.R
import kotlinx.android.synthetic.main.card_view.view.*
import android.util.Log
import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.egreen_fragmentapplication.GlideApp
import java.util.*

import com.squareup.picasso.Picasso


class CardAdapter(private val context: Context?, private val CardArrayList: ArrayList<CardModel>, viewModel: MainViewModel): PagerAdapter(){
    private val vm = viewModel

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

        view.plantName.text = plantName

        if(plantName != "New Plant"){
            Picasso.with(context).load(image.toUri()).into(view.bannerIv)
        }
        else
            view.bannerIv.setImageResource(R.drawable.genoveffa)

        view.water_level.text = waterlevel
        view.oxigen.text = oxygen

        //handles clicks
        view.setOnClickListener() {
            //Toast.makeText(context, "$plantName", Toast.LENGTH_SHORT).show()
            if(plantName != "New Plant"){
                it.findNavController().navigate(R.id.action_CardAdapter_to_plantFragment)
                //Log.d("Questa è la pianta: ", plantName)
            }
            else{
                vm.resetTmpPlantPath()
                it.findNavController().navigate(R.id.action_CardAdapter_to_addPlantFragment)
            }
        }

        //add view to container
        container.addView(view, position)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}