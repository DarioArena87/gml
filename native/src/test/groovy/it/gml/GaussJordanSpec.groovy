package it.gml

import spock.lang.Specification

class GaussJordanSpec extends Specification {

    def "Echelon Form"() {
        given:
        use(GaussJordan) {
            Matrix m = CreateMatrix.from(
                [
                    [0, 0, 2, 0],
                    [5, 8, 0, 5],
                    [1, 0, 0, 4],
                    [8, 6, 7, 0],
                ]
            )

            expect:
            m.echelonForm.result.round(4) == CreateMatrix.from(
                [
                    [5,    8,       0,      5],
                    [0, -6.8,       7,     -8],
                    [0,    0, -1.6471, 4.8824],
                    [0,    0,       0, 5.9286],
                ]
            )

            and:
            m == CreateMatrix.from(
                [
                    [0, 0, 2, 0],
                    [5, 8, 0, 5],
                    [1, 0, 0, 4],
                    [8, 6, 7, 0],
                ]
            )
        }
    }

    def "Reduced Echelon form"() {
        given:
        use(GaussJordan) {
            Matrix m = CreateMatrix.from(
                [
                    [0, 0, 2, 0],
                    [5, 8, 0, 5],
                    [1, 0, 0, 4],
                    [8, 6, 7, 0],
                ]
            )

            expect:
            m.reducedEchelonForm.result.round(4) == CreateMatrix.from(
                [
                    [1, 0, 0, 0],
                    [0, 1, 0, 0],
                    [0, 0, 1, 0],
                    [0, 0, 0, 1]
                ]
            )

            and:
            m == CreateMatrix.from(
                [
                    [0, 0, 2, 0],
                    [5, 8, 0, 5],
                    [1, 0, 0, 4],
                    [8, 6, 7, 0],
                ]
            )
        }

    }
}
