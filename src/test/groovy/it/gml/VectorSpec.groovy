package it.gml

import spock.lang.Specification

class VectorSpec extends Specification{
    def "A vector is a unidimensional matrix"(){
        given:
        Vector a = new Vector(1, 2, 3)
        Vector b = new Vector([4, 5, 6])

        expect:
        a in Matrix
        a.rows == 1
        a.columns == 3

        b in Matrix
        b.rows == 3
        b.columns == 1
    }
}
