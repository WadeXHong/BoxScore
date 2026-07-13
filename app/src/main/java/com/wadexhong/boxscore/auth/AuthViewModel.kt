package com.wadexhong.boxscore.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                auth.signInWithEmailAndPassword(email, pass).await()
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                val msg = when (e) {
                    is FirebaseAuthWeakPasswordException -> "Weak password."
                    is FirebaseAuthInvalidCredentialsException -> "Wrong password."
                    is FirebaseAuthUserCollisionException -> "Account already exists."
                    else -> "Account does not exist or login failed."
                }
                _uiState.value = AuthUiState.Error(msg)
            }
        }
    }

    fun signUp(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                auth.createUserWithEmailAndPassword(email, pass).await()
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                val msg = when (e) {
                    is FirebaseAuthUserCollisionException -> "Account already exists."
                    else -> e.localizedMessage ?: "Sign up failed."
                }
                _uiState.value = AuthUiState.Error(msg)
            }
        }
    }
}
