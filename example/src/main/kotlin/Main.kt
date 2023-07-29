class Person {
    var name: String? = "foo"
    var address: String = ""
    var age: Int = 0
}

fun main(arg: Array<String>) {
    val p = Person()

    println(p.name)
}