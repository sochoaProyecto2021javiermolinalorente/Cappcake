package es.javier.cappcake.domain

data class Ingredient(var name: String, var amount: Float, var amountType: AmountType)

enum class AmountType {
    NONE,
    ML,
    L,
    MG,
    G,
    KG,
    AMOUNT
}