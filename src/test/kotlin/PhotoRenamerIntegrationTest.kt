package org.iesra

import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

class PhotoRenamerIntegrationTest {

    @TempDir
    lateinit var tempDir: Path

    @Test
    fun `genera los scripts esperados para los cuatro ejemplos`() {
        val examples = listOf("Japan", "Paris", "italia", "londres")

        examples.forEach { place ->
            val input = copyInputToTemp(place)

            main(arrayOf(input.toString()))

            val generatedScript = input.parent.resolve("$place.sh")
            val expectedScript = Path.of("examples", "$place.sh")

            assertTrue(
                generatedScript.exists(),
                "No se genero el fichero de salida esperado: $generatedScript"
            )
            assertEquals(
                expectedScript.readLines(),
                generatedScript.readLines(),
                "El script generado para $place no coincide con examples/$place.sh"
            )
        }
    }

    private fun copyInputToTemp(place: String): Path {
        val inputDirectory = tempDir.resolve(place).createDirectories()
        val sourceInput = Path.of("examples", "$place.in")
        val targetInput = inputDirectory.resolve("$place.in")

        targetInput.writeText(sourceInput.toFile().readText())

        return targetInput
    }
}
