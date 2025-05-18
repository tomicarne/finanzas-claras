package co.edu.unab.overa32.finanzasclaras // *** IMPORTANTE: Reemplaza con el nombre correcto de tu paquete ***

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

// *** IMPORTANTE: Si tienes un tema Material 3 personalizado en ui.theme, ***
// *** deberías usar ese tema en lugar de MaterialTheme {} directamente. ***
// import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme


// --- Composable que maneja el Estado y las Acciones de Navegación ---
// Esta es la función que llamarías desde tu MainActivity.
// Maneja el estado del saldo y define qué sucede cuando se hace clic en los botones.
@Composable
fun MainScreenWithState() {
    // Estado mutable para el saldo total. Cambiar este valor redibuja la UI que lo usa.
    var currentSaldo by remember { mutableStateOf(0.0) } // Empieza en 0.0 por defecto

    // --- Definición de las Acciones de los Botones (Lambdas) ---
    // *** Aquí es donde pones tu LÓGICA DE NAVEGACIÓN REAL o LA ACCIÓN CORRESPONDIENTE. ***

    val navigateToAddExpense: () -> Unit = {
        // TODO: Implementar navegación a la pantalla para AÑADIR un nuevo gasto (GastosCompletosScreen)
        println(">>> Navegar a Añadir Gasto Completo")
        // Si usas Navigation Compose: navController.navigate("ruta_a_gastos_completos")
    }

    val navigateToTablaGastos: () -> Unit = {
        // TODO: Implementar navegación a la pantalla de Tabla de Gastos (TablaScreen)
        println(">>> Navegar a Tabla de Gastos")
        // Si usas Navigation Compose: navController.navigate("ruta_a_la_tabla_de_gastos")
    }

    val navigateToAjustes: () -> Unit = {
        // TODO: Implementar navegación a la pantalla de Ajustes (AjustesScreen)
        println(">>> Navegar a Ajustes")
        // Si usas Navigation Compose: navController.navigate("ruta_a_ajustes")
    }

    val exitApp: () -> Unit = {
        // TODO: Implementar la lógica para SALIR de la aplicación
        println(">>> Salir de la aplicación")
        // Puedes necesitar el Contexto para esto: val context = LocalContext.current
        // (context as? Activity)?.finish()
    }

    // Llamamos a PantallaPrincipalUI pasándole el estado actual del saldo
    // y las lambdas que definen las acciones de cada botón.
    PantallaPrincipalUI(
        saldoTotal = currentSaldo, // Le pasamos el valor actual del estado
        onAddExpenseClick = navigateToAddExpense, // Le decimos qué hacer al presionar Añadir Gasto
        onTablaGastosClick = navigateToTablaGastos, // Le decimos qué hacer al presionar Tabla de gastos
        onConfiguracionClick = navigateToAjustes, // Le decimos qué hacer al presionar Configuracion
        onSalirClick = exitApp // Le decimos qué hacer al presionar Salir
    )
}


