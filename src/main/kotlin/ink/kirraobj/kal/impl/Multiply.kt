package ink.kirraobj.kal.impl

import ink.kirraobj.kal.Expr
import ink.kirraobj.kal.Kal.mkLit
import ink.kirraobj.kal.Kal.mkMultiply

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