package ru.sberschool.hystrix

data class Pokemon(val results: Array<Result>?)

data class Ability(val results: Array<Result>?)

data class Location(val results: Array<Result>?)

data class Game(val results: Array<Result>?)

data class Result(val name: String, val url: String)
