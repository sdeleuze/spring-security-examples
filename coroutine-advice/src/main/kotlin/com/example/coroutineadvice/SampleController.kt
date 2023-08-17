package com.example.coroutineadvice

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
open class SampleController {
    @GetMapping("/helloSuspend")
    open suspend fun helloSuspend() : String {
        delay(1)
        println("helloSuspend")
        return "hello"
    }

    @GetMapping("/helloSuspendDenied")
    open suspend fun helloSuspendDenied() : String {
        delay(1)
        return "heyshouldfail"
    }

    @GetMapping("/hello")
    open fun hello() : Mono<String> {
        println("hello")
        return Mono.just("hello");
    }

    @GetMapping("/helloDenied")
    open fun helloDenied() : Mono<String> {
        return Mono.just("heyshouldfail");
    }
}
