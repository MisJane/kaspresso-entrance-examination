package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    @Test
    fun `containerCapacity negative throws`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-1f, 10f)
        }
    }

    @Test
    fun `storageCapacity less than containerCapacity throws`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `addCereal to existing container partially fills it`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 5f)
        val remaining = storage.addCereal(Cereal.MILLET, 3f)
        assertEquals(0f, remaining, 0.01f)
        assertEquals(8f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `addCereal to existing container overflows`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 8f)
        val remaining = storage.addCereal(Cereal.MILLET, 5f)
        assertEquals(3f, remaining, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `addCereal new cereal when storage has space`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 5f)
        val remaining = storage.addCereal(Cereal.RICE, 12f)
        assertEquals(2f, remaining, 0.01f)
        assertEquals(10f, storage.getAmount(Cereal.RICE), 0.01f)
    }

    @Test
    fun `addCereal new cereal when storage is full throws`() {
        val storage = CerealStorageImpl(10f, 10f)
        storage.addCereal(Cereal.MILLET, 5f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.RICE, 5f)
        }
    }

    @Test
    fun `getCereal from existing container returns correct amount`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 8f)
        val taken = storage.getCereal(Cereal.MILLET, 5f)
        assertEquals(5f, taken, 0.01f)
        assertEquals(3f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `getCereal more than available returns remaining`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 3f)
        val taken = storage.getCereal(Cereal.MILLET, 5f)
        assertEquals(3f, taken, 0.01f)
        assertEquals(0f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `getCereal from non-existent container returns zero`() {
        val storage = CerealStorageImpl(10f, 20f)
        val taken = storage.getCereal(Cereal.MILLET, 5f)
        assertEquals(0f, taken, 0.01f)
    }

    @Test
    fun `getCereal negative amount throws`() {
        val storage = CerealStorageImpl(10f, 20f)
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.MILLET, -1f)
        }
    }

    @Test
    fun `removeContainer empty container returns true`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 5f)
        storage.getCereal(Cereal.MILLET, 5f)
        assertTrue(storage.removeContainer(Cereal.MILLET))
        assertFalse(storage.getAmount(Cereal.MILLET) > 0)
    }

    @Test
    fun `removeContainer non-empty returns false`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 5f)
        assertFalse(storage.removeContainer(Cereal.MILLET))
    }

    @Test
    fun `removeContainer non-existent returns false`() {
        val storage = CerealStorageImpl(10f, 20f)
        assertFalse(storage.removeContainer(Cereal.RICE))
    }

    @Test
    fun `getAmount returns correct value`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 7f)
        assertEquals(7f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `getSpace returns correct value`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 3f)
        assertEquals(7f, storage.getSpace(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `toString returns correct representation`() {
        val storage = CerealStorageImpl(10f, 20f)
        storage.addCereal(Cereal.MILLET, 5f)
        storage.addCereal(Cereal.RICE, 3f)
        val str = storage.toString()
        assertTrue(str.contains("MILLET=5.0") && str.contains("RICE=3.0"))
    }
}