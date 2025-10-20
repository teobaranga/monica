package com.teobaranga.monica.contacts.detail.activities.edit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.contacts.detail.ContactDetailRoute
import com.teobaranga.monica.core.ui.menu.DropdownMenu
import com.teobaranga.monica.core.ui.menu.rememberDropdownMenuState
import com.teobaranga.monica.core.ui.navigation.LocalNavigator
import com.teobaranga.monica.useravatar.UserAvatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantsRow(
    state: ParticipantsState,
    modifier: Modifier = Modifier.Companion,
) {
    if (state.participants.isNotEmpty()) {
        val navigator = LocalNavigator.current
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            for (contact in state.participants) {
                item(contact.contactId) {
                    val dropdownMenuState = rememberDropdownMenuState()
                    DropdownMenu(
                        modifier = Modifier
                            .animateItem(),
                        state = dropdownMenuState,
                        anchor = {
                            UserAvatar(
                                modifier = Modifier
                                    .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                    .size(64.dp),
                                userAvatar = contact.avatar,
                            )
                        },
                        menuItems = {
                            Text(
                                modifier = Modifier
                                    .padding(MenuDefaults.DropdownMenuItemContentPadding)
                                    .padding(vertical = 8.dp),
                                text = contact.name,
                                style = MaterialTheme.typography.labelMedium,
                            )

                            ViewProfileDropdownMenuItem(
                                onClick = {
                                    dropdownMenuState.expanded = false
                                    navigator.navigate(ContactDetailRoute(contact.contactId))
                                },
                            )

                            DeleteParticipantDropdownMenuItem(
                                onClick = {
                                    dropdownMenuState.expanded = false
                                    state.participants.remove(contact)
                                }
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ViewProfileDropdownMenuItem(
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(text = "View profile")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.PersonPin,
                contentDescription = "View participant profile",
            )
        },
        onClick = onClick,
    )
}

@Composable
private fun DeleteParticipantDropdownMenuItem(
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        text = {
            Text(text = "Remove")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.PersonRemove,
                contentDescription = "Remove participant",
            )
        },
        onClick = onClick,
    )
}
