package com.dumbdogdiner.sass.reward.impl

import com.dumbdogdiner.sass.reward.api.Challenge

class ChallengeImpl(
    private val store: ChallengeStoreImpl,
    private val identifier: String,
) : Challenge {
    private var name = identifier
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

    override fun delete() {
        ensureValid()
        store.removeChallenge(identifier)
        invalid = true
    }

    private fun ensureValid() {
        if (invalid) throw IllegalStateException("Use of invalid statistic '$identifier'")
    }
}