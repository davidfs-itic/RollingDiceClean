package cat.dfdocencia.rollingdiceclean.Model

data class Estadistica (
    var tirades: Int = 0,
    var numdobles: Int = 0,
    var daus: ArrayList<Int> = arrayListOf<Int>(0, 0, 0, 0, 0, 0)
    )
