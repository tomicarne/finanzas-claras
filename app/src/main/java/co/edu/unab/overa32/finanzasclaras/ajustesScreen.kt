package co.edu.unab.overa32.finanzasclaras


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.* // Para remember y mutableStateOf (si un item necesita estado local)
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// --- 1. Clase Sellada para Representar Tipos de Items de Ajustes ---
// Esto nos ayuda a tener diferentes tipos de filas en nuestra lista
sealed class SettingsItem {
    data class Header(val title: String) : SettingsItem() // Un encabezado de sección
    // Un item clickeable (para navegación o acción), puede tener descripción
    data class ClickableItem(val id: String, val title: String, val description: String? = null, val onClick: () -> Unit) : SettingsItem()
    // Un item con un interruptor (toggle)
    data class ToggleItem(val id: String, val title: String, val description: String? = null, val isEnabled: Boolean, val onToggle: (Boolean) -> Unit) : SettingsItem()
    // Podrías añadir otros tipos si necesitas (ej: TextInputItem)
}

// --- 2. Datos de Ejemplo para la Lista de Ajustes ---
// En una app real, el estado de ToggleItem vendría de tu ViewModel/estado central
val sampleSettingsList: List<SettingsItem> = listOf(
    SettingsItem.Header("General"),
    SettingsItem.ClickableItem("currency", "Cambiar Moneda", "COP - Peso Colombiano", onClick = { /* TODO: Navegar a cambiar moneda */ }),
    SettingsItem.ClickableItem("language", "Idioma", "Español", onClick = { /* TODO: Navegar a cambiar idioma */ }),

    SettingsItem.Header("Notificaciones"),
    // Ejemplo de ToggleItem con estado mutable local solo para el preview
    SettingsItem.ToggleItem("expense_alerts", "Recibir Alertas de Gastos", isEnabled = true, onToggle = { isEnabled ->
        // TODO: Implementar lógica para guardar el estado de la alerta (ej. en SharedPreferences, ViewModel)
        println("Recibir Alertas cambiado a: $isEnabled")
    }),
    SettingsItem.ClickableItem("notification_sound", "Sonido de Notificación", onClick = { /* TODO: Navegar a ajustes de sonido */ }),

    SettingsItem.Header("Apariencia"),
    SettingsItem.ToggleItem("dark_mode", "Modo Oscuro", isEnabled = false, onToggle = { isEnabled ->
        // TODO: Implementar lógica para cambiar el tema de la app
        println("Modo Oscuro cambiado a: $isEnabled")
    }),

    SettingsItem.Header("Cuenta"),
    SettingsItem.ClickableItem("edit_profile", "Editar Perfil", onClick = { /* TODO: Navegar a editar perfil */ }),
    SettingsItem.ClickableItem("logout", "Cerrar Sesión", onClick = { /* TODO: Implementar cerrar sesión */ }), // Podrías darle un estilo diferente

    SettingsItem.Header("Acerca de"),
    // Un item clickeable sin acción, solo para mostrar info (la descripción es el valor)
    SettingsItem.ClickableItem("app_version", "Versión de la Aplicación", "1.0.0", onClick = { /* Opcional: Mostrar diálogo de detalles */ }),
    SettingsItem.ClickableItem("terms", "Términos y Condiciones", onClick = { /* TODO: Abrir URL o navegar a pantalla */ }),
    SettingsItem.ClickableItem("privacy", "Política de Privacidad", onClick = { /* TODO: Abrir URL o navegar a pantalla */ })
)

