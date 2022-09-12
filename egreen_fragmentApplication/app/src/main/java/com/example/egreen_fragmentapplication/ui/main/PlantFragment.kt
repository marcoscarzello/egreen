package com.example.egreen_fragmentapplication.ui.main

import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.egreen_fragmentapplication.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.GridLabelRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class PlantFragment : Fragment(R.layout.fragment_plant) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainViewModel by activityViewModels()

        var humMap: MutableMap<String, String> = HashMap()
        var watMap: MutableMap<String, String> = HashMap()


        val plantNameText  = view.findViewById<TextView>(R.id.plant_title)
        val humidity  = view.findViewById<TextView>(R.id.humidity)
        val waterlevel  = view.findViewById<TextView>(R.id.water_level)
        val brightness = view.findViewById<TextView>(R.id.brightness)
        val brightImg = view.findViewById<ImageView>(R.id.bright_img)

        var plantImg = view.findViewById<ImageView>(R.id.plant_image_view)
        viewModel.downPlantPic(this@PlantFragment.requireContext(), plantImg)   //scarico immagine pianta


        //to delete
        val test = view.findViewById<ImageView>(R.id.test_profile)
        test.setOnClickListener{
            findNavController().navigate(R.id.action_plantFragment_to_plantSettingsFragment)
        }

        val HgraphView = view.findViewById<GraphView>(R.id.Hgraph);
        HgraphView.getViewport().setYAxisBoundsManual(true);
        HgraphView.getViewport().setMinY(0.0);
        HgraphView.getViewport().setMaxY(100.0);

        var series = LineGraphSeries(
            arrayOf<DataPoint>(
                // on below line we are adding
                // each point on our x and y axis.
                DataPoint(1.0, 0.0),
            ))
        HgraphView.setTitle("Last humidity values")
        val gridLabel: GridLabelRenderer = HgraphView.getGridLabelRenderer()
        gridLabel.horizontalAxisTitle = ""
        gridLabel.verticalAxisTitle = "%"


        plantNameText.text = viewModel.getSelectedPlantName()

        viewModel.humidityMap.observe(this, Observer { hm ->
            if (hm!= null) {
                humMap = hm
                humidity.text = humMap["a"]
                Log.d("HUMIDITY MAP READ BY FRAGMENT PLANT", humMap.toString())

                //aggiornamento graph
                HgraphView.removeAllSeries()
                series = LineGraphSeries(
                    arrayOf<DataPoint>(
                        DataPoint(1.0, humMap["e"]!!.toDouble()),
                        DataPoint(2.0, humMap["d"]!!.toDouble()),
                        DataPoint(3.0, humMap["c"]!!.toDouble()),
                        DataPoint(4.0, humMap["b"]!!.toDouble()),
                        DataPoint(5.0, humMap["a"]!!.toDouble())
                    )
                )
                series.setColor(Color.WHITE)
                series.setDrawDataPoints(true)
                HgraphView.addSeries(series)
            }

        })

        viewModel.lastLight.observe(this, Observer { ll ->
            if (ll != null && ll!= ""){
                brightness.text = ll    //valore luminosità %  a schermo

                //immagine in funzione della luminosità
                if (ll.toInt() <= 35 ) brightImg.setImageResource(R.drawable.brightness0)
                else if (ll.toInt() < 70 ) brightImg.setImageResource(R.drawable.brightness50)
                else if (ll.toInt() < 100 ) brightImg.setImageResource(R.drawable.brightness100)


            }
        })
        /*
               viewModel.plantList.observe(this, Observer { plantList ->

                       var i : Int = 0
                       for (p: String in plantList) {
                           if (p != null) {

                               waterlevel.text = viewModel.dataWtList!!.value?.get(i)!!.toString()
                           }}})


               viewModel.waterMap.observe(this, Observer { wm ->
                   if(wm!= null) {
                       watMap = wm
                       waterlevel.text = watMap["a"]
                       Log.d("Water MAP READ BY FRAGMENT PLANT", watMap.toString())
                   }

               })

                */
        viewModel.water.observe(this, Observer { w ->
            if(w!= null){
                waterlevel.text = w
            }
        })

        //se faccio indietro da plant fragment andro sempre in garden fragmnet
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(ContentValues.TAG, "Fragment back pressed invoked")
                    // Do custom work here
                    findNavController().navigate(R.id.action_plantFragment_to_mainFragment)

                }
            }
            )
    }

}
