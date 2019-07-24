package it.gml

import spock.lang.Specification

class GaussJordanSpec extends Specification {

    def "Echelon Form"() {
        given:
        Matrix m = new Matrix(
            [
                [0, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 0, 4],
                [8, 6, 7, 0],
            ]
        )

        when:
        Matrix echForm = GaussJordan.transformToEchelonForm(m).result.round(4)

        then:
        m == new Matrix(
            [
                [0, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 0, 4],
                [8, 6, 7, 0],
            ]
        )

        and:
        echForm == new Matrix(
            [
                [5,    8,       0,      5],
                [0, -6.8,       7,     -8],
                [0,    0, -1.6471, 4.8824],
                [0,    0,       0, 5.9286],
            ]
        )
    }

    def "Reduced Echelon form"(){
        given:
        Matrix m = new Matrix(
            [
                [0, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 0, 4],
                [8, 6, 7, 0],
            ]
        )

        when:
        Matrix echForm = GaussJordan.transformToReducedEchelonForm(m).result.round(4)

        then:
        m == new Matrix(
            [
                [0, 0, 2, 0],
                [5, 8, 0, 5],
                [1, 0, 0, 4],
                [8, 6, 7, 0],
            ]
        )

        and:
        echForm == new Matrix(
            [
                [1, 0, 0, 0],
                [0, 1, 0, 0],
                [0, 0, 1, 0],
                [0, 0, 0, 1]
            ]
        )
    }
}
