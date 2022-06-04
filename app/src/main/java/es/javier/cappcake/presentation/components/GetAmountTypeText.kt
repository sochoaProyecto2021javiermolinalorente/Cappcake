package es.javier.cappcake.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import es.javier.cappcake.R
import es.javier.cappcake.domain.AmountType


@Composable
fun getAmountTypeText(amountType: AmountType) : String {
    return when (amountType) {
        AmountType.NONE -> stringResource(id = R.string.ingredient_none)
        AmountType.ML -> stringResource(id = R.string.ingredient_ml)
        AmountType.L -> stringResource(id = R.string.ingredient_l)
        AmountType.MG -> stringResource(id = R.string.ingredient_mg)
        AmountType.G -> stringResource(id = R.string.ingredient_g)
        AmountType.KG -> stringResource(id = R.string.ingredient_kg)
        AmountType.AMOUNT -> stringResource(id = R.string.ingredient_amount)
    }
}