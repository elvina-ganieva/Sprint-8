package ru.sberschool.hystrix


class FallbackSlowlyApi : SlowlyApi {
    override fun getFirstPokemon(): Pokemon {
        return Pokemon(arrayOf(Result("no name", "no url")))
    }

    override fun getFirstAbility(): Ability {
        return Ability(arrayOf(Result("no ability", "no url")))
    }

    override fun getFirstLocation(): Location {
        return Location(arrayOf(Result("no location", "no url")))
    }

    override fun getFirstGame(): Game {
        return Game(arrayOf(Result("no game", "no url")))
    }
}

