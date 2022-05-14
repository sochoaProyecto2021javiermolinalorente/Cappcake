package es.javier.cappcake.domain

import java.util.*

data class Ingredient(val id: String = UUID.randomUUID().toString(), var name: String, var amount: Float, var amountType: AmountType)

enum class AmountType {
    NONE,
    ML,
    L,
    MG,
    G,
    KG,
    AMOUNT
}