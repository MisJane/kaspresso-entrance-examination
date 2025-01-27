package ru.webrelab.kie.cerealstorage

import kotlin.math.min

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val containers = mutableMapOf<Cereal, Float>()
    private val maxContainers = (storageCapacity / containerCapacity).toInt()
    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Может быть только положительным" }

        val currentAmount = containers[cereal] ?: 0f
        val availableSpace = containerCapacity - currentAmount

        return if (currentAmount > 0) {
            val added = min(amount, availableSpace)
            containers[cereal] = currentAmount + added
            amount - added
        } else {
            if (containers.size >= maxContainers) {
                throw IllegalStateException("Невозможно добавить новый контейнер,- хранилище занято")
            }
            val added = min(amount, containerCapacity)
            containers[cereal] = added
            amount - added
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount >= 0) { "Может быть только положительным" }

        val currentAmount = containers[cereal] ?: 0f
        val taken = min(amount, currentAmount)
        containers[cereal] = currentAmount - taken
        return taken
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        return if ((containers[cereal] ?: 0f) == 0f) {
            containers.remove(cereal) != null
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return containers[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        return containerCapacity - getAmount(cereal)
    }

    override fun toString(): String {
        return containers.entries.joinToString(", ") { "${it.key}=${it.value}" }
    }

}
