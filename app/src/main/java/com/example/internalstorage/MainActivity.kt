package com.example.internalstorage

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.internalstorage.ui.theme.InternalStorageTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InternalStorageTheme {

                val textState = remember {
                    mutableStateOf("")
                }
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = textState.value)
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                        scope.launch {
                           textState.value = readFile(context)
                        }
                    }) {
                        Text("Read")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = {
                         scope.launch {
                             save(context)
                         }
                    }) {
                        Text("Save")
                    }
                }
                }
            }
        }
    }

private suspend fun save(context: Context){
    val text = "У лукоморья дуб зелёный;\n" +
            "Златая цепь на дубе том:\n" +
            "И днём и ночью кот учёный\n" +
            "Всё ходит по цепи кругом;\n" +
            "Идёт направо — песнь заводит,\n" +
            "Налево — сказку говорит.\n" +
            "Там чудеса: там леший бродит,"
    withContext(Dispatchers.IO){
        context.openFileOutput("test.txt",Context.MODE_PRIVATE).use {
            it.write(text.toByteArray())
        }
    }
}

private suspend fun readFile(context: Context) = withContext(Dispatchers.IO){
    try {
       context.openFileInput("test.txt").bufferedReader().useLines {
           lines -> lines.fold(" "){
               acc, s ->
               "$acc\n$s"
       }
       }

    } catch (e:IOException){
        e.printStackTrace()
        " "
    }
}


