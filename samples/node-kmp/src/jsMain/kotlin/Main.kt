import devmugi.mtgcards.scryfall.api.CardsApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

fun main() {
    val api = CardsApi()
    MainScope().launch {
        val card = api.random()
        println("Random Card: ${card.name}")
        println("Type: ${card.typeLine}")
        println("Text: ${card.oracleText ?: "No text"}")
    }
}
