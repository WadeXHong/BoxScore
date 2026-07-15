package com.wadexhong.boxscore.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wadexhong.boxscore.R

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            onLoginSuccess()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_main),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_main),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val shape = RoundedCornerShape(25.dp)
            val btnColor = colorResource(id = R.color.colorButtonInMain)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = shape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = btnColor
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = shape,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = btnColor
                ),
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.login(email.trim(), password) },
                    shape = shape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("LOGIN", color = colorResource(id = R.color.colorTextInMainButton))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { viewModel.signUp(email.trim(), password) },
                    shape = shape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = btnColor),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("SIGN UP", color = colorResource(id = R.color.colorTextInMainButton))
                }
            }

            if (uiState is AuthUiState.Loading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            } else if (uiState is AuthUiState.Error) {
                Spacer(modifier = Modifier.height(16.dp))
                Text((uiState as AuthUiState.Error).message, color = Color.Red)
            }
        }
    }
}
