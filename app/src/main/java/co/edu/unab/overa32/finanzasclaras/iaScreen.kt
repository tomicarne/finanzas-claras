package co.edu.unab.overa32.finanzasclaras // Reemplaza con tu paquete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
// *** Importa el ícono de enviar ***
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.* // Para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

// --- 1. Definiciones de Colores --- (Iguales que antes)
val SoftBackgroundColor = Color(0xFFD1C4E9) // Un lavanda/púrpura suave para el fondo
val MessageBubbleColor = Color(0xFF4527A0) // Un púrpura oscuro para los "globos" de mensaje/tarjetas
val InputFieldBackgroundColor = Color.White // Blanco para el fondo del campo de entrada
val SendButtonColor = Color(0xFF81C784) // Un verde para el botón de enviar
val TextColorOnDark = Color.White // Texto blanco sobre fondos oscuros
val TextColorOnLight = Color.Black // Texto negro sobre fondos claros
val PlaceholderTextColor = Color.Gray // Color para el texto de placeholder en el input

// --- 2. Composable de la Pantalla de IA ---
@OptIn(ExperimentalMaterial3Api::class) // Para TopAppBar
@Composable
fun IaScreen(
    myNavController: NavHostController,
    onBackClick: () -> Unit, // Acción para el botón de volver){}
    function: () -> Unit
    // Puedes añadir un parámetro para enviar el mensaje si la lógica está fuera
    // onSendMessage: (String) -> Unit
) {
    // Estado para el texto en el campo de entrada
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asistente IA", color = TextColorOnDark) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = TextColorOnDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MessageBubbleColor)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(SoftBackgroundColor)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Ajustamos un poco el padding general
        ) {
            // --- Área para los mensajes (Estáticos por ahora) ---
            MessageBubble(text = "tienes dudas?", isAiMessage = true, modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.height(8.dp))

            MessageBubble(
                text = "• Hola Bienvenido a tu gestor de gastos\n• ¿Que tienes en mente?\n• ¿Necesitas ayuda con algo?",
                isAiMessage = true,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Espacio para que los mensajes empujen el área de entrada hacia abajo ---
            Spacer(Modifier.weight(1f))

            // --- Área de Entrada de Texto y Botón de Enviar ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Padding vertical para la fila de entrada
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Campo de Entrada de Texto
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    label = { Text("Ingresa tus dudas o escenario financiero que deseas preguntar") }, // Placeholder text más largo
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = InputFieldBackgroundColor,
                        unfocusedContainerColor = InputFieldBackgroundColor,
                        disabledContainerColor = InputFieldBackgroundColor,
                        errorContainerColor = InputFieldBackgroundColor,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = TextColorOnLight,
                        focusedLabelColor = PlaceholderTextColor,
                        unfocusedLabelColor = PlaceholderTextColor
                    )
                )

                // *** BOTÓN DE ENVIAR (Cambiado a ícono) ***
                Button( // Usamos Button para poder controlar el color de fondo fácilmente y la forma
                    onClick = {
                        // TODO: Implementar acción de enviar el mensaje
                        if (inputText.isNotBlank()) {
                            println("Mensaje enviado: $inputText") // Ejemplo
                            // onSendMessage(inputText) // Llama a la lambda si aplica
                            inputText = "" // Limpiar campo
                        }
                    },
                    modifier = Modifier
                        .size(56.dp), // Tamaño del área del botón
                    shape = RoundedCornerShape(28.dp), // Forma redondeada consistente
                    colors = ButtonDefaults.buttonColors(containerColor = SendButtonColor), // Color verde
                    contentPadding = PaddingValues(0.dp) // Elimina padding interno para que el icono ocupe más espacio
                ) {
                    Icon( // *** Usamos el composable Icon ***
                        imageVector = Icons.Default.Send, // *** El ícono de enviar ***
                        contentDescription = "Enviar Mensaje",
                        tint = TextColorOnDark // Color del ícono (blanco)
                    )
                }
            }
        }
    }
}

// --- Composable Auxiliar para las "Burbujas" de Mensaje (Igual que antes) ---
@Composable
fun MessageBubble(text: String, isAiMessage: Boolean, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MessageBubbleColor)
    ) {
        Text(
            text = text,
            color = TextColorOnDark,
            fontSize = 16.sp,
            modifier = Modifier.padding(12.dp)
        )
    }
}


// --- Cómo integrar en tu MainActivity --- (Igual que antes)
/*
package co.edu.unab.overa32.finanzasclaras // Tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Importa tu tema y la pantalla de IA
import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme
import co.edu.unab.overa32.finanzasclaras.IaScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanzasClarasTheme { // Aplica tu tema
                // Navegación
                IaScreen(
                    onBackClick = {
                        // TODO: Implementar navegación hacia atrás
                    }
                    // onSendMessage = { message -> ... }
                )
            }
        }
    }
}
*/