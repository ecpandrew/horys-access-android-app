package com.example.KLSDinfo.Models

class TreeNode<T>(value: T) {
    var value : T = value
    var parent: TreeNode<T>? = null

    var children: MutableList<TreeNode<T>> = mutableListOf()



    fun addChild(node:TreeNode<T>){
        children.add(node)
        node.parent = this
    }


}