package cat.dfdocencia.rollingdiceclean

import android.app.Application
import android.provider.Settings

class MainApp : Application() {

    companion object {
        var idDispositiu = ""
        const val nomAplicacio = "Rolling Dice"
        const val idAplicacio = "rollindiceclean"
    }

    override fun onCreate() {
        super.onCreate()
        //Es desaconsella utilitzar ids lligats al dispositiu,
        //https://developer.android.com/identity/user-data-ids
        idDispositiu = Settings.Secure.getString(
            getApplicationContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )

    }
}