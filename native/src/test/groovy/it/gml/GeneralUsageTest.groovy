package it.gml


import spock.lang.Specification

class GeneralUsageTest extends Specification {
    def "Solving linear system using library"() {
        given: "A coefficient matrix"
        Matrix A = CreateMatrix.from(
            [
                [1, -1,  1],
                [2,  1, -1],
                [1, -1, -1]
            ]
        )

        and: "A known vector"
        Matrix b = CreateVector.from(6, -3, 0).transpose()

        when: "I compute the solution as A^(-1) * b"
        Matrix x = A**-1 * b

        then:
        x.round(4) == CreateVector.from(1, -2, 3).transpose()
    }

    def "If a matrix is singular, system has no solution"() {
        given: "A singular coefficient matrix"
        Matrix A = CreateMatrix.from(
            [
                [1, -1, 4],
                [1, -2, 0],
                [2, -5, -4]
            ]
        )

        and: "A known vector"
        Matrix b = CreateVector.from([0, 1, 2])

        when: "I compute the solution as A^(-1) * b"
        Matrix x = A**-1 * b

        then:
        thrown(AssertionError)
    }

    def "Big matrices inversion are not a problem"() {
        given: "A big matrix"
        Matrix A = Spy(CreateMatrix.random(50, 50))

        when:
        Matrix b = A.invert()

        then:
        1 * A.determinant
        (b * A).round(4) == CreateMatrix.identity(50)
    }
}
