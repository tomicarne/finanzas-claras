package co.edu.unab.overa32.finanzasclaras // Reemplaza con tu paquete

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat // Para formatear el saldo como moneda
import java.util.Locale // Para especificar la localización del formato
import androidx.compose.foundation.shape.CircleShape // Importar para CircleShape
import androidx.compose.ui.draw.clip // Importar para clip
import androidx.compose.ui.text.style.TextAlign

// --- Definiciones de Colores --- (Mantente consistentes con tu tema)
// Asumo que usas colores similares a los de la pantalla principal rediseñada
val PrimaryBlue = Color(0xFF0077B6)
val SecondaryOrange = Color(0xFFFB8500)
val GentleBlueBackground = Color(0xFFE3F2FD) // Si usas este fondo en toda la app
val CardBackground = Color.White
val TextColorPrimary = Color(0xFF212121)
val TextColorSecondary = Color(0xFF757575)
val DangerColor = Color(0xFFD32F2F)
val ButtonColorGreen = Color(0xFF81C784)
val ButtonColorPurple = Color(0xFFBA68C8)
val ButtonColorTeal = Color(0xFF4FC3F7)

// Colores personalizados si los usas en la lista de ejemplo
val CustomPurple = Color(0xFF9C27B0)
val CustomOrange = Color(0xFFFF9800)
val CustomGray = Color(0xFF9E9E9E)

// --- Data Class para un Gasto --- (Igual que antes)
data class Expense(
    val id: Long,
    val description: String,
    val amount: Double,
    val dateInfo: String,
    val categoryColor: Color
)

// --- Composable de la Pantalla de Tabla de Gastos ---
@OptIn(ExperimentalMaterial3Api::class) // Se requiere para TopAppBar
@Composable
fun TablaScreen(
    onBackClick: () -> Unit, // Acción para el botón de volver
    onCrearAlertasClick: () -> Unit // *** NUEVA: Acción para el botón "Crear Alertas" ***
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabla de Gastos") },
                navigationIcon = {
                    // *** BOTÓN DE VOLVER (YA ESTABA, VISIBLE EN PREVIEW) ***
                    IconButton(onClick = onBackClick) { // Usa la lambda onBackClick
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Acción del menú */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White) // Fondo blanco similar al de la imagen
                .padding(horizontal = 16.dp) // Añade padding horizontal aquí para no afectar el encabezado y lista
        ) {
            // --- Fila de Encabezados ---
            // Esta fila NO tiene padding horizontal propio para que los bordes coincidan con la lista
            ExpenseTableHeader(modifier = Modifier.padding(horizontal = 0.dp)) // Pasa 0.dp para eliminar padding horizontal si la Column ya lo tiene

            // --- Lista de Gastos (Desplazable) ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Hace que la lista ocupe todo el espacio disponible restante
            ) {
                items(sampleExpenseList) { expense ->
                    ExpenseListItem(expense = expense)
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }

            // *** NUEVO BOTÓN: "Crear Alertas" ***
            Spacer(Modifier.height(24.dp)) // Espacio entre la lista y el botón
            Button(
                onClick = onCrearAlertasClick, // *** Llama a la lambda para la acción ***
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryOrange, // Usamos el color secundario para este botón
                    contentColor = Color.White // Texto blanco
                ),
                modifier = Modifier
                    .fillMaxWidth() // Ocupa el ancho máximo
                    .height(56.dp), // Altura estándar
                shape = RoundedCornerShape(8.dp) // Esquinas redondeadas
            ) {
                Text("Crear Alertas", fontSize = 18.sp)
            }
            Spacer(Modifier.height(16.dp)) // Espacio después del botón si es el último elemento antes del borde inferior
        }
    }
}

// --- Composable para la Fila de Encabezados ---
@Composable
fun ExpenseTableHeader(modifier: Modifier = Modifier) { // Recibe un modificador
    Row(
        modifier = modifier // *** Aplica el modificador recibido ***
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5)) // Gris muy claro
            .padding(horizontal = 16.dp, vertical = 8.dp), // Padding interno del encabezado
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Description",
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Text(
            text = "Amount",
            modifier = Modifier.width(80.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.End
        )

        Text(
            text = "Date",
            modifier = Modifier.width(60.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.End
        )
    }
}

