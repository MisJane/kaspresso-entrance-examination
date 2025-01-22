package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.assertj.core.api.Assertions.assertThat


class CerealStorageImplTest {

    // private val storage = CerealStorageImpl(10f, 20f)
    private val storageCapacity = 50f
    private val containerCapacity = 10f
    private val storage = CerealStorageImpl(containerCapacity, storageCapacity)
    private val cereal = Cereal.MILLET


    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `addCereal should throw IllegalArgumentException if amount is negative`() {
        assertThrows<IllegalArgumentException> {
            storage.addCereal(cereal, -5f)
        }
    }

    @Test
    fun `addCereal should throw IllegalArgumentException if storage capacity is exceeded`() {
        repeat((storageCapacity / containerCapacity).toInt()) {
            storage.addCereal(Cereal.MILLET, containerCapacity)
        }
        assertThrows<IllegalArgumentException> {
            storage.addCereal(cereal, 5f)
        }
    }

    @Test
    fun `addCereal should add cereal if container is exist`() {
        storage.addCereal(cereal, 5f)
        assertThat(storage.getAmount(cereal)).isEqualTo(5f)
    }

    @Test
    fun `addCereal should return remainder if container overflows`() {
        val remainder = storage.addCereal(cereal, 15f)
        assertThat(remainder).isEqualTo(5f)
        assertThat(storage.getAmount(cereal)).isEqualTo(containerCapacity)
    }

}