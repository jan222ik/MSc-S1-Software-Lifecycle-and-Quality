package impl

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt


object QuadraticSolver {
    /**
     * Calculates and returns the real solutions of a quadratic function of the form ax^2 + bx + c = 0 in form of a list
     * of doubles
     * @param a first parameter of quadratic function
     * @param b second parameter of quadratic function
     * @param c third parameter of quadratic function
     * @return immutable list of doubles of real solutions
     * @exception DiscriminantNegative if discriminant is negative (b^2 - 4ac) < 0
     * @exception ParamAZero if parameter a is zero
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


    fun solveBigDecimal(a: Double, b: Double, c: Double): List<Double> {
        if (a.equals(0.0)) throw ParamAZero()
        if (4*a*c > b*b) throw DiscriminantNegative()

        val big_a = BigDecimal(a)
        val big_b = BigDecimal(b)
        val big_c = BigDecimal(c)
        val results = mutableListOf<Double>()
        val plus = -big_b + bigSqrt(big_b.pow(2) - BigDecimal(4) * big_a * big_c)!!.divide(BigDecimal(2) * big_a)
        val minus = -big_b - bigSqrt(big_b.pow(2) - BigDecimal(4) * big_a * big_c)!!.divide(BigDecimal(2) * big_a)

        results.add(plus.toDouble())
        if (!plus.equals(minus)) {
            results.add(minus.toDouble())
        }
        return results
    }

    class DiscriminantNegative: Error("Discriminant is negative, thus no real solution can be found..")
    class ParamAZero: Error("Param a is zero, thus no real solution can be found. (Division by zero)")


    private val SQRT_DIG = BigDecimal(150)
    private val SQRT_PRE = BigDecimal(10).pow(SQRT_DIG.toInt())

    /**
     * Private utility method used to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    private fun sqrtNewtonRaphson(c: BigDecimal, xn: BigDecimal, precision: BigDecimal): BigDecimal? {
        val fx = xn.pow(2).add(c.negate())
        val fpx = xn.multiply(BigDecimal(2))
        var xn1 = fx.divide(fpx, 2 * SQRT_DIG.toInt(), RoundingMode.HALF_DOWN)
        xn1 = xn.add(xn1.negate())
        val currentSquare = xn1.pow(2)
        var currentPrecision = currentSquare.subtract(c)
        currentPrecision = currentPrecision.abs()
        return if (currentPrecision.compareTo(precision) <= -1) {
            xn1
        } else sqrtNewtonRaphson(c, xn1, precision)
    }

    /**
     * Uses Newton Raphson to compute the square root of a BigDecimal.
     *
     * @author Luciano Culacciatti
     * @url http://www.codeproject.com/Tips/257031/Implementing-SqrtRoot-in-BigDecimal
     */
    fun bigSqrt(c: BigDecimal): BigDecimal? {
        return sqrtNewtonRaphson(c, BigDecimal(1), BigDecimal(1).divide(SQRT_PRE))
    }



}
