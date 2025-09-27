package com.teobaranga.monica.setup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NoEncryption
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import monica.app.generated.resources.Res
import monica.app.generated.resources.insecure_alert_accept
import monica.app.generated.resources.insecure_alert_body1
import monica.app.generated.resources.insecure_alert_body2
import monica.app.generated.resources.insecure_alert_reject
import monica.app.generated.resources.insecure_alert_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsecureHttpBottomSheet(
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            TitleText()
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.insecure_alert_body1),
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(Res.string.insecure_alert_body2),
                style = MaterialTheme.typography.bodyMedium,
            )
            ActionButtonRow(
                onAccept = onAccept,
                onReject = onDismiss,
            )
        }
    }
}

@Composable
private fun TitleText() {
    val titleStyle = MaterialTheme.typography.headlineSmall
    val inlineIcon = InlineTextContent(
        placeholder = Placeholder(
            width = 32.sp,
            height = titleStyle.fontSize,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )
    ) {
        Icon(
            modifier = Modifier
                .padding(horizontal = 2.dp),
            imageVector = Icons.Outlined.NoEncryption,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
        )
    }
    BasicText(
        text = buildAnnotatedString {
            appendInlineContent("warning_icon")
            append(stringResource(Res.string.insecure_alert_title))
        },
        style = titleStyle,
        inlineContent = mapOf("warning_icon" to inlineIcon),
    )
}

@Composable
private fun ActionButtonRow(
    onAccept: () -> Unit,
    onReject: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 24.dp),
    ) {
        TextButton(
            onClick = onReject,
        ) {
            Text(stringResource(Res.string.insecure_alert_reject))
        }
        Spacer(
            modifier = Modifier.weight(1f),
        )
        TextButton(
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            onClick = onAccept,
        ) {
            Text(stringResource(Res.string.insecure_alert_accept))
        }
    }
}

@Preview
@Composable
private fun InsecureHttpBottomSheetPreview() {
    MonicaTheme {
        InsecureHttpBottomSheet(
            onDismiss = { },
            onAccept = { },
        )
    }
}
