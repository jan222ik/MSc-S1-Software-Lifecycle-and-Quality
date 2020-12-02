package impl

import kotlin.math.sqrt

object QuadraticSolver {
    /**
     * TODO Document me
     * @param a
     * @param b
     * @param c
     * @return
     */
    fun solve(a: Double, b: Double, c: Double): List<Double> {
        if (a.equals(0.0)) throw ParamAZero()
        if (4*a*c > b*b) throw DiscriminantNegative()

        val plus = (-b + sqrt(b*b - 4 * a * c)) / 2*a
        val minus = (-b - sqrt(b*b - 4 * a * c)) / 2*a
        val results = mutableListOf<Double>()

        results.add(plus)
        if (!plus.equals(minus)) {
          results.add(minus)
        }
        return results
    }

    class DiscriminantNegative: Error("Discriminant is negative, thus no real solution can be found..")
    class ParamAZero: Error("Param a is zero, thus no real solution can be found.")
}
