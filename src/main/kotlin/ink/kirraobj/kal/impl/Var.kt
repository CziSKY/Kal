package ink.kirraobj.kal.impl

import ink.kirraobj.kal.Expr

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