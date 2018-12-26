package ru.otus.l051.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Test {

	static class None extends Throwable {

		private static final long serialVersionUID = 1L;

		private None() {

		}
	}

	Class<? extends Throwable> expected() default None.class;

}
