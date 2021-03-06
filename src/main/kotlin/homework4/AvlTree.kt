package homework4

class AvlTree<K : Comparable<K>, V> : Map<K, V> {
    private var root: AvlNode<K, V>? = null
    private var _size: Int = 0
    override val size: Int
        get() = _size
    override val entries: Set<Map.Entry<K, V>>
        get() {
            val entries = mutableSetOf<AvlNode<K, V>>()
            root?.getEntries(entries) ?: emptySet<AvlNode<K, V>>()
            return entries
        }
    override val keys: Set<K>
        get() = entries.map { it.key }.toSet()
    override val values: Collection<V>
        get() = entries.map { it.value }

    fun clear() {
        root = null
        _size = 0
    }

    fun addValue(key: K, value: V) {
        if (this.isEmpty()) {
            root = AvlNode(key, value)
            _size++
        } else {
            if (root?.add(key, value)!!) {
                _size++
            }
            root?.updateHeight()
            root = root?.balance()
        }
    }

    fun remove(key: K) {
        root = root?.remove(key, null)
    }

    override fun containsKey(key: K): Boolean = root?.isContainsKey(key) ?: false

    override fun containsValue(value: V): Boolean = root?.isContainsValue(value) ?: false

    override fun isEmpty(): Boolean = root == null

    override fun get(key: K): V? = root?.getValueByKey(key)
}
