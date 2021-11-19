package ru.sberschool.hystrix

import feign.RequestLine

interface SlowlyApi {
    @RequestLine("GET /ability/4")
    fun getFourthAbility(): Ability
}
