@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
 * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
 *
 * Пример:
 *
 * 01:15:19 PM
 * 07:26:57 AM
 * 10:00:03 AM
 * 07:56:14 PM
 * 01:15:19 PM
 * 12:40:31 AM
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 12:40:31 AM
 * 07:26:57 AM
 * 10:00:03 AM
 * 01:15:19 PM
 * 01:15:19 PM
 * 07:56:14 PM
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */

// O(n log n) - траты времени
// O(1) - траты памяти
fun sortTimes(inputName: String, outputName: String) {

    val input = File(inputName).readLines()

    File(outputName).bufferedWriter().use {
        require(input.all { time -> time.matches(Regex("""^(0[1-9]|1[0-2]):([0-5]\d):([0-5]\d) [AP]M$""")) }) { "There is no time!" }

        Collections.sort(input) { x, y ->
            compareTimes(x, y)
        }

        for (i in input) {
            it.write(i)
            it.newLine()
        }

        it.close()
    }
}

fun compareTimes(first: String, second: String): Int {

    if ((first[9] == 'P') && (second[9] == 'A'))
        return 1
    if ((first[9] == 'A') && (second[9] == 'P'))
        return -1

    val firstHours = first.substring(0, 2).toInt() % 12
    val secondHours = second.substring(0, 2).toInt() % 12
    val firstMinutes = first.substring(3, 5).toInt()
    val secondMinutes = second.substring(3, 5).toInt()
    val firstSeconds = first.substring(6, 8).toInt()
    val secondSeconds = second.substring(6, 8).toInt()

    if (firstHours > secondHours)
        return 1
    if (firstHours < secondHours)
        return -1
    if (firstMinutes > secondMinutes)
        return 1
    if (firstMinutes < secondMinutes)
        return -1
    if (firstSeconds > secondSeconds)
        return 1
    if (firstSeconds < secondSeconds)
        return -1
    return 0
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {
    TODO()
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */

// Траты времени: O(n+m), где n - длина первого массива, m - длина второго массива
// Траты памяти: O(1)
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {

    var i = 0
    var j = first.size
    var k = 0

    while ((i < first.size) && (j < second.size)) {
        if (first[i] > second[j]!!) {
            second[k] = second[j]
            j++
            k++
        } else {
            second[k] = first[i]
            i++
            k++
        }
    }

    if (i < first.size) {
        for (element in i until first.size) {
            second[k] = first[element]
            k++
        }
    }
}

