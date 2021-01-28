package com.willk.ktlntodo

interface RecClickListener {
       fun onCheck(pos: Int, chk: Boolean)
       fun onDel(pos: Int)
}
