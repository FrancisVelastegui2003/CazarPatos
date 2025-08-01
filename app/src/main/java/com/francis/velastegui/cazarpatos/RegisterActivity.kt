package com.francis.velastegui.cazarpatos

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var buttonBackToLogin: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.editTextRegisterEmail)
        editTextPassword = findViewById(R.id.editTextRegisterPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin)

        auth = FirebaseAuth.getInstance()

        buttonSignUp.setOnClickListener {
            validarYRegistrarUsuario()
        }

        buttonBackToLogin.setOnClickListener {
            finish() // Regresa al LoginActivity
        }
    }

    private fun validarYRegistrarUsuario() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        // Validaciones
        if (email.isEmpty()) {
            editTextEmail.error = "El email es obligatorio"
            editTextEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "El formato del email es inválido"
            editTextEmail.requestFocus()
            return
        }

        if (password.length < 8) {
            editTextPassword.error = "La contraseña debe tener al menos 8 caracteres"
            editTextPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            editTextConfirmPassword.error = "Las contraseñas no coinciden"
            editTextConfirmPassword.requestFocus()
            return
        }

        // Registro con Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Cierra pantalla de registro
                } else {
                    // Error
                    val mensaje = task.exception?.message ?: "Error desconocido"
                    Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
                }
            }
    }
}
