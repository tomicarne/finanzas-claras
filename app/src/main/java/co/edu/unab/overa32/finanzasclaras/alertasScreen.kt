package co.edu.unab.overa32.finanzasclaras


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.* // Usamos Material 3 components
import androidx.compose.runtime.* // Para remember y mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import java.text.NumberFormat // Para formatear el saldo como moneda
import java.util.Locale // Para especificar la localización del formato
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(
    navController: NavHostController,
    alertList: List<Alert>,
    totalAmount: Double,
    currentAmount: Double,
    onAddAlert: (Double) -> Unit,
    onToggleAlert: (Int) -> Unit
) {
    val context = LocalContext.current

    // Notificación si el saldo actual baja de cualquier alerta activa
    val alertsSnapshot = remember(alertList) { alertList.toList() }

    LaunchedEffect(currentAmount, alertsSnapshot) {
        val triggered = alertsSnapshot.any { it.enabled && currentAmount < it.amount }
        if (triggered) {
            sendNotification(context, "¡Alerta!", "El saldo actual bajó de una alerta activa.")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6A5ACD)) // fondo
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
            Text("Alertas", Modifier.weight(1f), textAlign = TextAlign.Center, color = Color.White)
            IconButton(onClick = {
                navController.navigate("addAlert")
            }) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Green)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de alertas
        alertList.forEachIndexed { index, alert ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color(0xFF2D232E), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "$ ${alert.amount}", color = Color.White)
                Switch(checked = alert.enabled, onCheckedChange = { onToggleAlert(index) })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Saldos
        Column {
            InfoBox(title = "saldo total:", amount = totalAmount)
            Spacer(modifier = Modifier.height(16.dp))
            InfoBox(title = "saldo actual", amount = currentAmount)
        }
    }
}

@Composable
fun InfoBox(title: String, amount: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0D1B2A), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(title, color = Color.White)
            Text("$${amount}", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}


fun sendNotification(context: Context, title: String, content: String) {
    val channelId = "alert_channel"
    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId, "Alertas", NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(android.R.drawable.ic_dialog_alert)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    manager.notify(1, notification)
}
