package com.vinyleaf.musicplayer.presentation.screen.register

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vinyleaf.musicplayer.R
import com.vinyleaf.musicplayer.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Enhanced gradient colors for a more modern look
    val gradientColors = listOf(
        GradientStart,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
        GradientEnd.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.background
    )

    // Handle registration success
    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image/Pattern
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(gradientColors)
                )
        ) {
            // Add decorative elements similar to Animity
            DecorativeBackground()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Enhanced Logo Section
            LogoSection()

            Spacer(modifier = Modifier.height(40.dp))

            // Registration Form Card with Glass Effect
            RegistrationCard(
                email = email,
                username = username,
                password = password,
                confirmPassword = confirmPassword,
                showPassword = showPassword,
                showConfirmPassword = showConfirmPassword,
                isLoading = uiState.isLoading,
                onEmailChange = { email = it },
                onUsernameChange = { username = it },
                onPasswordChange = { password = it },
                onConfirmPasswordChange = { confirmPassword = it },
                onShowPasswordToggle = { showPassword = !showPassword },
                onShowConfirmPasswordToggle = { showConfirmPassword = !showConfirmPassword },
                onRegisterClick = {
                    viewModel.register(email, username, password, confirmPassword)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login Link
            TextButton(
                onClick = onLoginClick,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Already have an account? Sign In",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // Error handling
        uiState.error?.let { error ->
            LaunchedEffect(error) {
                // Show snackbar or handle error
            }
        }
    }
}

@Composable
private fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        // Floating circles similar to Animity design
        Circle(
            modifier = Modifier
                .offset(x = (-50).dp, y = 100.dp)
                .size(200.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )

        Circle(
            modifier = Modifier
                .offset(x = 250.dp, y = 300.dp)
                .size(150.dp),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)
        )

        Circle(
            modifier = Modifier
                .offset(x = (-30).dp, y = 600.dp)
                .size(180.dp),
            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.06f)
        )

        Circle(
            modifier = Modifier
                .offset(x = 200.dp, y = 800.dp)
                .size(120.dp),
            color = MusicAccent.copy(alpha = 0.05f)
        )
    }
}

@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color)
    )
}

@Composable
private fun LogoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated logo container
        Card(
            modifier = Modifier.size(100.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Vinyleaf Logo",
                    modifier = Modifier.size(50.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "Join Vinyleaf to start streaming your music",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun RegistrationCard(
    email: String,
    username: String,
    password: String,
    confirmPassword: String,
    showPassword: Boolean,
    showConfirmPassword: Boolean,
    isLoading: Boolean,
    onEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onShowPasswordToggle: () -> Unit,
    onShowConfirmPasswordToggle: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
            // Email Field
            EnhancedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = "Email",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username Field
            EnhancedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = "Username",
                icon = Icons.Default.Person
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            EnhancedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = "Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                showPassword = showPassword,
                onShowPasswordToggle = onShowPasswordToggle
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            EnhancedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = "Confirm Password",
                icon = Icons.Default.Lock,
                isPassword = true,
                showPassword = showConfirmPassword,
                onShowPasswordToggle = onShowConfirmPasswordToggle
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Create Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onShowPasswordToggle: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = if (isPassword) {
            {
                IconButton(onClick = { onShowPasswordToggle?.invoke() }) {
                    Icon(
                        if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            }
        } else null,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    )
}
