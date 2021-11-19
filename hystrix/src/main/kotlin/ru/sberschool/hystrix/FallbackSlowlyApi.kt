package ru.sberschool.hystrix


class FallbackSlowlyApi : SlowlyApi {

    override fun getFourthAbility(): Ability {
        return Ability("fallback")
    }
}