// --- Composable de la Interfaz de la Pantalla Principal (Espaciado Manual) ---
// Esta función solo se encarga de cómo SE VE la pantalla basándose en los datos y acciones.
@Composable
fun PantallaPrincipalUI(
    saldoTotal: Double, // Dato que recibe para mostrar
    onAddExpenseClick: () -> Unit, // Acción a ejecutar al hacer clic en Añadir Gasto
    onTablaGastosClick: () -> Unit, // Acción a ejecutar al hacer clic en Tabla de gastos
    onConfiguracionClick: () -> Unit, // Acción a ejecutar al hacer clic en Configuracion
    onSalirClick: () -> Unit // Acción a ejecutar al hacer clic en Salir
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp), // Padding horizontal para el contenido
        horizontalAlignment = Alignment.CenterHorizontally
        // Quitamos verticalArrangement.spacedBy aquí para controlar el espacio manualmente con Spacers
    ) {
        // --- Espacio en la parte superior ---
        Spacer(Modifier.height(32.dp)) // Espacio fijo en la parte superior

        // --- Botón "Añadir Gasto" ---
        Button(
            onClick = onAddExpenseClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Añadir Gasto", fontSize = 18.sp)
        }

        Spacer(Modifier.height(24.dp))

        // --- Sección para mostrar el Total ---
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Total Gastado:",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))

                val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
                val saldoFormateado = format.format(saldoTotal)

                Text(
                    text = saldoFormateado,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // *** Espacio MÁS GRANDE entre Total Gastado y Tabla de gastos ***
        Spacer(Modifier.height(200.dp)) // <-- Ajusta este valor para el espacio deseado (ej: 48.dp, 64.dp)

        // --- Botón "Tabla de gastos" ---
        Button(
            onClick = onTablaGastosClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Tabla de gastos", fontSize = 18.sp)
        }

        // --- Espacio entre botones secundarios ---
        Spacer(Modifier.height(24.dp)) // Espacio fijo (igual que después de Añadir Gasto)

        // Botón "Configuracion"
        Button(
            onClick = onConfiguracionClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF81C784), // Ejemplo: Un verde claro
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Configuracion", fontSize = 18.sp)
        }

        // --- Espacio antes del botón Salir ---
        Spacer(Modifier.height(32.dp)) // Espacio fijo (ajusta si es necesario)

        // --- Botón "Salir" ---
        Button(
            onClick = onSalirClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
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


// --- Preview ---
@Preview(showBackground = true) // *** ESTA ES LA ANOTACIÓN PARA EL PREVIEW ***
@Composable // *** ES UNA FUNCIÓN COMPOSABLE ***
fun MainScreenPreview() { // *** ESTE ES EL NOMBRE DE LA FUNCIÓN DE PREVIEW ***
    // *** Envuelve con tu MaterialTheme para ver los colores y tipografía correctos ***
    // Asegúrate de tener un tema Material 3 definido en ui.theme
    // Si no tienes uno, usa MaterialTheme {} para los colores por defecto
    MaterialTheme { // Usamos el tema por defecto o tu tema personalizado
        // En el preview, llamamos a MainScreenWithState (que a su vez llama a PantallaPrincipalUI)
        // Pasamos lambdas vacías ya que no hay navegación real en el preview.
        MainScreenWithState()
    }
}

// --- Cómo usarlo en tu MainActivity ---
/*
// Este código iría en tu archivo MainActivity.kt
package co.edu.unab.overa32.finanzasclaras // Tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
// Importa tu tema
// import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme

// *** Importa el composable principal que maneja el estado y las acciones ***
import co.edu.unab.overa32.finanzasclaras.MainScreenWithState

// Importa los composables de las otras pantallas si usas Navigation Compose
// import co.edu.unab.overa32.finanzasclaras.TablaScreen
// import co.edu.unab.overa32.finanzasclaras.GastosCompletosScreen
// import co.edu.unab.overa32.finanzasclaras.AjustesScreen
// import co.edu.unab.overa32.finanzasclaras.AlertasScreen
// import co.edu.unab.overa32.finanzasclaras.IaScreen

// Si usas Navigation Compose:
// import androidx.navigation.compose.NavHost
// import androidx.navigation.compose.composable
// import androidx.navigation.compose.rememberNavController
// import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // *** Envuelve todo el contenido con tu tema (MaterialTheme) ***
            // Si usas un tema personalizado: YourAppNameTheme { ... }
            MaterialTheme { // Usamos el MaterialTheme por defecto
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
                ) {
                    // *** Aquí es donde llamas a tu composable principal (MainScreenWithState) ***

                    // Si usas Navigation Compose, pondrías tu NavHost aquí
                    // val navController = rememberNavController()
                    // NavHost(navController = navController, startDestination = "main_screen") {
                    //     composable("main_screen") {
                    //          MainScreenWithState(
                    //              onAddExpenseClick = { navController.navigate("gastos_completos_screen") },
                    //              onTablaGastosClick = { navController.navigate("tabla_screen") },
                    //              onConfiguracionClick = { navController.navigate("ajustes_screen") },
                    //              onSalirClick = { /* Lógica para salir */ }
                    //          )
                    //      }
                    //      // Define las otras rutas aquí
                    //      // composable("tabla_screen") { TablaScreen(...) }
                    //      // composable("gastos_completos_screen") { GastosCompletosScreen(...) }
                    //      // ... etc.
                    // }

                    // *** Si no usas Navigation Compose y solo quieres ver la pantalla principal al inicio ***
                     MainScreenWithState() // Esto mostrará la pantalla principal. Las acciones de los botones imprimirán en consola por ahora.
                }
            }
        }
    }
}
*/