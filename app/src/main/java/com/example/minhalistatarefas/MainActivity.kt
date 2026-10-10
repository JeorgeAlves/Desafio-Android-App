package com.example.minhalistatarefas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Tarefa(
    val id: Int,
    val titulo: String,
    val concluida: Boolean = false
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ListaTarefasApp()
            }
        }
    }
}

@Composable
fun ListaTarefasApp() {
    val tarefas = remember {
        mutableStateListOf<Tarefa>()
    }

    var textoTarefa by remember {
        mutableStateOf("")
    }

    var proximoId by remember {
        mutableStateOf(1)
    }

    val concluidas = tarefas.count { it.concluida }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingInterno ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .padding(20.dp)
        ) {
            Text(
                text = "Minhas tarefas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${tarefas.size} tarefas • $concluidas concluídas",
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textoTarefa,
                    onValueChange = { textoTarefa = it },
                    label = { Text("Nova tarefa") },
                    placeholder = { Text("Ex.: Estudar Kotlin") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (textoTarefa.isNotBlank()) {
                                tarefas.add(
                                    Tarefa(
                                        id = proximoId,
                                        titulo = textoTarefa.trim()
                                    )
                                )
                                proximoId++
                                textoTarefa = ""
                            }
                        }
                    ),
                    modifier = Modifier.weight(1f)
                )

                Button(
                    onClick = {
                        if (textoTarefa.isNotBlank()) {
                            tarefas.add(
                                Tarefa(
                                    id = proximoId,
                                    titulo = textoTarefa.trim()
                                )
                            )
                            proximoId++
                            textoTarefa = ""
                        }
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Adicionar")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (tarefas.isEmpty()) {
                Text(
                    text = "Nenhuma tarefa ainda. Adicione a primeira!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = tarefas,
                        key = { it.id }
                    ) { tarefa ->

                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = tarefa.concluida,
                                    onCheckedChange = { marcada ->
                                        val indice = tarefas.indexOfFirst {
                                            it.id == tarefa.id
                                        }

                                        if (indice != -1) {
                                            tarefas[indice] =
                                                tarefa.copy(
                                                    concluida = marcada
                                                )
                                        }
                                    }
                                )

                                Text(
                                    text = tarefa.titulo,
                                    modifier = Modifier.weight(1f),
                                    textDecoration =
                                        if (tarefa.concluida) {
                                            TextDecoration.LineThrough
                                        } else {
                                            TextDecoration.None
                                        }
                                )

                                Button(
                                    onClick = {
                                        tarefas.removeAll {
                                            it.id == tarefa.id
                                        }
                                    }
                                ) {
                                    Text("Excluir")
                                }
                            }

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}