package es.javier.cappcake.domain

data class Ingredient(val name: String, val amount: Float, val amountType: AmountType)

enum class AmountType {
    NONE,
    ML,
    L,
    G,
    KG,
    AMOUNT
}