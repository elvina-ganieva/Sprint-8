package ru.sberschool.hystrix

import feign.RequestLine

interface SlowlyApi {
    @RequestLine("GET /pokemon/?limit=1")
    fun getFirstPokemon(): Pokemon

    @RequestLine("GET /ability/?limit=1")
    fun getFirstAbility(): Ability

    @RequestLine("GET /location/?limit=1")
    fun getFirstLocation(): Location

    @RequestLine("GET /generation/?limit=1")
    fun getFirstGame(): Game
}


