package com.capraro.chapter3

sealed class List<out A> {

    fun <A> tail(xs: List<A>): List<A> =
            when (xs) {
                is Nil -> throw IllegalStateException("Nil cannot have a `tail`")
                is Cons -> xs.tail
            }

    fun <A> setHead(xs: List<A>, x: A): List<A> =
            when (xs) {
                is Nil -> throw IllegalStateException("Cannot set `head` of Nil")
                is Cons -> Cons(x, xs.tail)
            }

    fun <A> drop(l: List<A>, n: Int): List<A> =
            if (n == 0) l
            else when (l) {
                is Nil -> throw IllegalStateException("Cannot drop elements of Nil")
                is Cons -> drop(l.tail, n - 1)
            }

    fun <A> dropWhile(l: List<A>, p: (A) -> Boolean): List<A> =
            when (l) {
                is Nil -> l
                is Cons -> if (p(l.head)) dropWhile(l.tail, p) else l
            }

    fun <A> append(a1: List<A>, a2: List<A>): List<A> =
            when (a1) {
                is Nil -> a2
                is Cons -> Cons(a1.head, append(a1.tail, a2))
            }

    fun <A> init(l: List<A>): List<A> =
            when (l) {
                is Nil -> throw IllegalStateException("Cannot init Nil")
                is Cons ->
                    if (l.tail == Nil) Nil
                    else Cons(l.head, init(l.tail))
            }

    fun <A> empty(): List<A> = Nil

    fun <A, B> foldRight(xs: List<A>, z: B, f: (A, B) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> f(xs.head, foldRight(xs.tail, z, f))
            }

    tailrec fun <A, B> foldLeft(xs: List<A>, z: B, f: (B, A) -> B): B =
            when (xs) {
                is Nil -> z
                is Cons -> foldLeft(xs.tail, f(z, xs.head), f)
            }

    fun <A, B> foldLeftR(xs: List<A>, z: B, f: (B, A) -> B): B =
            foldRight(
                    xs,
                    { b: B -> b },
                    { a, g ->
                        { b ->
                            g(f(b, a))
                        }
                    })(z)

    fun <A> length(xs: List<A>): Int = foldRight(xs, 0) { _, acc -> acc + 1 }

    fun <A> length2(xs: List<A>): Int = foldLeft(xs, 0) { acc , _ -> acc + 1 }

    fun <A> reverse(xs: List<A>): List<A> = foldLeft(xs, empty()) { xs, x -> Cons(x, xs)}

    companion object {

        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1..aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }

}

object Nil : List<Nothing>()

data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()