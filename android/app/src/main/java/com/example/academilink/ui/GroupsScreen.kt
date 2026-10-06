package com.example.academilink.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.academilink.viewmodel.MainViewModel

@Composable
fun GroupsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val groups by viewModel.groups.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val token by viewModel.token.collectAsState(initial = "")
    val currentUser by viewModel.user.observeAsState()

    var institution by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }

    var newGroupName by remember { mutableStateOf("") }
    var newGroupDescription by remember { mutableStateOf("") }
    var newGroupInstitution by remember { mutableStateOf("") }
    var newGroupCourse by remember { mutableStateOf("") }
    var newGroupCategory by remember { mutableStateOf("") }

    var editingGroupId by remember { mutableStateOf<String?>(null) }
    var editName by remember { mutableStateOf("") }
    var editDescription by remember { mutableStateOf("") }
    var editInstitution by remember { mutableStateOf("") }
    var editCourse by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }



    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {
            Text("Study Groups")
        }

        item {
            TextButton(
                onClick = onBack
            ) {
                Text("Back to Profile")
            }
        }

        item {
            Text("Search Groups")
        }

        item {
            OutlinedTextField(
                value = institution,
                onValueChange = { institution = it },
                label = { Text("Institution") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = course,
                onValueChange = { course = it },
                label = { Text("Course") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    viewModel.searchGroups(
                        institution = institution,
                        course = course,
                        category = category
                    )
                }
            ) {
                Text("Search")
            }
        }

        item {
            TextButton(
                onClick = {
                    institution = ""
                    course = ""
                    category = ""
                    viewModel.loadGroups()
                }
            ) {
                Text("Show All")
            }
        }

        item {
            Text("Create New Group")
        }

        item {
            OutlinedTextField(
                value = newGroupName,
                onValueChange = { newGroupName = it },
                label = { Text("Group Name") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = newGroupDescription,
                onValueChange = { newGroupDescription = it },
                label = { Text("Description") }
            )
        }

        item {
            OutlinedTextField(
                value = newGroupInstitution,
                onValueChange = { newGroupInstitution = it },
                label = { Text("Institution") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = newGroupCourse,
                onValueChange = { newGroupCourse = it },
                label = { Text("Course") },
                singleLine = true
            )
        }

        item {
            OutlinedTextField(
                value = newGroupCategory,
                onValueChange = { newGroupCategory = it },
                label = { Text("Category") },
                singleLine = true
            )
        }

        item {
            Button(
                onClick = {
                    if (token.isNotEmpty()) {
                        viewModel.createGroup(
                            token = token,
                            name = newGroupName,
                            description = newGroupDescription,
                            institution = newGroupInstitution,
                            course = newGroupCourse,
                            category = newGroupCategory
                        )
                    }
                }
            ) {
                Text("Create Group")
            }
        }

        item {
            if (isLoading) {
                Text("Loading groups...")
            }
        }

        error?.let { message ->
            item {
                Text(message)
            }
        }

        items(groups) { group ->
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (editingGroupId == group.id) {

                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Group Name") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = editDescription,
                            onValueChange = { editDescription = it },
                            label = { Text("Description") }
                        )

                        OutlinedTextField(
                            value = editInstitution,
                            onValueChange = { editInstitution = it },
                            label = { Text("Institution") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = editCourse,
                            onValueChange = { editCourse = it },
                            label = { Text("Course") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = editCategory,
                            onValueChange = { editCategory = it },
                            label = { Text("Category") },
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (token.isNotEmpty()) {
                                    viewModel.updateGroup(
                                        token = token,
                                        groupId = group.id,
                                        name = editName,
                                        description = editDescription,
                                        institution = editInstitution,
                                        course = editCourse,
                                        category = editCategory
                                    )

                                    editingGroupId = null
                                }
                            }
                        ) {
                            Text("Save Changes")
                        }

                        TextButton(
                            onClick = {
                                editingGroupId = null
                            }
                        ) {
                            Text("Cancel")
                        }

                    } else {

                        Text(group.name)
                        Text("Institution: ${group.institution}")
                        Text("Course: ${group.course}")
                        Text("Category: ${group.category}")

                        val isOwner = group.ownerId.id == currentUser?.id
                        val isMember = group.members.any { member ->
                            member.id == currentUser?.id
                        }

                        if (isOwner) {
                            Button(
                                onClick = {
                                    editingGroupId = group.id
                                    editName = group.name
                                    editDescription = group.description
                                    editInstitution = group.institution
                                    editCourse = group.course
                                    editCategory = group.category
                                }
                            ) {
                                Text("Edit")
                            }

                            Button(
                                onClick = {
                                    if (token.isNotEmpty()) {
                                        viewModel.deleteGroup(
                                            token = token,
                                            groupId = group.id
                                        )
                                    }
                                }
                            ) {
                                Text("Delete")
                            }

                        } else if (isMember) {

                            Button(
                                onClick = {
                                    if (token.isNotEmpty()) {
                                        viewModel.leaveGroup(
                                            token = token,
                                            groupId = group.id
                                        )
                                    }
                                }
                            ) {
                                Text("Leave Group")
                            }

                        } else {

                            Button(
                                onClick = {
                                    if (token.isNotEmpty()) {
                                        viewModel.joinGroup(
                                            token = token,
                                            groupId = group.id
                                        )
                                    }
                                }
                            ) {
                                Text("Join Group")
                            }

                        }
                    }
                }
            }
        }
    }
}