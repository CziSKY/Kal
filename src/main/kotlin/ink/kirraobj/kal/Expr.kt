package ink.kirraobj.kal

abstract class Expr {

    abstract fun again(loc: Map<String, Int>): (IntArray) -> (Int)

    abstract fun yolo(loc: Map<String, Int>, env: Array<Int>): Int

    abstract fun locate(loc: MutableMap<String, Int>)

    abstract fun eval(vars: Map<String, Int> = mapOf()): Int

    open fun simp() = this
}