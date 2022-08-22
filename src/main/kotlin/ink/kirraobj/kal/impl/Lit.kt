package ink.kirraobj.kal.impl

import ink.kirraobj.kal.Expr

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