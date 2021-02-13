package com.badmitry.translater.presenter

interface IPresenter<T, V> {

    fun attachView(view: V)

    fun detachView(view: V)

    fun getData(word: String, isOnline: Boolean)
}