package cat.dfdocencia.rollingdiceclean

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cat.dfdocencia.rollingdiceclean.Model.Estadistica
import cat.dfdocencia.rollingdiceclean.Model.EstadisticaProvider
import cat.dfdocencia.rollingdiceclean.Model.EstadisticaViewModel
import cat.dfdocencia.rollingdiceclean.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vmodel: EstadisticaViewModel by viewModels { EstadisticaViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.maintoolbar)

        supportActionBar?.title = MainApp.nomAplicacio

        binding.txtDice1.text = getString(R.string.novaluestring)
        binding.txtDice2.text = getString(R.string.novaluestring)
        binding.txtTotal.text = getString(R.string.novaluestring)

        binding.tiradesText.text="Carregant Estadística..."
        vmodel.cargarEstadistica(MainApp.idDispositiu)

        binding.btnRoll.setOnClickListener {

        }
        binding.btnRoll.setOnClickListener(this::onbtnRollClicked)
        vmodel.resultEstadistica.observe(this, this::onEstaditicaChanged)
    }

    private fun onbtnRollClicked(view: View?) {
        val dice1 = Random.nextInt(1, 7)
        val dice2 = Random.nextInt(1, 7)
        val sum = dice1 + dice2
        binding.txtDice1.text = dice1.toString()
        binding.txtDice2.text = dice2.toString()
        binding.txtTotal.text = sum.toString()
        vmodel.ActualitzaEstadistica(dice1, dice2)
    }

    private fun onEstaditicaChanged(result: Result<Estadistica>?) {
        if (result!!.isSuccess){
            Log.i(
                "MainActivity",
                "Estadistica actualitzada. Num tirades ${result!!.getOrNull()!!.tirades.toString()}"
            )
            binding.tiradesText.text =
                "Tirades totals:" + EstadisticaProvider.dataEstadistica.tirades.toString()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuEstadistica) {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
            return true
        } else return super.onOptionsItemSelected(item)

    }

}