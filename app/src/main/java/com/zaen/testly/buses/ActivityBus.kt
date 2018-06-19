package com.zaen.testly.buses

import io.reactivex.subjects.BehaviorSubject

class ActivityBus{
    private var subject: BehaviorSubject<Any> = BehaviorSubject.create<Any>()
        get() = field
}