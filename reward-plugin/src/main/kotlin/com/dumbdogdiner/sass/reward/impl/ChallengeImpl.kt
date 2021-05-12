package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.Challenge
import java.util.UUID
import java.util.function.Predicate

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
    private var name: String,
    private var visibility: Predicate<UUID>
) : Challenge {
    private var invalid = false

    override fun getChallengeStore(): ChallengeStoreImpl {
        ensureValid()
        return store
    }

    override fun getIdentifier(): String {
        ensureValid()
        return identifier
    }

    override fun getName(): String {
        ensureValid()
        return name
    }

    override fun setName(value: String) {
        ensureValid()
        name = value
    }

    override fun getVisibility(): Predicate<UUID> {
        ensureValid()
        return visibility
    }

    override fun setVisibility(value: Predicate<UUID>) {
        ensureValid()
        visibility = value
    }

    override fun delete() {
        ensureValid()
        store.removeChallenge(identifier)
        invalid = true
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid statistic '$identifier'")
    }
}