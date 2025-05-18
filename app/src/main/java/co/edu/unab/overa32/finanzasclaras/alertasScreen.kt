package co.edu.unab.overa32.finanzasclaras


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.* // Para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.NumberFormat // Para formatear el saldo como moneda
import java.util.Locale // Para especificar la localización del formato

// --- 1. Clase de Datos para un Umbral de Alerta ---
data class AlertThreshold(
    val id: Long, // Identificador único
    val amount: Double, // Monto del umbral
    val isEnabled: Boolean // Si la alerta está activa
)

// --- 2. Definiciones de Colores (Aproximados a la imagen) ---
val PurpleBackground = Color(0xFF673AB7) // Un púrpura para el fondo
val CardColorDark = Color(0xFF424242) // Un gris oscuro/marrón para las tarjetas de alerta y saldo
val GreenToggleActive = Color(0xFF4CAF50) // Color verde para el interruptor activo
val GrayToggleInactive = Color(0xFFB0BEC5) // Color gris para el interruptor inactivo
val TextColorWhite = Color.White // Texto blanco
val TextColorGray = Color.Gray // Texto gris secundario (si se usa)


// --- 3. Composable de la Pantalla de Alertas ---
@OptIn(ExperimentalMaterial3Api::class) // Para TopAppBar
@Composable
fun AlertasScreen(
    myNavController: NavHostController, // Acción para el botón de volver
    onBackClick: () -> Unit,
    onAddAlertClick: () -> Unit, // Acción para el botón "+" de añadir alerta){}
    function: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Alertas", color = TextColorWhite) }, // Título blanco
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextColorWhite) // Icono blanco
                    }
                },
                actions = {
                    // Botón "+" para añadir nueva alerta
                    IconButton(onClick = onAddAlertClick) {
                        Icon(Icons.Filled.Add, contentDescription = "Añadir Alerta", tint = TextColorWhite) // Icono blanco
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PurpleBackground) // Fondo púrpura para la barra superior
            )
        }
    ) { paddingValues ->
        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .padding(paddingValues) // Aplica el padding del Scaffold
                .fillMaxSize()
                .background(PurpleBackground) // Fondo púrpura para el contenido
                .padding(horizontal = 16.dp, vertical = 24.dp), // Padding alrededor del contenido
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre los elementos principales
        ) {
            // --- Lista de Umbrales de Alerta ---
            // Como la lista en la imagen parece fija y pequeña, usamos Column.
            // Para una lista potencialmente larga, usar LazyColumn sería más eficiente.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // Espacio entre los umbrales individuales
            ) {
                // Aquí deberías pasar tu lista real de AlertThresholds
                // Usamos datos de ejemplo por ahora:
                sampleAlertThresholds.forEach { alert ->
                    AlertThresholdItem(alert = alert)
                }
            }

            // --- Espacio para separar umbrales y saldos ---
            Spacer(Modifier.height(32.dp)) // Espacio mayor

            // --- Sección de Saldo Total ---
            SaldoDisplayCard(label = "saldo total:", amount = 15400000.00) // Usamos una función auxiliar

            // --- Sección de Saldo Actual ---
            SaldoDisplayCard(label = "saldo actual", amount = 1500000.00) // Usamos la misma función

            // Si hubiera espacio restante, puedes empujar los elementos hacia arriba
            // Spacer(Modifier.weight(1f))
        }
    }
}

// --- 4. Composable para un Item Individual de Umbral de Alerta ---
@Composable
fun AlertThresholdItem(alert: AlertThreshold) {
    // Usamos una Card para el contenedor con esquinas redondeadas y fondo oscuro
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Esquinas redondeadas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Sombra sutil
        colors = CardDefaults.cardColors(containerColor = CardColorDark) // Fondo oscuro
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp) // Padding interno
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Distribuye espacio
        ) {
            // Monto del Umbral (Formateado)
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // Ajusta la localización
            val amountFormatted = format.format(alert.amount)

            Text(
                text = amountFormatted,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextColorWhite, // Texto blanco
                modifier = Modifier.weight(1f) // Permite que el texto ocupe espacio
            )

            // Interruptor (Switch)
            // El estado del switch debe ser mutable si quieres que cambie al tocarlo
            // Aquí solo mostramos el estado inicial del dato
            var isChecked by remember { mutableStateOf(alert.isEnabled) } // Estado mutable para el switch

            Switch(
                checked = isChecked, // Estado actual del switch
                onCheckedChange = { enabled ->
                    isChecked = enabled // Actualiza el estado local al interactuar
                    // TODO: Aquí deberías notificar a tu ViewModel o lógica
                    // para actualizar el estado real de la alerta en tu lista/base de datos
                },
                colors = SwitchDefaults.colors( // Colores del switch
                    checkedThumbColor = Color.White, // El "dedo" (círculo) cuando está activo
                    checkedTrackColor = GreenToggleActive, // La "pista" (fondo) cuando está activo
                    uncheckedThumbColor = Color.White, // El "dedo" cuando está inactivo
                    uncheckedTrackColor = GrayToggleInactive // La "pista" cuando está inactivo
                )
            )
        }
    }
}

// --- 5. Composable Auxiliar para Mostrar Saldos ---
@Composable
fun SaldoDisplayCard(label: String, amount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), // Esquinas redondeadas consistentes
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = CardColorDark) // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp) // Padding interno
                .fillMaxWidth()
        ) {
            Text(
                text = "$label:", // Etiqueta (saldo total o saldo actual)
                fontSize = 16.sp,
                color = TextColorWhite // Texto blanco
            )
            Spacer(Modifier.height(4.dp)) // Pequeño espacio

            val format = NumberFormat.getCurrencyInstance(Locale("es", "CO")) // Ajusta la localización
            val amountFormatted = format.format(amount)

            Text(
                text = amountFormatted, // Monto formateado
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextColorWhite // Texto blanco
            )
        }
    }
}

// --- 6. Datos de Ejemplo para los Umbrales de Alerta ---
// En una aplicación real, esto vendría de tu estado o ViewModel
val sampleAlertThresholds = listOf(
    AlertThreshold(1, 680000.00, true),
    AlertThreshold(2, 600000.00, true),
    AlertThreshold(3, 500000.00, true),
    AlertThreshold(4, 200000.00, true) // El último en la imagen también parece activo
)



// --- Cómo integrar en tu MainActivity ---
/*
package co.edu.unab.overa32.finanzasclaras // Tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Importa tu tema y la pantalla de alertas
import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme
import co.edu.unab.overa32.finanzasclaras.AlertasScreen // *** Importa la pantalla de alertas ***

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanzasClarasTheme { // Aplica tu tema
                // Si usas Navigation Compose, la navegación manejaría esto
                AlertasScreen(
                    onBackClick = {
                        // TODO: Implementar navegación hacia atrás
                        // navigator.popBackStack()
                    },
                    onAddAlertClick = {
                        // TODO: Implementar navegación a la pantalla para añadir/editar alerta
                        // navigator.navigate("ruta_para_añadir_alerta")
                    }
                )
            }
        }
    }
}
*/