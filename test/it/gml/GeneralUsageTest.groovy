package it.gml

import spock.lang.Specification

class GeneralUsageTest extends Specification {
    def "Solving linear system using library"() {
        given: "A coefficient matrix"
        Matrix A = new Matrix(
            [
                [1, -1, 1],
                [2, 1, -1],
                [1, -1, -1]
            ]
        )

        and: "A known vector"
        Matrix b = new Matrix(
            [
                [6], [-3], [0]
            ]
        )

        when: "I compute the solution as A^(-1) * b"
        Matrix x = A**-1 * b

        then:
        x.round(4) == new Matrix(
            [
                [1], [-2], [3]
            ]
        )
    }

    def "If a matrix is singular, system has no solution"() {
        given: "A singular coefficient matrix"
        Matrix A = new Matrix(
            [
                [1, -1, 4],
                [1, -2, 0],
                [2, -5, -4]
            ]
        )

        and: "A known vector"
        Matrix b = new Matrix(
            [
                [0], [1], [2]
            ]
        )

        when: "I compute the solution as A^(-1) * b"
        Matrix x = A**-1 * b

        then:
        thrown(AssertionError)
    }

    def "Big matrices inversion are not a problem"() {
        given: "A big matrix"
        Matrix A = Spy(Matrix, constructorArgs: [MatrixGenerator.random(50, 50).elems])

        when:
        Matrix b = A.invert()

        then:
        1 * A.determinant
    }
}
