package com.onear.doplusplus.ui.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onear.doplusplus.R

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MaterialTheme {
        LoginScreen(onNavigateBack = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                title = {
                    Text("登录")
                }, actions = {

                }, navigationIcon = {
                    IconButton(modifier = Modifier, onClick = {
                        onNavigateBack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "BACK")
                    }
                }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = stringResource(id = R.string.login_text),
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                rememberTextFieldState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text(stringResource(R.string.account_textbox))
                },
                leadingIcon = {
                    Icon(Icons.Filled.Person, "account")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedSecureTextField(
                rememberTextFieldState(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                label = {
                    Text(stringResource(R.string.password_textbox))
                },
                leadingIcon = {
                    Icon(Icons.Filled.Password, "account")
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Checkbox(false, onCheckedChange = {}, modifier = Modifier)
                Text(
                    stringResource(R.string.license_agreement_checkbox),
                    Modifier.align(Alignment.CenterVertically)
                )

            }
//            Row(Modifier.align(Alignment.Start).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.Start){
//                Checkbox(false, onCheckedChange = {}, modifier = Modifier)
//                Text(stringResource(R.string.license_agreement_checkbox), Modifier.align(Alignment.CenterVertically))
//            }


            Spacer(modifier = Modifier.height(20.dp))
            OutlinedIconButton(
                onClick = {},
                modifier = Modifier
                    .wrapContentWidth()
                    .width(100.dp),

                ) {
                Icon(Icons.AutoMirrored.Filled.Login, contentDescription = "login")
            }
        }
    }
}