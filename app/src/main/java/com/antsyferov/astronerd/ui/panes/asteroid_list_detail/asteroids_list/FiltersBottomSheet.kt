package com.antsyferov.astronerd.ui.panes.asteroid_list_detail.asteroids_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.antsyferov.astronerd.R
import com.antsyferov.astronerd.ui.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date

val formatter = SimpleDateFormat("yyyy-MM-dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAddFilter: (Filter) -> Unit,
    filters: List<Filter>,
    range: ClosedFloatingPointRange<Float>
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)) {
            val isDangerous = filters.filterIsInstance<Filter.Dangerous>().firstOrNull()?.isDangerous
            Text(
                text = "Is dangerous",
                style = AppTheme.typography.bold16
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Yes",
                    style = AppTheme.typography.regular14
                )
                RadioButton(
                    selected = isDangerous == true,
                    onClick = { onAddFilter.invoke(Filter.Dangerous(true)) }
                )
                Spacer(modifier = Modifier.width(32.dp))
                Text(
                    text = "No",
                    style = AppTheme.typography.regular14
                )
                RadioButton(
                    selected = isDangerous == false,
                    onClick = { onAddFilter.invoke(Filter.Dangerous(false))  }
                )
            }

            Text(
                text = "By name",
                style = AppTheme.typography.bold16
            )
            Spacer(modifier = Modifier.height(2.dp))
            var name by remember {
                mutableStateOf(filters.filterIsInstance<Filter.Name>().firstOrNull()?.name ?: "")
            }
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it.trim()
                    onAddFilter.invoke(Filter.Name(name))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By orbiting body",
                style = AppTheme.typography.bold16
            )
            Spacer(modifier = Modifier.height(2.dp))
            var body by remember {
                mutableStateOf(filters.filterIsInstance<Filter.Orbiting>().firstOrNull()?.body ?: "")
            }
            OutlinedTextField(
                value = body,
                onValueChange = {
                    body = it.trim()
                    onAddFilter.invoke(Filter.Orbiting(body))
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            var showDialog by remember { mutableStateOf(false) }
            val dateState = rememberDatePickerState()
            LaunchedEffect(true) {
                filters.filterIsInstance<Filter.Date>().firstOrNull()?.date?.let {
                    dateState.selectedDateMillis = formatter.parse(it)?.time
                }
            }

            Text(
                text = "By date",
                style = AppTheme.typography.bold16
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                }
                dateState.selectedDateMillis?.let {
                    Text(
                        text = formatter.format(Date(it)),
                        style = AppTheme.typography.regular14
                    )
                }
            }
            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                dateState.selectedDateMillis?.let {
                                    onAddFilter.invoke(Filter.Date(formatter.format(Date(it))))
                                }
                            }
                        ) {
                            Text(text = "OK")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                dateState.selectedDateMillis = null
                            }
                        ) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = dateState,
                        showModeToggle = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By diameter",
                style = AppTheme.typography.bold16
            )
            var sliderPosition by remember { mutableStateOf(range) }
            RangeSlider(
                value = sliderPosition,
                onValueChange = { range -> sliderPosition = range },
                valueRange = range,
                onValueChangeFinished = {
                    onAddFilter.invoke(Filter.Diameter(sliderPosition))
                },
            )

            Text(text = stringResource(id = R.string.diameter_range, sliderPosition.start, sliderPosition.endInclusive))
            Spacer(modifier = Modifier.height(16.dp))

        }

    }

}