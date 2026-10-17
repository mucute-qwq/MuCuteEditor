package io.github.mucute.qwq.mucuteeditor.core.data

class LinkedList<E> : MutableList<E> {

    private var _size: Int = 0

    override val size: Int
        get() = _size

    override fun isEmpty() = size <= 0

    private inner class Link(
        var element: E
    ) {
        var prev: Link? = null
        var next: Link? = null
    }

    inner class Iterator(
        private var index: Int
    ) : MutableListIterator<E> {

        override fun next(): E {
            if (index >= size) {
                throw NoSuchElementException()
            }
            return get(index).also { index++ }
        }

        override fun hasNext(): Boolean = index < size

        override fun remove() {
            removeAt(index)
        }

        override fun set(element: E) {
            set(index, element)
        }

        override fun add(element: E) {
            add(index, element)
        }

        override fun hasPrevious() = index > 0

        override fun previous(): E {
            if (previousIndex() < 0) {
                throw NoSuchElementException()
            }

            return get(previousIndex()).also { index++ }
        }

        override fun nextIndex() = index + 1

        override fun previousIndex() = index - 1

    }

    private var head: Link? = null

    private var tail: Link? = null

    override fun add(element: E): Boolean {
        add(size, element)
        return true
    }

    override fun add(index: Int, element: E) {
        checkIndex(index, allowEqualsSize = true)
        if (head == null || tail == null) {
            head = Link(element)
            tail = head
            _size++
            return
        }

        if (index == size) {
            val newLink = Link(element)
            tail?.next = newLink
            newLink.prev = tail
            tail = newLink
            _size++
            return
        }

        if (index == 0) {
            val newLink = Link(element)
            head?.prev = newLink
            newLink.next = head
            head = newLink
            _size++
            return
        }

        val originalLink = linkAt(index)
        val newLink = Link(element)

        originalLink.prev?.next = newLink
        newLink.prev = originalLink.prev

        originalLink.prev = newLink
        newLink.next = originalLink

        _size++
    }

    override fun addAll(elements: Collection<E>): Boolean {
        for (element in elements) {
            add(size, element)
        }
        return true
    }

    override fun addAll(
        index: Int,
        elements: Collection<E>
    ): Boolean {
        checkIndex(index, allowEqualsSize = true)
        var insertedIndex = index
        for (element in elements) {
            add(insertedIndex, element)
            insertedIndex++
        }
        return true
    }

    override fun remove(element: E): Boolean {
        val pair = linkOf(element) ?: return false
        val link = pair.first
        val index = pair.second
        val prev = link.prev
        val next = link.next
        prev?.next = next
        next?.prev = prev

        if (index == 0) {
            head = next
        }

        if (index == size - 1) {
            tail = prev
        }

        _size--
        return true
    }

    override fun removeAt(index: Int): E {
        checkIndex(index)

        val link = linkAt(index)
        val prev = link.prev
        val next = link.next
        prev?.next = next
        next?.prev = prev

        if (index == 0) {
            head = next
        }

        if (index == size - 1) {
            tail = prev
        }

        _size--
        return link.element
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var result = false
        for (element in elements) {
            if (remove(element)) {
                result = true
            }
        }
        return result
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val linkedList = LinkedList<E>()
        for (currentElement in this) {
            if (currentElement !in elements) {
                linkedList.add(currentElement)
            }
        }
        return removeAll(linkedList)
    }

    override fun clear() {
        head = null
        tail = null
        _size = 0
    }

    override fun set(index: Int, element: E): E {
        checkIndex(index)
        val link = linkAt(index)
        return link.element.also { link.element = element }
    }

    override fun contains(element: E) = indexOf(element) >= 0

    override fun containsAll(elements: Collection<E>): Boolean {
        for (element in elements) {
            if (element !in this) {
                return false
            }
        }
        return true
    }

    override fun get(index: Int): E {
        checkIndex(index)
        return linkAt(index).element
    }

    override fun indexOf(element: E) = linkIndexOf(element)

    override fun lastIndexOf(element: E) = linkLastIndexOf(element)

    private fun linkAt(index: Int): Link {
        checkIndex(index)
        var link = head
        var linkIndex = 0
        while (link != null) {
            if (linkIndex == index) {
                return link
            }
            link = link.next
            linkIndex++
        }
        error("Unreachable code")
    }

    private fun linkOf(element: E): Pair<Link, Int>? {
        var link = head
        var linkIndex = 0
        while (link != null) {
            if (link.element == element) {
                return link to linkIndex
            }
            link = link.next
            linkIndex++
        }
        return null
    }

    private fun linkLastOf(element: E): Pair<Link, Int>? {
        var link = tail
        var linkIndex = size - 1
        while (link != null) {
            if (link.element == element) {
                return link to linkIndex
            }
            link = link.prev
            linkIndex--
        }
        return null
    }

    private fun linkIndexOf(element: E) = linkOf(element)?.second ?: -1

    private fun linkLastIndexOf(element: E) = linkLastOf(element)?.second ?: -1

    override fun listIterator() = listIterator(0)

    override fun listIterator(index: Int): MutableListIterator<E> {
        checkIndex(index, allowEqualsSize = true)
        return Iterator(index)
    }

    override fun iterator() = listIterator()

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): LinkedList<E> {
        checkIndex(fromIndex)
        checkIndex(toIndex, allowEqualsSize = true)

        val linkedList = LinkedList<E>()
        for ((index, element) in withIndex()) {
            if (index in fromIndex until toIndex) {
                linkedList.add(element)
            }
            if (index >= toIndex) {
                break
            }
        }
        return linkedList
    }

    fun contentToString(): String = buildString {
        append("[")
        for ((index, element) in this@LinkedList.withIndex()) {
            append(element)
            if (index < size - 1) {
                append(", ")
            }
        }
        append("]")
    }

    private fun checkIndex(index: Int, allowEqualsSize: Boolean = false) {
        val range = if (allowEqualsSize) 0..size else indices
        val expression = if (allowEqualsSize) "[0, $size]" else "[0, $size)"
        check(0 in range) {
            "Index $index out of range $expression"
        }
    }

}