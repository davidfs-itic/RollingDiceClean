package cat.dfdocencia.rollingdiceclean

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cat.dfdocencia.rollingdiceclean.Model.Estadistica
import cat.dfdocencia.rollingdiceclean.Model.EstadisticaProvider
import cat.dfdocencia.rollingdiceclean.Model.EstadisticaViewModel
import cat.dfdocencia.rollingdiceclean.databinding.ActivityStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter

class StatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatsBinding
    private val vmodel: EstadisticaViewModel by viewModels { EstadisticaViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityStatsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.statstoolbar)
        supportActionBar?.title =MainApp.nomAplicacio
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.btnResetStats.setOnClickListener(this::resetStats)
        vmodel.resultEstadistica.observe(this,this::OnEstaditicaActualitzada)
        vmodel.saved.observe(this,this::OnEstadisticaGuardada)
    }

    private fun OnEstadisticaGuardada(success: Boolean) {
        if (success){
            Toast.makeText(this,"Estadistica Guardada Correctament",Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this,"Estadistica Guardada Correctament",Toast.LENGTH_LONG).show()

        }
    }

    private fun OnEstaditicaActualitzada(result: Result<Estadistica>) {
        //Actualitza la UI quan canvii l'estadística
        val dades = result.getOrDefault(Estadistica())
        UpdateBarGraph(dades)
        UpdatePieGraph(dades)
        if (result.isFailure) {
            Toast.makeText(
                this,
                result.exceptionOrNull()?.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun resetStats(view: View?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_estadistica, menu);
        return true;
    }


    private fun UpdatePieGraph(estadistica:Estadistica) {
        val entries = listOf(
            PieEntry(estadistica.numdobles.toFloat() , "Dobles"), // Valor 70%
            PieEntry((estadistica.tirades-estadistica.numdobles).toFloat(), "No Dobles")  // Valor 30%
        )
        val pieDataSet = PieDataSet(entries, "Distribució")
        pieDataSet.colors = listOf(Color.rgb(135, 206, 250), Color.rgb(255, 182, 193)) // Blau clar i rosa clar
        pieDataSet.valueTextColor = Color.BLACK // Color del text
        pieDataSet.valueTextSize = 16f // Mida del text

        // Afegeix el DataSet al PieData
        binding.piechartDobles.apply {
            data= PieData(pieDataSet)
            description.isEnabled = false // Desactiva la descripció
            isDrawHoleEnabled = true // Mostra el forat al centre
            holeRadius = 40f // Mida del forat
            setHoleColor(Color.WHITE) // Color del forat
            animateY(1000) // Animació en Y
            setEntryLabelColor(Color.BLACK) // Color de les etiquetes
            setEntryLabelTextSize(14f) // Mida del text de les etiquetes

            // Actualitza el gràfic
            invalidate()
        }
    }


    private fun UpdateBarGraph(estadistica:Estadistica){
        val entries = ArrayList<BarEntry>()
        for (i in estadistica.daus.indices) {
            entries.add(BarEntry((i+1).toFloat(), estadistica.daus[i].toFloat()))
        }

        val barDataSet = BarDataSet(entries, "Resultats Daus")

        val pastelColors = listOf(
            Color.rgb(255, 204, 204), // Rosa clar
            Color.rgb(204, 229, 255), // Blau clar
            Color.rgb(204, 255, 204), // Verd clar
            Color.rgb(255, 255, 204), // Groc clar
            Color.rgb(255, 229, 204), // Taronja clar
            Color.rgb(229, 204, 255)  // Lila clar
        )
        barDataSet.colors=pastelColors
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 16f
        barDataSet.valueFormatter= DefaultValueFormatter(0)

        binding.barchartDaus.apply {
            data= BarData(barDataSet)
            //setDrawGridBackground(false)

            xAxis.setDrawGridLines(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            axisRight.setDrawGridLines(false)
            axisRight.setDrawLabels(false)
            description.text="Freqüència daus"
            description.isEnabled = true // Activa la descripció
            setFitBars(true) // Ajusta les barres al gràfic
            animateY(1000) // Animació en Y
            invalidate()
        }
    }
}