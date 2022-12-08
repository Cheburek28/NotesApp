package com.example.androiddev

enum class osTypeEnum {
    Linux,
    Windows,
    MacOs,
}

fun main() {
    println("Hello world!")

    val a : String = "Hello world" // Константа
    var b : String = "Hello world" // Изменяемая

    var x : Int = 1
    var y : Float = 1.0f
    val z : Boolean = false

    val someType = 1 // Тут отработает динамическая типизация, если это очевидно

    println(someType) // Вывод интов в тупую работает

    x += 10
    y += 15

    var result: Int

    val max = if (x > y) x else y

    var osType = 2

    osType -= 1

    val osName : String = when (osType) {
        1 -> "Linux"
        2 -> "Windows"
        else -> "unknown"
    }

    println(osName)

    println(max(20, 23))

    val secondPart = 10
    val stringResult = "first"

    val fullstrRes = "first$secondPart" // аналог f строк питона

    println(getWeight(1002))

    println(osTypeEnum.Linux.ordinal)

    val r = if (osType == osTypeEnum.Linux.ordinal) "1" else "2" // Вот так проверяется на equal

    println(r)

    // OOP

    val animal = Animal(2, 10)
    val animal2 = Animal(weight = 5, age = 2)

    println("age = ${animal.age}")
    println(animal.ageAsString())
    println(animal2.ageAsString())

    println(Duck(10, 15).ageAsString())

    val m = TestDataClass(1)

    val new_m = m.copy() // При этом можно передать аргументы
}

fun max(a: Int, b: Int) : Int {
    return if (a > b) a else b
}

fun max_short(a: Int, b: Int) : Int = if (a > b) a else b // Короткая реализация функций


fun getWeight(weightInGramms: Int) : String { // Возвразает амссу в кг если больше кг иначе в граммах
    return if (weightInGramms > 1000) "${(weightInGramms.toFloat() / 1000)} Kg" else "$weightInGramms G"
}

//public val apub = 1 // - по умолчаниб все публичные
private val apriv = 2

open class Animal( // Constuctor, open means that someone can inherit from it. Все классы наследубтся от котлин класса Any
    val age: Int,
    private val weight: Int,
    private val isAlive: Boolean = true
) {
    fun getWeight() : Int = weight
    fun getIsAlive() : Boolean = isAlive

    fun ageAsString() : String {
        return "age = $age" // Или же this.age
    }
}


class Duck(
    age: Int,
    weight: Int,
    isAlive: Boolean = true
) : Animal(age, weight, isAlive) { // Наследоваться можно только один раз, а интерфейсов можно совать солько угожно
    fun fly() {
        println("Flying")
    }

}


class Dolphin

abstract class Test() { // Нельзя создать сущеность для этого класса, только отначледоваться от него
    abstract val t: Int

    abstract fun absfunc() : Boolean

    abstract val absval : Int
}

class TestChild(
    override val t: Int // Перезапись абстрактного аргумента
) : Test() {
    override fun absfunc(): Boolean {
        return false
    }

    override val absval: Int
        get() = 5 // Можно сделать и set

}


// ИНТЕРФЕЙСЫ

interface  Flyable {
    fun fly()
}

class Vorona(
    age: Int,
    weight: Int,
    isAlive: Boolean = true
) : Animal(age, weight, isAlive), Flyable {
    override fun fly() {
        println("Flying")
    }

}

// Классы держатели данных

data class TestDataClass(       // Держатель данных, Нужен чтобы при сравнении с другим истансом сравнивались значения
    private val x : Int, // При сравнении двух инстансов он будет смотреть на эти значение. Если хотим сравнит из по ссылке, то пишем ===, иначе ==
)