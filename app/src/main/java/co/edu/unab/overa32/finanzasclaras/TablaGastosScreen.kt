package co.edu.unab.overa32.finanzasclaras

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablaGastosScreen (navController: NavController,onNavigateToAlertas: (Double) -> Unit){
    val context = LocalContext.current
    val saldoDataStore = remember { SaldoDataStore(context) }
    var gastos by remember { mutableStateOf(listOf<Triple<String, Double, String>>()) }
    // Función para borrar el archivo
    fun borrarArchivoGastos() {
        val file = File(context.filesDir, "gastos.txt")
        if (file.exists()) {
            file.delete()
            gastos = emptyList() // Limpiar la lista en memoria
            Toast.makeText(context, "Archivo borrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "No existe el archivo", Toast.LENGTH_SHORT).show()
        }
    }
    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "gastos.txt")
        if (file.exists()) {
            val lines = file.readLines()
                .filter { it.isNotBlank() } // Filtra líneas vacías
                .map { it.trim() } // Elimina espacios en blanco
            gastos = lines.mapNotNull { line ->
                val parts = line.split("|")
                if (parts.size == 3) {
                    val desc = parts[0].trim()
                    val monto = parts[1].trim().toDoubleOrNull()
                    val fecha = parts[2].trim()
                    if (monto != null) Triple(desc, monto, fecha) else null
                } else null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabla de Gastos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFE57373)) // rojo suave
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Descripción", fontWeight = FontWeight.Bold)
                Text("Monto", fontWeight = FontWeight.Bold)
                Text("Fecha", fontWeight = FontWeight.Bold)
            }
            Divider(thickness = 1.dp, color = Color.White)
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                itemsIndexed(gastos) { index, gasto ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(gasto.first)
                        Text("$${gasto.second}")
                        Text(gasto.third)
                        IconButton(onClick = {
                            gastos = gastos.toMutableList().also { it.removeAt(index) }
                            val file = File(context.filesDir, "gastos.txt")
                            file.writeText(gastos.joinToString("\n") {
                                "${it.first}|${it.second}|${it.third}"
                            })
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }

            }
            Button(
                onClick = {
                    val totalGastos = gastos.sumOf { it.second }
                    onNavigateToAlertas(totalGastos)

                    CoroutineScope(Dispatchers.IO).launch {
                        saldoDataStore.saveSaldo(totalGastos)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Ir a Alertas")
            }
        }
    }
}