// --- Composable para un Item Individual de la Lista de Gastos --- (Igual que antes)
@Composable
fun ExpenseListItem(expense: Expense) {
    Row(
        // Nota: El padding horizontal 16.dp ahora viene de la Column padre
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp), // Solo padding vertical aquí
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Indicador de Categoría (el círculo de color)
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(expense.categoryColor)
        )

        Spacer(Modifier.width(12.dp))

        // Descripción y Posible Subtexto
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = expense.description,
                fontSize = 16.sp,
                color = TextColorPrimary // Usar color de texto del tema
            )
            // Opcional: Subtexto
            // Text("Categoría", fontSize = 12.sp, color = TextColorSecondary)
        }

        // Monto Formateado
        val format = NumberFormat.getCurrencyInstance(Locale("en", "US")) // Ajusta localización
        val amountFormatted = format.format(expense.amount)

        Text(
            text = amountFormatted,
            modifier = Modifier.width(80.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextColorPrimary, // Usar color de texto del tema
            textAlign = TextAlign.End
        )

        // Información Adicional
        Text(
            text = expense.dateInfo,
            modifier = Modifier.width(60.dp),
            fontSize = 14.sp,
            color = TextColorSecondary, // Usar color de texto secundario del tema
            textAlign = TextAlign.End
        )
    }
}


// --- Datos de Ejemplo para la Lista --- (Igual que antes)
val sampleExpenseList = listOf(
    Expense(1, "Cocorrico", 1.80, "8:15 TU", Color.Blue),
    Expense(2, "Uber Ride", 27.123, "2:00", Color.Red),
    Expense(3, "Uber Ride", 22.80, "2:00", Color.Red),
    Expense(4, "Eater Jaes", 5.09, "...", Color.Green),
    Expense(5, "Suber Joess", 5.90, "...", Color.Green),
    Expense(6, "Uber Ride", 50.80, "1", Color.Red),
    Expense(7, "Spoority", 55.09, "$", Color.Blue),
    Expense(8, "Paymharion", 0.0, "...", Color.Blue),
    Expense(9, "Parenty", 0.0, "1", CustomGray),
    Expense(10, "Sospcripbion", 0.0, "...", CustomPurple),
    Expense(11, "Casvricbion", 0.0, "...", CustomPurple),
    Expense(12, "Payarmioeen", 25.00, "...", CustomGray),
    Expense(13, "Otra Prueba", 10.50, "Hoy", CustomOrange),
)


// --- Preview ---
@Preview(showBackground = true)
@Composable
fun PreviewTablaScreen() {
    // Envuelve con tu tema si lo tienes
    // FinanzasClarasTheme {
    TablaScreen(
        onBackClick = { /* Acción de preview vacía */ }, // Aquí la acción está vacía
        onCrearAlertasClick = { /* Acción de preview vacía */ } // Aquí la acción está vacía para el preview
    )
    // }
}

// --- Cómo integrar en tu MainActivity ---
/*
package co.edu.unab.overa32.finanzasclaras // Tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Importa tu tema y la pantalla
import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme
import co.edu.unab.overa32.finanzasclaras.TablaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanzasClarasTheme { // Aplica tu tema
                // Aquí deberías tener la navegación de tu app
                // Por ejemplo, usando un NavHostController

                TablaScreen(
                    onBackClick = {
                        // TODO: Implementar la navegación hacia atrás
                        // navigator.popBackStack() si usas Navigation Compose
                        // finish() si estás en una Activity simple y quieres cerrarla
                    },
                    onCrearAlertasClick = {
                         // TODO: Implementar la navegación a la pantalla de Alertas
                         // navigator.navigate("ruta_a_pantalla_alertas")
                    }
                )
            }
        }
    }
}
*/