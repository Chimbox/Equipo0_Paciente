package felix.alfonso.equipo0_paciente.dominio

import java.util.*

data class Solicitud(var idCita: Int, var nombreSolicitante:String, var fechaHora:Date, var aprobada:Boolean, var body:String)
