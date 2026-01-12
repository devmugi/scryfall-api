@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.CatalogsApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsCatalog
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for CatalogsApi.
 *
 * Usage:
 * ```javascript
 * const api = new CatalogsApiJs();
 * const cardNames = await api.cardNames();
 * const creatureTypes = await api.creatureTypes();
 * ```
 */
@JsExport
class CatalogsApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = CatalogsApi(config)

    /** Returns all English card names on Scryfall. */
    @JsName("cardNames")
    fun cardNames(): Promise<JsCatalog> = GlobalScope.promise { api.cardNames().toJs() }

    /** Returns all artist names on Scryfall. */
    @JsName("artistNames")
    fun artistNames(): Promise<JsCatalog> = GlobalScope.promise { api.artistNames().toJs() }

    /** Returns all English words that could appear in a card name. */
    @JsName("wordBank")
    fun wordBank(): Promise<JsCatalog> = GlobalScope.promise { api.wordBank().toJs() }

    /** Returns all supertypes in the game. */
    @JsName("supertypes")
    fun supertypes(): Promise<JsCatalog> = GlobalScope.promise { api.supertypes().toJs() }

    /** Returns all card types in the game. */
    @JsName("cardTypes")
    fun cardTypes(): Promise<JsCatalog> = GlobalScope.promise { api.cardTypes().toJs() }

    /** Returns all creature supertypes and subtypes. */
    @JsName("creatureTypes")
    fun creatureTypes(): Promise<JsCatalog> = GlobalScope.promise { api.creatureTypes().toJs() }

    /** Returns all Planeswalker types. */
    @JsName("planeswalkerTypes")
    fun planeswalkerTypes(): Promise<JsCatalog> = GlobalScope.promise { api.planeswalkerTypes().toJs() }

    /** Returns all land supertypes and subtypes. */
    @JsName("landTypes")
    fun landTypes(): Promise<JsCatalog> = GlobalScope.promise { api.landTypes().toJs() }

    /** Returns all artifact types. */
    @JsName("artifactTypes")
    fun artifactTypes(): Promise<JsCatalog> = GlobalScope.promise { api.artifactTypes().toJs() }

    /** Returns all battle types. */
    @JsName("battleTypes")
    fun battleTypes(): Promise<JsCatalog> = GlobalScope.promise { api.battleTypes().toJs() }

    /** Returns all enchantment types. */
    @JsName("enchantmentTypes")
    fun enchantmentTypes(): Promise<JsCatalog> = GlobalScope.promise { api.enchantmentTypes().toJs() }

    /** Returns all spell types (instant and sorcery types). */
    @JsName("spellTypes")
    fun spellTypes(): Promise<JsCatalog> = GlobalScope.promise { api.spellTypes().toJs() }

    /** Returns all possible power values in the game. */
    @JsName("powers")
    fun powers(): Promise<JsCatalog> = GlobalScope.promise { api.powers().toJs() }

    /** Returns all possible toughness values in the game. */
    @JsName("toughnesses")
    fun toughnesses(): Promise<JsCatalog> = GlobalScope.promise { api.toughnesses().toJs() }

    /** Returns all possible loyalty values in the game. */
    @JsName("loyalties")
    fun loyalties(): Promise<JsCatalog> = GlobalScope.promise { api.loyalties().toJs() }

    /** Returns all watermarks used on cards. */
    @JsName("watermarks")
    fun watermarks(): Promise<JsCatalog> = GlobalScope.promise { api.watermarks().toJs() }

    /** Returns all keyword abilities on cards. */
    @JsName("keywordAbilities")
    fun keywordAbilities(): Promise<JsCatalog> = GlobalScope.promise { api.keywordAbilities().toJs() }

    /** Returns all keyword actions on cards. */
    @JsName("keywordActions")
    fun keywordActions(): Promise<JsCatalog> = GlobalScope.promise { api.keywordActions().toJs() }

    /** Returns all ability words on cards. */
    @JsName("abilityWords")
    fun abilityWords(): Promise<JsCatalog> = GlobalScope.promise { api.abilityWords().toJs() }

    /** Returns all flavor words on cards. */
    @JsName("flavorWords")
    fun flavorWords(): Promise<JsCatalog> = GlobalScope.promise { api.flavorWords().toJs() }
}
