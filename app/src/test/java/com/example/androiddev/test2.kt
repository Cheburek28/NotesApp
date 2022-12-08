package com.example.androiddev

fun main() {
    val persons : List<Person> = listOf<Person>(
        Person("bob", 20),
        Person("Jane", 18)

    )

    val anys : List<Any> = listOf<Any>( // Чтобы передать разнообразные типы
        Person("bob", 20),
        Person("Jane", 18),
        10,
        "TeST",
        1.0f
    )

    for (element in persons) {
        println(element)
    }

    for (index in 0..persons.size - 1) {
        println(persons[index])
    }

    val lambda : (Person) -> Int = { person: Person -> person.age}
    persons.sumOf(lambda) // Лямбды
    persons.sumOf{ it.age } // Можно даже просто так

    persons.forEach {
        println(it)
    }

//    persons.forEachIndexed {index, person -> }


    var count = 10
    while (count > 0) {
            println(count)
            count--
        }


}

data class Person(
    val name: String,
    val age: Int,
)