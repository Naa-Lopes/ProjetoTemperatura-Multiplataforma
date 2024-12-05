package com.example.projetotemperatura

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetotemperatura.ui.theme.ProjetoTemperaturaTheme
import com.google.firebase.firestore.FirebaseFirestore

// Modelo de dados atualizado
data class SensorData(
    val date: String = "",
    val time: String = "",
    val temperature: Double = 0.0,
    val humidity: Double = 0.0
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetoTemperaturaTheme {
                var sensorDataList by remember { mutableStateOf(listOf<SensorData>()) }

                // Buscar dados no Firestore
                LaunchedEffect(Unit) {
                    val db = FirebaseFirestore.getInstance()
                    db.collection("SensorData")
                        .get()
                        .addOnSuccessListener { result ->
                            val data = result.mapNotNull { doc ->
                                try {
                                    // Extração manual dos campos para conversão segura
                                    val date = doc.getString("date") ?: ""
                                    val time = doc.getString("time") ?: ""
                                    val temperature = doc.getString("temp")?.toDoubleOrNull() ?: 0.0
                                    val humidity = doc.getString("hmd")?.toDoubleOrNull() ?: 0.0

                                    // Criar objeto SensorData
                                    SensorData(date, time, temperature, humidity)
                                } catch (e: Exception) {
                                    Log.e("Firebase", "Erro ao processar documento: ${e.message}")
                                    null
                                }
                            }
                            sensorDataList = data
                            Log.d("Firebase", "Dados carregados: $sensorDataList")
                        }
                        .addOnFailureListener { e ->
                            sensorDataList = emptyList() // Tratar erros conforme necessário
                            Log.e("Firebase", "Erro ao buscar dados: ${e.message}")
                        }
                }

                // Exibir os dados em uma lista com um título
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Nanath Temperatura",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color(0xFF0D47A1) // Azul escuro
                            )
                        )
                    },
                    containerColor = Color(0xFFE3F2FD) // Azul claro de fundo
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(sensorDataList) { data ->
                            SensorDataItem(data)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SensorDataItem(data: SensorData) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFBBDEFB), // Azul médio
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Data: ${data.date}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hora: ${data.time}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Temperatura: ${data.temperature} °C",
                    fontSize = 16.sp
                )
                Text(
                    text = "Umidade: ${data.humidity} %",
                    fontSize = 16.sp
                )
            }
        }
    }
}
