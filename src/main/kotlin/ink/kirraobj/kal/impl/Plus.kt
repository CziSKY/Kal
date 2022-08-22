package ink.kirraobj.kal.impl

import ink.kirraobj.kal.Expr
import ink.kirraobj.kal.Kal.mkLit
import ink.kirraobj.kal.Kal.mkPlus

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