package ink.kirraobj.kal


@Suppress("MemberVisibilityCanBePrivate")
object Kal {

    @JvmStatic
    fun main(args: Array<String>) {
        println(Multiply(mkLit(2), mkLit(0)).simp())
    }

    abstract class Expr {

        abstract fun again(loc: Map<String, Int>): (IntArray) -> (Int)

        abstract fun yolo(loc: Map<String, Int>, env: Array<Int>): Int

        abstract fun locate(loc: MutableMap<String, Int>)

        abstract fun eval(vars: Map<String, Int>): Int

        open fun simp() = this
    }

    data class Var(val name: String) : Expr() {

        override fun again(loc: Map<String, Int>): (IntArray) -> Int {
            val index = loc[name]!!
            return { env -> env[index] }
        }

        override fun yolo(loc: Map<String, Int>, env: Array<Int>): Int {
            val index = loc[name]!!
            return env[index]
        }

        override fun locate(loc: MutableMap<String, Int>) {
            if (loc.containsKey(name)) {
                loc[name] = loc.size
            }
        }

        override fun eval(vars: Map<String, Int>): Int {
            return vars[name]!!
        }
    }

    data class Lit(val value: Int) : Expr() {

        override fun again(loc: Map<String, Int>): (IntArray) -> Int = {
            value
        }

        override fun yolo(loc: Map<String, Int>, env: Array<Int>): Int {
            return value
        }

        override fun locate(loc: MutableMap<String, Int>) {}

        override fun eval(vars: Map<String, Int>) = value

        override fun toString() = value.toString()
    }

    data class Plus(val left: Expr, val right: Expr) : Expr() {

        override fun again(loc: Map<String, Int>): (IntArray) -> Int {
            return { env -> left.again(loc).invoke(env) + right.again(loc).invoke(env) }
        }

        override fun yolo(loc: Map<String, Int>, env: Array<Int>): Int {
            return left.yolo(loc, env) + right.yolo(loc, env)
        }

        override fun locate(loc: MutableMap<String, Int>) {
            left.locate(loc)
            right.locate(loc)
        }

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

        override fun again(loc: Map<String, Int>): (IntArray) -> Int {
            return { env -> left.again(loc).invoke(env) * right.again(loc).invoke(env) }
        }

        override fun yolo(loc: Map<String, Int>, env: Array<Int>): Int {
            return left.yolo(loc, env) * right.yolo(loc, env)
        }

        override fun locate(loc: MutableMap<String, Int>) {
            left.locate(loc)
            right.locate(loc)
        }

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

    fun env2LocEnv(env: Map<String, Int>, loc: Map<String, Int>): Array<Int> {
        val arr = arrayOfNulls<Int>(loc.size)
        loc.entries.forEach {
            arr[it.value] = env[it.key]
        }
        return arr.filterNotNull().toTypedArray()
    }

    fun mkLit(value: Int) = Lit(value) as Expr
    fun mkPlus(left: Expr, right: Expr) = Plus(left, right) as Expr
    fun mkMultiply(left: Expr, right: Expr) = Multiply(left, right) as Expr
}