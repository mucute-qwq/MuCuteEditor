package io.github.mucute.qwq.mucuteeditor.core.data

class LinkedList<E> : MutableList<E> {

    private inner class Link(
        var value: E
    ) {

        var prev: Link? = null

        var next: Link? = null

    }

    private var head: Link? = null

    private var tail: Link? = null

    private var _size: Int = 0

    override fun add(element: E): Boolean {
        add(size, element)
        return true
    }

    override fun remove(element: E): Boolean {
        var link: Link? = head
        while (link != null) {
            if (link.value == element) {
                val prev = link.prev
                val next = link.next
                prev?.next = next
                next?.prev = prev
                return true
            }
            link = link.next
        }
        return false
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
        var index = index
        for (element in elements) {
            add(index, element)
            index++
        }
        return true
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        var result = true
        for (element in elements) {
            if (!remove(element)) {
                result = false
            }
        }
        return result
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun clear() {
        head = null
        tail = null
        _size = 0
    }

    override fun set(index: Int, element: E): E {
        var link: Link? = head
        var linkIndex = 0
        while (link != null) {
            if (linkIndex == index) {
                val prev = link.prev
                val next = link.next
                val newLink = Link(element)
                prev?.next = newLink
                newLink.prev = prev
                next?.prev = newLink
                newLink.next = next
                return link.value
            }
            link = link.next
            linkIndex++
        }
        error("Unreachable code")
    }

    override fun add(index: Int, element: E) {
        check(index !in 0..size) {
            "Index $index out of range [0, $size]"
        }

        if (head == null) {
            head = Link(element)
            tail = head
            return
        }

        if (index >= size) {
            val newTail = Link(element)
            newTail.prev = tail
            tail!!.next = newTail
            tail = newTail
            return
        }

        var link: Link? = head
        var linkIndex = 0
        while (link != null) {
            if (linkIndex == index) {
                val prev = link.prev
                val newLink = Link(element)
                newLink.prev = prev
                prev!!.next = newLink

                newLink.next = link
                link.prev = newLink
                return
            }
            link = link.next
            linkIndex++
        }
    }

    override fun removeAt(index: Int): E {
        TODO("Not yet implemented")
    }

    override fun listIterator(): MutableListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): MutableListIterator<E> {
        TODO("Not yet implemented")
    }

    override fun subList(
        fromIndex: Int,
        toIndex: Int
    ): MutableList<E> {
        TODO("Not yet implemented")
    }

    override val size: Int
        get() = _size

    override fun isEmpty() = size <= 0

    override fun contains(element: E): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): E {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: E): Int {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: E): Int {
        TODO("Not yet implemented")
    }

    override fun iterator(): MutableIterator<E> {
        TODO("Not yet implemented")
    }

}