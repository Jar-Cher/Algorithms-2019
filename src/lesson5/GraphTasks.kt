@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.impl.GraphBuilder
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    TODO()
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph = GraphBuilder().apply {
    for (i in vertices)
        addVertex(i.name)

    val connectionsToAdd = mutableSetOf<Graph.Edge>()
    for (i in edges) {
        val queue = ArrayDeque<Graph.Vertex>()
        val prefs = mutableMapOf<Graph.Vertex, Graph.Vertex>()
        val visited = mutableSetOf<Graph.Vertex>()
        queue.push(i.begin)
        connectionsToAdd.add(i)
        var shouldAdd = true
        while (queue.isNotEmpty()) {
            val currentVertex = queue.pop()
            visited.add(currentVertex)
            for (j in getNeighbors(currentVertex)
                .intersect(
                    connectionsToAdd.filter { (it.begin == currentVertex) || (it.end == currentVertex) }.fold(
                        setOf<Graph.Vertex>()
                    ) { previousResult, element -> previousResult + element.begin + element.end })) {
                shouldAdd = (prefs[currentVertex] == j) || (j !in visited)
                if (!shouldAdd)
                    break
                prefs[j] = currentVertex
                if (j !in visited)
                    queue.add(j)
            }
        }
        if (!shouldAdd)
            connectionsToAdd.remove(i)
    }
    for (i in connectionsToAdd)
        addConnection(i.begin, i.end)
}.build()

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
// O(n) - траты времени, где n - количество вершин в графе
// O(n) - траты памяти, где n - количество вершин в графе
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    if (vertices.isEmpty())
        return emptySet()

    val evens = mutableMapOf<Int, MutableSet<Graph.Vertex>>()
    val odds = mutableMapOf<Int, MutableSet<Graph.Vertex>>()
    val queue = ArrayDeque<Graph.Vertex>()
    val prefs = mutableMapOf<Graph.Vertex, Graph.Vertex>()
    val notVisited = mutableSetOf<Graph.Vertex>()
    notVisited.addAll(vertices)
    var connectedComponent = 0

    while (notVisited.isNotEmpty()) {
        queue.push(notVisited.first())
        evens[connectedComponent] = mutableSetOf<Graph.Vertex>()
        evens[connectedComponent]!!.add(notVisited.first())
        odds[connectedComponent] = mutableSetOf<Graph.Vertex>()
        while (queue.isNotEmpty()) {
            val currentVertex = queue.pop()
            notVisited.remove(currentVertex)
            for (i in getNeighbors(currentVertex)) {
                require(
                    !((i in evens[connectedComponent]!!) ||
                            (i in odds[connectedComponent]!!)) ||
                            prefs[currentVertex] == i
                ) { "" }
                if (prefs[currentVertex] == i)
                    continue
                prefs[i] = currentVertex
                if (currentVertex in evens[connectedComponent]!!)
                    odds[connectedComponent]!!.add(i)
                else
                    evens[connectedComponent]!!.add(i)
                queue.add(i)
            }
        }
        connectedComponent++
    }

    val ans = mutableSetOf<Graph.Vertex>()
    for (i in 0 until connectedComponent) {
        if (evens[i]!!.size >= odds[i]!!.size)
            ans.addAll(evens[i]!!)
        else
            ans.addAll(odds[i]!!)
    }
    return ans
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    TODO()
}