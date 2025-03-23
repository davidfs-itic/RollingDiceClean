package cat.dfdocencia.rollingdiceclean.Model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


//singelgon, semblant a static en java.
//Objecte únic

object EstadisticaProvider {
    //private val firestore = FirebaseFirestore.getInstance()
    //La variable s'inicialitzarà la primera vegada que s'utilitzi.
    val db: FirebaseFirestore by lazy { Firebase.firestore }
    var dataEstadistica=Estadistica()

    //Retorna Unit perque no necessitem cap valor
    //només si ha funcionat o no. El valor d'estadistica es guarda
    //en la variable estadistica.
    suspend fun carregarEstadistica(idDispositiu:String): Result<Estadistica> {
        return try {

            //Obtenim les dades de la base de dades.
            //Guardarem les tirades en la col·lecció Devices.
            //Per cada Device(identificat amb un id), es guardaran les estadístiques.
            val doc = db.collection("Devices").document(idDispositiu)
                .get()
                .await()
            val valorbbdd=doc.toObject<Estadistica>()
            if (valorbbdd != null) {
                dataEstadistica=valorbbdd
                Result.success(valorbbdd)
            } else {

                //si la creem per defecte, o esperem que l'usuari la guardi
                //db.collection("Devices").document(MainApp.idDispositiu).set(estadisticadefecte).await()
                //val estadisticadefecte=Estadistica()
                Result.failure(Exception("Estadística no trobada. Documentid ${idDispositiu}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend  fun guardarEstadistica(idDispositiu:String, estadistica:Estadistica): Result<Unit> {
        return try {
            db.collection("Devices").document(idDispositiu).set(estadistica).await()
            dataEstadistica=estadistica
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    fun afegeixTirada(dau1:Int,dau2:Int){
        dataEstadistica.daus[dau1-1]++
        dataEstadistica.daus[dau2-1]++
        dataEstadistica.tirades++
        if (dau1==dau2) dataEstadistica.numdobles++
    }
}