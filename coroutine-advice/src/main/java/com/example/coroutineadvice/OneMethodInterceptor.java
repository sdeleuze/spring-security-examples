package com.example.coroutineadvice;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import reactor.core.publisher.Mono;

import org.springframework.core.KotlinDetector;
import org.springframework.core.convert.converter.Converter;

public class OneMethodInterceptor implements MethodInterceptor {

    private final Converter<Object, Mono<Object>> advice = (result) -> {
        if (result.toString().startsWith("h")) {
            return Mono.just("accepted");
        }
        return Mono.error(() -> new IllegalArgumentException("denied"));
    };

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("One Method Interceptor");
        Method method = invocation.getMethod();
        Class<?> returnType = method.getReturnType();
        boolean isSuspendingFunction = KotlinDetector.isSuspendingFunction(method);
        if (Mono.class.isAssignableFrom(returnType) || isSuspendingFunction) {
            Object result = invocation.proceed();
            return ((Mono<?>) result).flatMap((r) -> this.advice.convert(r).then(Mono.just(r)));
        }
        return invocation.proceed();
    }
}
