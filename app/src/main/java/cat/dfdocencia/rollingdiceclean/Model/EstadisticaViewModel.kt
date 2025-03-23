package cat.dfdocencia.rollingdiceclean.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.launch

class EstadisticaViewModel(private val dataprovider: EstadisticaProvider) : ViewModel() {


    private val _saved = MutableLiveData<Boolean>(false)
    val saved: LiveData<Boolean> get() = _saved

    private val _resultEstadistica = MutableLiveData<Result<Estadistica>>()
    val resultEstadistica: LiveData<Result<Estadistica>> get() = _resultEstadistica

    //Necessitem el patró factory per a que es pugui crear el viewmodel
    //Ho fem en el companion object
    //    viewModelFactory:
    //    Aquesta funció de la biblioteca androidx.lifecycle:lifecycle-viewmodel-ktx permet crear un ViewModelProvider.Factory de manera més concisa.
    //    Dins de initializer, pots inicialitzar el teu ViewModel amb les dependències necessàries.
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val dataprovider = EstadisticaProvider
                EstadisticaViewModel(dataprovider)
            }
    }}

    fun cargarEstadistica(idDispositiu: String) {
        viewModelScope.launch {
            val result = dataprovider.carregarEstadistica(idDispositiu)
            _resultEstadistica.value=result
        }
    }

    fun guardarEstadistica(idDispositiu:String) {
        viewModelScope.launch {
            val result= dataprovider.guardarEstadistica(idDispositiu,dataprovider.dataEstadistica)
            _saved.value=result.isSuccess
        }
    }

    fun ActualitzaEstadistica(dau1:Int, dau2:Int){
        EstadisticaProvider.afegeixTirada(dau1,dau2)

        //Actualitzem la variable per a que els observers s'enterin.
        _resultEstadistica.value=Result.success(dataprovider.dataEstadistica)

    }
}