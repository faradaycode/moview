package com.movie.mandiri.utils

class BaseContract {
    interface Presenter<in T> {
        fun subsribe()
        fun unsubscribe()
        fun attach(view: T)
    }

    interface View {}
}