// --- 3. Composable de la Pantalla de Ajustes ---
@OptIn(ExperimentalMaterial3Api::class) // Para TopAppBar
@Composable
fun AjustesScreen(
    myNavController: NavHostController,
    onBackClick: () -> Unit, // Acción para el botón de volver){}
    function: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes", color = MaterialTheme.colorScheme.onSurface) }, // Color de texto desde el tema
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onSurface) // Color de icono desde el tema
                    }
                },
                // Puedes añadir acciones aquí si es necesario (ej: buscar ajustes)
                // actions = { IconButton(...) { Icon(...) } }
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface) // Color de fondo de la barra desde el tema
            )
        }
    ) { paddingValues ->
        // Usamos LazyColumn para la lista de ajustes, eficiente para listas largas
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues) // Aplica el padding del Scaffold
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Color de fondo general desde el tema
        ) {
            // items es un builder que nos permite manejar diferentes tipos de elementos
            items(sampleSettingsList) { item ->
                when (item) {
                    is SettingsItem.Header -> SettingsHeader(title = item.title)
                    is SettingsItem.ClickableItem -> ClickableSettingItem(item = item)
                    is SettingsItem.ToggleItem -> ToggleSettingItem(item = item)
                }
                // Opcional: Añadir un Divider después de cada item (excepto headers)
                // if (item !is SettingsItem.Header) {
                //     Divider(color = Color.LightGray, thickness = 0.5.dp)
                // }
            }
        }
    }
}

// --- 4. Composable para un Encabezado de Sección ---
@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall, // Estilo de texto para encabezados (ajusta según tu tema)
        color = MaterialTheme.colorScheme.primary, // Color primario para los encabezados
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Padding
            .padding(top = 16.dp) // Espacio adicional arriba del primer encabezado de una sección
    )
}

// --- 5. Composable para un Item de Ajuste Clickeable ---
@Composable
fun ClickableSettingItem(item: SettingsItem.ClickableItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick) // Hace toda la fila clickeable
            .padding(horizontal = 16.dp, vertical = 12.dp), // Padding interno del item
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Distribuye el espacio
    ) {
        Column(
            modifier = Modifier.weight(1f) // Ocupa la mayor parte del espacio
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge, // Estilo de texto para títulos de items
                color = MaterialTheme.colorScheme.onSurface // Color de texto principal del tema
            )
            item.description?.let { description -> // Si hay descripción, la muestra
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall, // Estilo de texto más pequeño para descripción
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Color de texto secundario del tema
                )
            }
        }

        // Ícono de flecha si es clickeable y lleva a otra pantalla (opcional)
        if (item.onClick != { /* TODO: ... */ } && item.id != "app_version") { // No mostrar flecha si es solo para mostrar info o acción directa
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward, // Ícono de flecha
                contentDescription = null, // No necesita descripción para accesibilidad si es solo visual
                tint = MaterialTheme.colorScheme.onSurfaceVariant // Color del icono
            )
        }
        // Si es el item de Logout, podrías darle un color diferente al texto (ej. rojo)
        // if (item.id == "logout") { /* Aplicar color rojo al Text */ }
    }
    Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp) // Separador después del item
}

// --- 6. Composable para un Item de Ajuste con Interruptor ---
@Composable
fun ToggleSettingItem(item: SettingsItem.ToggleItem) {
    // Estado local para el switch. En una app real, este estado vendría de un ViewModel
    var isChecked by remember { mutableStateOf(item.isEnabled) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { isChecked = !isChecked; item.onToggle(isChecked) }) // Hace la fila clickeable y alterna el switch
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(end = 16.dp) // Espacio al final de la columna de texto
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            item.description?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = isChecked, // Usa el estado local
            onCheckedChange = { enabled ->
                isChecked = enabled // Actualiza el estado local
                item.onToggle(enabled) // Llama a la lambda para notificar al exterior
            },
            // Puedes personalizar los colores del Switch aquí si no te gustan los del tema
            // colors = SwitchDefaults.colors(...)
        )
    }
    Divider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp) // Separador después del item
}


// --- Cómo integrar en tu MainActivity ---
/*
package co.edu.unab.overa32.finanzasclaras // Tu paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// Importa tu tema y la pantalla de ajustes
import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme
import co.edu.unab.overa32.finanzasclaras.AjustesScreen // *** Importa la pantalla de ajustes ***

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanzasClarasTheme { // Aplica tu tema
                // Aquí deberías tener la navegación de tu app (ej: usando NavHostController)
                AjustesScreen(
                    onBackClick = {
                        // TODO: Implementar navegación hacia atrás
                        // navigator.popBackStack() // Ejemplo con Navigation Compose
                    }
                )
            }
        }
    }
}
*/