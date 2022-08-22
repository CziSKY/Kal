package ink.kirraobj.kal

import ink.kirraobj.kal.impl.*


@Suppress("MemberVisibilityCanBePrivate")
object Kal {

    @JvmStatic
    fun main(args: Array<String>) {
        println(Minus(Minus(mkLit(1), mkLit(2)), Plus(mkLit(2), mkLit(3))).simp())
        println(Multiply(mkLit(2), mkLit(0)).simp())
    }

    fun env2LocEnv(env: Map<String, Int>, loc: Map<String, Int>): Array<Int> {
        val arr = arrayOfNulls<Int>(loc.size)
        loc.entries.forEach {
            arr[it.value] = env[it.key]
        }
        return arr.filterNotNull().toTypedArray()
    }

    fun mkLit(value: Int) = Lit(value) as Expr
    fun mkPlus(left: Expr, right: Expr) = Plus(left, right) as Expr
    fun mkMinus(left: Expr, right: Expr) = Minus(left, right) as Expr
    fun mkMultiply(left: Expr, right: Expr) = Multiply(left, right) as Expr
    fun mkDivide(left: Expr, right: Expr) = Divide(left, right) as Expr
}