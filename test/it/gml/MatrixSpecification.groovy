package it.gml

import spock.lang.Specification

class MatrixSpecification extends Specification {
    def "A matrix is a grid of elements"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 2, 3, 4],
                [3, 4, 2, 6],
                [0, 8, 3, 1]
            ]
        )

        expect:
        a.rows == 3
        a.columns == 4
    }

    def "A matrix should not be empty/1"() {
        when:
        Matrix a = new Matrix(elems)

        then:
        thrown AssertionError

        where:
        elems << [[], null]
    }

    def "A matrix should not be empty/2"() {
        when:
        Matrix a = new Matrix(rows, cols)

        then:
        thrown AssertionError

        where:
        rows  | cols
         0    |  1
         1    |  0
        -1    |  0
         0    | -1
    }

    def "In a matrix all the rows must have the same number of columns"() {
        when:
        Matrix a = new Matrix(
            [
                [1, 2, 3, 4],
                [3, 4],
                [0, 8, 3]
            ]
        )

        then:
        thrown AssertionError
    }

    def "Two matrices are equals if they have the same dimensions and all elements are the same"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Matrix b = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Matrix c = new Matrix(
            [
                [1, 3, 6, 7],
                [7, 5, 2, 5],
                [0, 1, 4, 0],
            ]
        )

        Matrix d = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 1],
                [0, 1, 4],
            ]
        )

        expect:
        a == b
        a != c
        a != d
        c != d
    }

    def "Matrix addition"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Matrix b = new Matrix(
            [
                [6, 1, 2],
                [2, 4, 3],
                [8, 5, 1],
            ]
        )

        expect:
        a + b == new Matrix(
            [
                [7, 4, 8],
                [9, 9, 5],
                [8, 6, 5],
            ]
        )
    }

    def "Matrix scalar addition"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Number b = 2.5

        expect:
        a + b == new Matrix(
            [
                [3.5, 5.5, 8.5],
                [9.5, 7.5, 4.5],
                [2.5, 3.5, 6.5],
            ]
        )
    }

    def "Matrix subtraction"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Matrix b = new Matrix(
            [
                [6, 1, 2],
                [2, 4, 3],
                [8, 5, 1],
            ]
        )

        expect:
        a - b == new Matrix(
            [
                [-5, 2, 4],
                [5, 1, -1],
                [-8, -4, 3],
            ]
        )
    }

    def "Matrix scalar subtraction"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        Number b = 3.2

        expect:
        a - b == new Matrix(
            [
                [-2.2, -0.2, 2.8],
                [ 3.8, 1.8, -1.2],
                [-3.2, -2.2, 0.8],
            ]
        )
    }

    def "Per scalar multiplication"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        expect:
        a * 2 == new Matrix(
            [
                [2, 6, 12],
                [14, 10, 4],
                [0, 2, 8]
            ]
        )
    }

    def "Matrix multiplication"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 5, 2, 6],
                [8, 0, 7, 5],
                [2, 1, 3, 4]
            ]
        )

        Matrix b = new Matrix(
            [
                [2, 1],
                [3, 7],
                [2, 0],
                [7, 9],
            ]
        )

        when:
        Matrix product = a * b

        then:
        product == new Matrix(
            [
                [63, 90],
                [65, 53],
                [41, 45]
            ]
        )
    }

    def "Matrix power is a very fast way of multiply matrix by itself"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 6],
                [7, 5, 2],
                [0, 1, 4],
            ]
        )

        expect:
        a**15 == a * a * a * a * a * a * a * a * a * a * a * a * a * a * a
    }

    def "Transposing a matrix reflects it by its main diagonal"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3, 5],
                [2, 4, 6],
            ]
        )

        expect:
        a.transpose() == new Matrix(
            [
                [1, 2],
                [3, 4],
                [5, 6],
            ]
        )
    }

    def "Dot product"() {
        given:
        Matrix a = new Matrix(
            [
                [1],
                [3],
                [-2],
            ]
        )

        Matrix b = new Matrix(
            [
                [4],
                [-2],
                [-1],
            ]
        )

        when:
        Number result = a ^ b

        then:
        result == 0
    }

    def "Sparsity is the number non zero elements over total number of elements"() {
        given:
        Matrix m = new Matrix(
            [
                [0, 0, 0, 0],
                [5, 8, 0, 0],
                [0, 0, 3, 0],
                [0, 6, 0, 0],
            ]
        )

        expect:
        m.sparsity == 0.25
        m.density == 1 - m.sparsity
    }

    def "Determinant"() {
        given:
        Matrix a = new Matrix(
            [
                [1, 3],
                [7, 5]
            ]
        )

        Matrix b = new Matrix(
            [
                [-2, 2, -3],
                [-1, 1,  3],
                [2,  0, -1],
            ]
        )

        expect:
        a.determinant == -16
        b.determinant == 18
    }

    def "Minors of a matrix are constructed removing a row and a column from a matrix"() {
        given:
        Matrix m = new Matrix(
            [
                [3, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 3, 4],
                [8, 6, 7, 0],
            ]
        )

        when:
        Matrix minor01 = m.minor(0, 1)
        Matrix minor13 = m.minor(1, 3)

        then:
        m == new Matrix(
            [
                [3, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 3, 4],
                [8, 6, 7, 0],
            ]
        )

        and:
        minor01 == new Matrix(
            [
                [5, 0, 5],
                [1, 3, 4],
                [8, 7, 0]
            ]
        )

        minor13 == new Matrix(
            [
                [3, 0, 2],
                [1, 0, 3],
                [8, 6, 7]
            ]
        )
    }

    def "Inverse matrix is the one that multiplies a matrix and results in identity matrix"() {
        given:
        Matrix m = new Matrix(
            [
                [1, 2],
                [2, 3]
            ]
        )

        when:
        Matrix inverse = m.invert()

        then:
        inverse == new Matrix(
            [
                [-3,  2],
                [ 2, -1]
            ]
        )

        m * inverse == MatrixGenerator.identity(2)
    }

    def "Augment"() {
        given:
        Matrix a = new Matrix(
            [
                [5, 0, 5],
                [1, 3, 4],
                [8, 7, 0]
            ]
        )

        Matrix i = MatrixGenerator.identity(3)

        expect:
        a.augment(i) == new Matrix(
            [
                [5, 0, 5, 1, 0, 0],
                [1, 3, 4, 0, 1, 0],
                [8, 7, 0, 0, 0, 1]
            ]
        )
    }

}
