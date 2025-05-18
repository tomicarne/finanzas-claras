package co.edu.unab.overa32.finanzasclaras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import co.edu.unab.overa32.finanzasclaras.ui.theme.FinanzasClarasTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
data class Alert(
    val amount: Double,
    val enabled: Boolean
)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alerts = mutableStateListOf<Alert>()
        var totalAmount by mutableStateOf(0.0)
        var currentAmount by mutableStateOf(0.0)
        setContent {
            FinanzasClarasTheme {

                val myNavController = rememberNavController()
                val myStartDestination = "main"
                val context = LocalContext.current
                val saldoDataStore = remember { SaldoDataStore(context) }
                var saldo by remember { mutableStateOf(0.00) }

                LaunchedEffect(Unit) {
                    saldoDataStore.getSaldo.collect { savedSaldo ->
                        saldo = savedSaldo
                    }
                }
                NavHost(
                    navController = myNavController,
                    startDestination = myStartDestination,
                    modifier = Modifier.fillMaxSize()

                ) {
                    composable("main") {
                        PantallaPrincipalUI(saldo,myNavController)
                    }
                    composable("aiScreen") {
                        IaScreen(myNavController,onBackClick = { myNavController.popBackStack() }) { }
                    }
                    composable("gastos") {
                        MainScreenWithState()
                    }
                    composable("alerts") {
                        AlertScreen(
                            myNavController,
                            alertList = alerts,
                            totalAmount = totalAmount,
                            currentAmount = currentAmount,
                            onAddAlert = { newAmount ->
                                alerts.add(Alert(newAmount, true))
                            },
                            onToggleAlert = { index ->
                                alerts[index] = alerts[index].copy(enabled = !alerts[index].enabled)
                            }
                        )
                    }
                    

                    composable("ajustes") {
                        AjustesScreen(
                            myNavController,
                            onBackClick = { myNavController.popBackStack() }) { }
                    }
                    composable("addGasto") {
                        AddGastoScreen(navController = myNavController)
                    }
                    composable("tablaGastos") {
                        TablaGastosScreen(myNavController) { nuevoSaldo ->
                            saldo = nuevoSaldo
                            myNavController.navigate("alerts")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FinanzasClarasTheme {
        Greeting("Android")
    }
}