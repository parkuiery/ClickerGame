package com.uiel.clickergame.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun NicknameTextField(
    modifier: Modifier = Modifier,
    initEdit: Boolean,
    text: String,
    onTextChange: (String) -> Unit,
    onCheckClick: () -> Unit,
) {
    var isEdit by remember { mutableStateOf(initEdit) }
    LaunchedEffect(initEdit) {
        isEdit = initEdit
    }
    val icon = if (initEdit || isEdit) Icons.Default.Check else Icons.Default.Edit
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (initEdit || isEdit) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true,
            )
        } else {
            Text(
                text = text
            )
        }
        IconButton(onClick = {
            if (isEdit) onCheckClick()
            isEdit = !isEdit
        }) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }
    }
}