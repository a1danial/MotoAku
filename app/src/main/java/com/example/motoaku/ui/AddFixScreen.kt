package com.example.motoaku.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.motoaku.R
import com.example.motoaku.TestTags.ADDFIX_INPUTCOSTPRICE
import com.example.motoaku.ViewModel
import com.example.motoaku.database.fix.Fix
import com.example.motoaku.database.motorcycle.Motorcycle
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@ExperimentalMaterial3Api
@Composable
fun AddFixScreen(
    navController: NavHostController,
    motoId: Int,
    vm: ViewModel = hiltViewModel()
) {
    var part by remember { mutableStateOf("") }
    var partCheck by remember { mutableStateOf(false) } // TODO error
    var dateStart by remember { mutableStateOf(Date(0)) }
    var dateEnd by remember { mutableStateOf(Date(0)) }
    var dateEndFirst by remember { mutableStateOf(true) }
    var odoStart by remember { mutableStateOf("") }
    var odoEnd by remember { mutableStateOf("") }
    var costPrice by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }

    val mCalendar = Calendar.getInstance()
    val modifier = Modifier.fillMaxWidth()

    LaunchedEffect(Unit) {
        vm.addFixScreenInit(motoId)
        dateStart = Date.from(LocalDateTime.of(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH),0,0,0).toInstant(ZoneOffset.of("Z")))
    }

    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(top = 30.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BrandField(vm.motoTracker, vm.MotoList, modifier) {
            vm.motoTracker = it
        }
        GenericTextField(
            label = R.string.fix_part,
            data = part,
            modifier = modifier,
            isError = partCheck
        ) {
            part = it
            if (part.isNotEmpty()) {
                partCheck = false
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            DateField(
                label = R.string.fix_date_start,
                dateDate = SimpleDateFormat("EEE d MMM yyyy").format(dateStart),
                mCalendar = mCalendar,
                modifier = modifier.weight(1f),
                updateDate = { year, month, day ->
                    dateStart = Date.from(LocalDateTime.of(year,month+1,day,0,0,0)
                        .toInstant(ZoneOffset.of("Z")))
                }
            )
            DateField(
                label = R.string.fix_date_end,
                dateDate = if (dateEndFirst) "" else SimpleDateFormat("EEE d MMM yyyy").format(dateEnd),
                mCalendar = mCalendar,
                modifier = modifier.weight(1f),
                firstChange = { dateEndFirst = false },
                updateDate = { year, month, day ->
                    dateEnd = Date.from(LocalDateTime.of(year,month+1,day,0,0,0)
                        .toInstant(ZoneOffset.of("Z")))
                }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            GenericNumberField(R.string.fix_odo_start,odoStart,modifier.weight(1f),KeyboardOptions(keyboardType = KeyboardType.Number)) {odoStart = digitDecimalFilter(it)}
            GenericNumberField(R.string.fix_odo_end,odoEnd,modifier.weight(1f),KeyboardOptions(keyboardType = KeyboardType.Number)) {odoEnd = digitDecimalFilter(it)}
        }
        GenericNumberField(
            R.string.fix_cost_amount,
            costPrice,
            modifier.testTag(ADDFIX_INPUTCOSTPRICE),
            KeyboardOptions(keyboardType = KeyboardType.Number)
        ) {costPrice = digitDecimalFilter(it)}
        GenericTextField(R.string.fix_description,comment,modifier) {comment = it}
        Button(
            onClick = {
                val isPartInputEmpty = part.isEmpty()
                if (isPartInputEmpty) {
                    partCheck = true
                } else {
                    vm.addFix(
                        Fix(
                            motoId = vm.motoTracker.mId,
                            dateStart = dateStart,
                            part = part.trim(),
                            dateEnd = if (!dateEndFirst) dateEnd else null,
                            odoStart = if (odoStart.isNotEmpty()) odoStart.toDouble() else null,
                            odoEnd = if (odoEnd.isNotEmpty()) odoEnd.toDouble() else null,
                            costPrice = if (costPrice.isNotEmpty()) costPrice.toDouble() else null,
                            costCurrency = null,
                            comment = if (comment.isNotEmpty()) comment.trim() else null
                        )
                    )
                    navController.popBackStack()
                }
            }) {
            Text(text = "Add Fix", color = Color.White)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun BrandField(
    selectedMoto: Motorcycle,
    motoList: List<Motorcycle>,
    modifier: Modifier,
    onSelect: (Motorcycle) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var rowSize by remember { mutableStateOf(Size.Zero) }
    Box {
        TextField(
            enabled = false,
            value = selectedMoto.brand+" - "+selectedMoto.model,
            onValueChange = {},
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    rowSize = coordinates.size.toSize()
                }
                .clickable {
                    expanded = true
                },
            leadingIcon = {
                if (selectedMoto.imageRes != null) {
                    Image(painterResource(selectedMoto.imageRes.imageRes),
                        null,
                        Modifier.size(24.dp)) } },
            label = { Text(text = "Moto") },
            colors = TextFieldDefaults.textFieldColors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(),
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { rowSize.width.toDp() }),
        ) {
            motoList.forEach { moto ->
                DropdownMenuItem(
                    text = { Text(text = (if (moto.imageRes != null) moto.imageRes.brandName+" - " else "") + moto.model,
                        modifier = Modifier.fillMaxWidth()) },
                    leadingIcon = {
                        if (moto.imageRes != null) {
                            Image(painterResource(moto.imageRes.imageRes),
                                null,
                                Modifier.size(24.dp)) } },
                    onClick = {
                        onSelect(moto)
                        expanded = false
                    })
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun DateField(
    @StringRes label: Int,
    dateDate: String,
    mCalendar: Calendar,
    modifier: Modifier,
    firstChange: (() -> Unit)? = null,
    updateDate: (year: Int, month: Int, day: Int) -> Unit,
) {
    val mContext = LocalContext.current
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            updateDate(year, month, day)
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)
    )
    TextField(
        enabled = false,
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        value = dateDate,
        onValueChange = {},
        modifier = modifier
            .clickable {
                if (firstChange != null) { firstChange() }
                mDatePickerDialog.show()
            },
        label = { Text(text = stringResource(id = label), overflow = TextOverflow.Ellipsis, maxLines = 1) },
    )
}

@ExperimentalMaterial3Api
@Composable
private fun GenericTextField(
    @StringRes label: Int,
    data: String,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = data,
        onValueChange = { onValueChange(it) },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        isError = isError,
        label = { Text(text = stringResource(id = label)) },
    )
}

@ExperimentalMaterial3Api
@Composable
private fun GenericNumberField(
    @StringRes label: Int,
    data: String,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = TextFieldValue(
            text = data,
            selection = TextRange(data.length)
        ), // TextRange ensures cursor always at the end
        onValueChange = { onValueChange(it.text) },
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        isError = isError,
        label = { Text(text = stringResource(id = label)) },
    )
}

fun digitDecimalFilter(text: String): String {
    var empty = text.isEmpty()
    var justZero = text.length == text.count { it == '0' }
    var zeroDot = listOf("0.","0.0","0..",".").indexOf(text) != -1
    val filterDigitAndDecimal = text.filterIndexed { index, char -> // Permit only digits and one decimal to most left
        char in "0123456789" || text.indexOf('.') == index
    }
    val trimZeroEnds = when { // Remove zero ends
        filterDigitAndDecimal.trim('0') == "." -> ""
        else -> filterDigitAndDecimal.trimStart('0')
    }
    val twoDecimal = when { // Remove excess decimals
        trimZeroEnds.contains('.') && trimZeroEnds.substringAfter('.').length > 2 ->
            trimZeroEnds.dropLast(trimZeroEnds.substringAfter('.').length - 2)
        else -> trimZeroEnds
    }
    val addZeroStart = when {
        empty -> ""
        twoDecimal.indexOf('.') == 0 -> "0"+twoDecimal
        justZero -> "0"
        zeroDot -> "0."
        else -> twoDecimal
    }
    return addZeroStart
}