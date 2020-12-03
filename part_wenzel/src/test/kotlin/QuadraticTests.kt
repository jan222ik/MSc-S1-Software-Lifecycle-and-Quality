import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import impl.QuadraticSolver
import org.junit.Assert.fail
import org.junit.Test
import kotlin.math.sqrt
import kotlin.reflect.KFunction3
import kotlin.reflect.jvm.javaMethod
import kotlin.system.measureNanoTime


class QuadraticTests {

    class Performance {
        @Test
        fun speed() {
            val maxTime = 10_000_000 // 10ms
            val nanoTimes = mutableListOf<Long>()
            for (i in 0 until 1000) {
                nanoTimes.add(measureNanoTime {
                    QuadraticSolver.solve(1.0, 4.0, 1.0)
                })
            }
            nanoTimes.filter { it > maxTime }.takeIf { it.isNotEmpty() }?.let { fail("Execution too slow") }
        }
    }

    class Functional {
        @ExperimentalStdlibApi
        @Test
        fun `Check Function Types`() {
            val kFunction3: KFunction3<Double, Double, Double, List<Double>> = QuadraticSolver::solve
            val javaMethod = kFunction3.javaMethod
            // Check Return Type
            assertThat(
                actual = javaMethod?.returnType,
                criteria = equalTo(List::class.java)
            )
            // Check Param Types
            javaMethod?.parameterTypes?.forEach {
                assertThat(actual = it, criteria = equalTo(Double::class.java))
            }
        }

        @Test(expected = QuadraticSolver.DiscriminantNegative::class)
        fun `Discriminant may not be Negative`() {
            QuadraticSolver.solve(1.0, 1.0, 1.0)
        }

        @Test(expected = QuadraticSolver.ParamAZero::class)
        fun `Parameter a may not be Zero`() {
            QuadraticSolver.solve(0.0, 1.0, 1.0)
        }

        @Test
        fun `Discriminant of Zero shall have exactly one solution`() {
            val result = QuadraticSolver.solve(1.0, 2.0, 1.0)
            assertThat(actual = result.size, criteria = equalTo(1))
        }

        @Test
        fun `Solution with one result`() {
            val result = QuadraticSolver.solve(1.0, 2.0, 1.0)
            assertThat(actual = result, criteria = equalTo(listOf(-1.0)))
        }

        @Test
        fun `Solution with two result`() {
            val result = QuadraticSolver.solve(1.0, 4.0, 1.0)
            assertThat(actual = result, criteria = equalTo(listOf(-2 + sqrt(3.0), -2 - sqrt(3.0))))
        }

        @Test
        fun `Parameter with long mantissa`() {
            val result = QuadraticSolver.solve(1.555555555, 4.5555555555555, 1.555555555555)
            assertThat(actual = result, criteria = equalTo(listOf(-2.5339270808492, -0.39464434772325)))
        }

    }

}
