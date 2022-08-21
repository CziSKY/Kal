package ink.kirraobj.kal


@Suppress("MemberVisibilityCanBePrivate")
object Kal {

    @JvmStatic
    fun main(args: Array<String>) {
//        val expr = mkMultiply(mkPlus(mkLit(1), mkLit(2)), mkPlus(mkLit(3), mkLit(4)))
//        println(Plus(mkLit(2), mkLit(0)).simp())
        println(Multiply(mkLit(2), mkLit(0)).simp())
//        println(expr.toString())
//        println(expr.eval(mutableMapOf()))
    }

    abstract class Expr {

        abstract fun eval(vars: Map<String, Int>): Int

        open fun simp() = this
    }

    data class Var(val name: String) : Expr() {

        override fun eval(vars: Map<String, Int>): Int {
            return vars[name]!!
        }
    }

    data class Lit(val value: Int) : Expr() {

        override fun eval(vars: Map<String, Int>) = value

        override fun toString() = value.toString()
    }

    data class Plus(val left: Expr, val right: Expr) : Expr() {

        override fun eval(vars: Map<String, Int>) = left.eval(vars) + right.eval(vars)

        override fun toString() = "($left + $right)"

        override fun simp(): Expr {
            val left = left.simp()
            val right = right.simp()
            return when {
                left == mkLit(0) -> right
                right == mkLit(0) -> left
                left == right -> mkLit((left as Lit).value + (right as Lit).value)
                else -> mkPlus(left, right)
            }
        }
    }

    data class Multiply(val left: Expr, val right: Expr) : Expr() {

        override fun eval(vars: Map<String, Int>) = left.eval(vars) * right.eval(vars)

        override fun toString() = "($left * $right)"

        override fun simp(): Expr {
            val left = left.simp()
            val right = right.simp()
            return when {
                left == mkLit(1) -> right
                right == mkLit(1) -> left
                left == mkLit(0) || right == mkLit(0) -> mkLit(0)
                left is Lit && right is Lit -> mkLit(left.value * right.value)
                else -> mkMultiply(left, right)
            }
        }
    }

    fun mkLit(value: Int) = Lit(value) as Expr
    fun mkPlus(left: Expr, right: Expr) = Plus(left, right) as Expr
    fun mkMultiply(left: Expr, right: Expr) = Multiply(left, right) as Expr
}