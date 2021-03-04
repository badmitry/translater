package com.badmitry.repository.repo

interface Repository<T> {

    suspend fun getData(word: String): T
}
