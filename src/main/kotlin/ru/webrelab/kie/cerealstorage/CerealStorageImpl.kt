package ru.webrelab.kie.cerealstorage

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

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        require(amount > 0) { "Кол-во не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)
        val availableSpace = getSpace(cereal)

        return if (availableSpace >= amount) {
            storage[cereal] = currentAmount + amount
            0f
        } else {
            storage[cereal] = currentAmount + availableSpace
            amount - availableSpace
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        require(amount > 0) { "Кол-во не может быть отрицательным" }

        val currentAmount = storage.getOrDefault(cereal, 0f)
        return if (currentAmount >= amount) {
            storage[cereal] = currentAmount - amount
            amount
        } else {
            storage.remove(cereal)
            currentAmount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return if (currentAmount == 0f) {
            storage.remove(cereal)
            true
        } else {
            false
        }
    }

    override fun getAmount(cereal: Cereal): Float {
        return storage.getOrDefault(cereal, 0f)
    }

    override fun getSpace(cereal: Cereal): Float {
        val currentAmount = storage.getOrDefault(cereal, 0f)
        return containerCapacity - currentAmount
    }

    override fun toString(): String {
        return buildString {
            append("Хранилище крупы: \n")
            storage.forEach { (cereal, amount) ->
                append("- ${cereal.name}: $amount/$containerCapacity\n")
            }
        }
    }

}
