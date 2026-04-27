package co.tiagoaguiar.loteriacomposedev.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.tiagoaguiar.loteriacomposedev.R
import co.tiagoaguiar.loteriacomposedev.ui.theme.Green

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoNumberTextField(
    value: String,
    @StringRes label: Int,
    @StringRes placeholder: Int,
    imeAction: ImeAction,
    onValueChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        maxLines = 1,
        label = {
            Text(
                stringResource(label)
            )
        },
        placeholder = {
            Text(stringResource(placeholder))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        onValueChange = onValueChanged
    )
}

@Preview(showBackground = true)
@Composable
fun LoNumberTextFieldPreview() {
    LoNumberTextField(
        value = "abc",
        label = R.string.trevo,
        placeholder = R.string.bets,
        imeAction = ImeAction.Done
    ) { }
}

@Composable
fun LoItemType(
    name: String,
    @DrawableRes icon: Int = R.drawable.trevo,
    color: Color = Color.Black,
    bgColor: Color = Color.Transparent
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .wrapContentSize()
            .background(bgColor)
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = stringResource(R.string.trevo),
            modifier = Modifier
                .size(100.dp)
                .padding(10.dp)
        )

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.padding(top = 4.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun LoItemTypePreview() {
    LoItemType(
        name = "Mega Sena",
        color = Color.White,
        bgColor = Green
    )
}