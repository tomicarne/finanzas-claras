package co.edu.unab.overa32.finanzasclaras // O el subpaquete donde esté este archivo

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color // Asegúrate de que esta importación esté presente
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat // Para formatear el saldo como moneda
import java.util.Locale // Para especificar la localización del formato

// --- Definiciones de Colores (Eliminadas para evitar errores) ---
// Usaremos colores predefinidos como Color.White, Color.Gray, etc.
// y los colores por defecto del MaterialTheme

// --- Función Composable Principal Modificada (Usando colores predefinidos/del tema) ---
@SuppressLint("ContextCastToActivity")
@Composable
fun PantallaPrincipalUI(saldoTotal: Double,navController: NavController) {
    val activity = (LocalContext.current as? Activity)
    // Usamos un Column para apilar los elementos verticalmente
    Column(
        modifier = Modifier
            .fillMaxSize() // Hace que la columna ocupe toda la pantalla
            // *** Usamos Color.LightGray o Color.White como fondo si no hay tema personalizado ***
            .background(Color.White) // Fondo básico
            .padding(16.dp), // Añade espacio alrededor del contenido
        horizontalAlignment = Alignment.CenterHorizontally // Centra los elementos hijos horizontalmente
        // *** ELIMINAMOS verticalArrangement.spacedBy aquí para controlar el espacio manualmente ***
    ) {
        Spacer(Modifier.height(32.dp))
        // --- Botón "Añadir Gasto" ---
        Button(
            onClick = {
                navController.navigate("addGasto")// TODO: Implementar acción al hacer clic
            },
            colors = ButtonDefaults.buttonColors(
                // *** Usamos MaterialTheme.colors.primary o un Color básico ***
                containerColor = MaterialTheme.colorScheme.primary, // Color primario del tema
                contentColor = MaterialTheme.colorScheme.onPrimary // Color del texto sobre el primario
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Añadir Gasto", fontSize = 18.sp)
        }

        // *** Espacio después del botón "Añadir Gasto" ***
        Spacer(Modifier.height(50.dp))

        // --- Sección para mostrar el Total ---
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                // *** Usamos MaterialTheme.colors.surface o Color.White ***
                containerColor = MaterialTheme.colorScheme.surface // Color de superficie del tema (usualmente blanco/gris claro)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Total Gastado:",
                    fontSize = 16.sp,
                    // *** Usamos Color.Gray o MaterialTheme.colors.onSurfaceVariant ***
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Color secundario de texto del tema
                )
                Spacer(Modifier.height(4.dp))

                val format = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // Ajusta la localización
                val saldoFormateado = format.format(saldoTotal)

                Text(
                    text = saldoFormateado,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    // *** Usamos Color.Black o MaterialTheme.colors.onSurface ***
                    color = MaterialTheme.colorScheme.onSurface // Color principal de texto del tema
                )
            }
        }

        // *** Espacio después de la tarjeta del Total ***
        Spacer(Modifier.height(112.dp))

        // --- Botones Secundarios (Usando colores básicos o del tema) ---
        // Cada botón tendrá su propio Spacer después para control de separación

        // Botón "Tabla de gastos"
        Button(
            onClick = {
                navController.navigate("tablaGastos")// TODO: Implementar acción al hacer clic
            },
            colors = ButtonDefaults.buttonColors(
                // *** Usamos colores básicos o del tema ***
                containerColor = MaterialTheme.colorScheme.secondary, // Ejemplo: color secundario del tema
                contentColor = MaterialTheme.colorScheme.onSecondary // Ejemplo: texto sobre secundario
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Tabla de gastos", fontSize = 18.sp)
        }

        // *** Espacio entre botones secundarios ***
        Spacer(Modifier.height(30.dp))

        // Botón "preguntas"
        Button(
            onClick = {
                navController.navigate("aiScreen")
            },
            colors = ButtonDefaults.buttonColors(
                // *** Usamos otros colores básicos o del tema ***
                containerColor = Color(0xFF64B5F6), // Ejemplo: Un azul claro (Material Blue 300)
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("preguntas", fontSize = 18.sp)
        }

        // *** Espacio entre botones secundarios ***
        Spacer(Modifier.height(30.dp))

        // Botón "Configuracion"
        Button(
            onClick = {
                navController.navigate("ajustes")// TODO: Implementar acción al hacer clic
            },
            colors = ButtonDefaults.buttonColors(
                // *** Usamos otros colores básicos o del tema ***
                containerColor = Color(0xFF81C784), // Ejemplo: Un verde claro (Material Green 300)
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Configuracion", fontSize = 18.sp)
        }

        // --- Espacio para empujar el botón Salir hacia abajo ---
        Spacer(Modifier.weight(1f))

        // --- Botón "Salir" ---
        Button(
            onClick = {
                activity?.finishAffinity()// TODO: Implementar acción al hacer clic (ej. finish())
            },
            colors = ButtonDefaults.buttonColors(
                // *** Usamos Color.Red o MaterialTheme.colors.error ***
                containerColor = MaterialTheme.colorScheme.error, // Color de error del tema (usualmente rojo)
                contentColor = MaterialTheme.colorScheme.onError // Texto sobre color de error
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Salir", fontSize = 18.sp)
        }
    }
}

// --- Función @Preview para ver el diseño ---


// --- Data Class Expense y Lista de Ejemplo ---
// Mantén la definición de Expense y sampleExpenseList como estaban
// Solo asegúrate de que los colores que uses en sampleExpenseList
// sean colores predefinidos (Color.Red, Color.Blue, etc.) si no defines colores personalizados.
/*
data class Expense( ... )
val sampleExpenseList = listOf( ... ) // Usar Color.Red, Color.Blue, etc. aquí
*/

// --- Uso en MainActivity ---
/*
// Asegúrate de envolver con un MaterialTheme en tu Activity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { // Aplica el MaterialTheme por defecto o tu tema personalizado
                 // Llama a tu composable principal
                 // PantallaPrincipalUI(saldoTotal = ...)
                 // o MainScreenWithState() si usas ese enfoque para el estado
            }
        }
    }
}
*/
