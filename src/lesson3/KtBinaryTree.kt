package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */

    // O(n) - траты времени, где n - высота дерева
    // O(1) - траты памяти
    override fun remove(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null)
            return false
        else
            element.compareTo(closest.value)

        if (comparison != 0)
            return false

        return remove(closest)
    }

    private fun remove(closest: Node<T>): Boolean {
        val parent = findParent(closest)
        when {

            (closest.left == null) and (closest.right == null) -> {
                when {
                    parent == null -> root = null
                    parent.left == closest -> parent.left = null
                    parent.right == closest -> parent.right = null
                }
            }

            (closest.left == null) and (closest.right != null) -> {
                when {
                    parent == null -> root = closest.right
                    parent.left == closest -> parent.left = closest.right
                    parent.right == closest -> parent.right = closest.right
                }
            }

            (closest.left != null) and (closest.right == null) -> {
                when {
                    parent == null -> root = closest.left
                    parent.left == closest -> parent.left = closest.left
                    parent.right == closest -> parent.right = closest.left
                }
            }

            (closest.left != null) and (closest.right != null) -> {
                var replacementNode = closest.right
                var replacementRightChildToMove = false

                while (replacementNode?.left != null) {
                    replacementNode = replacementNode.left
                    if (replacementNode?.right != null)
                        replacementRightChildToMove = true
                }

                val replacementNodeParent = findParent(replacementNode!!.value)

                when {
                    parent == null -> root = replacementNode
                    parent.left == closest -> parent.left = replacementNode
                    parent.right == closest -> parent.right = replacementNode
                }

                replacementNode.left = closest.left
                if (replacementNode != closest.right) {
                    if (replacementRightChildToMove)
                        replacementNodeParent?.left = replacementNode.right
                    else
                        replacementNodeParent?.left = null
                    replacementNode.right = closest.right
                }
            }

        }
        size--
        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun findParent(value: T): Node<T>? =
        root?.let { findParent(it, null, value) }

    private fun findParent(node: Node<T>): Node<T>? =
        root?.let { findParent(it, null, node.value) }

    private fun findParent(start: Node<T>, parent: Node<T>?, value: T): Node<T>? {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> parent
            comparison < 0 -> start.left?.let { findParent(it, start, value) } ?: parent
            else -> start.right?.let { findParent(it, start, value) } ?: parent
        }
    }

    private fun maxNode(): Node<T>? {
        var ans = root
        while (ans?.right != null)
            ans = ans.right
        return ans
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private val visitedNodes = mutableSetOf<Node<T>>()

        private var currentNode = root

        private var wasDeleted = false

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */

        // O(1) - траты времени
        // O(n) - траты памяти, где n - высота дерева
        override fun hasNext(): Boolean {
            if (root == null)
                return false
            var node = root
            while (node?.right != null)
                node = node.right
            return node != currentNode
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */

        // O(n) - траты времени, где n - высота дерева
        // O(1) - траты памяти
        override fun next(): T {
            /*println("current node is: " + currentNode?.value)
            println("left: " + currentNode?.left?.value)
            println("right: " + currentNode?.right?.value)
            for (i in visitedNodes) {
                print(i.value)
                print(" ")
            }
            println()
            println()*/
            if (wasDeleted) {
                wasDeleted = false
                currentNode?.let { visitedNodes.add(it) }
                return currentNode!!.value
            }
            val nextNodeData = nextNode(currentNode, visitedNodes)
            currentNode = nextNodeData.first
            visitedNodes.addAll(nextNodeData.second)
            return currentNode!!.value
        }

        private fun nextNode(node: Node<T>?, visited: MutableSet<Node<T>>): Pair<Node<T>?, MutableSet<Node<T>>> {
            var ans: Node<T>?
            when {
                (node?.left !in visited) and (node?.left != null) -> {
                    ans = node
                    while (ans?.left != null) {
                        ans = ans.left
                    }
                }
                node !in visited -> {
                    ans = node
                }
                (node?.right !in visited) and (node?.right != null) -> {
                    ans = node?.right
                    while (ans?.left != null) {
                        ans = ans.left
                    }
                }
                else -> {
                    if (node == maxNode())
                        return Pair(null, visited)
                    ans = node
                    while (ans in visited)
                        ans = findParent(ans!!)
                }
            }
            ans.let { visited.add(it!!) }
            return Pair(ans, visited)
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            wasDeleted = true
            val replacement = nextNode(currentNode, visitedNodes)
            remove(find(currentNode!!.value)!!)
            currentNode = replacement.first
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        val limit = find(toElement)
        return if (limit?.value == toElement)
            this.filter { (it < toElement) and (it >= fromElement) }.toSortedSet()
        else
            this.filter { (it <= toElement) and (it >= fromElement) }.toSortedSet()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        val limit = find(toElement)
        return if (limit?.value == toElement)
            this.filter { it < toElement }.toSortedSet()
        else
            this.filter { it <= toElement}.toSortedSet()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> = this.filter { it >= fromElement}.toSortedSet()

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
