package gml

import it.gml.CreateVector
import it.gml.Matrix
import it.gml.Vector
import spock.lang.Specification

class VectorSpec extends Specification{
    def "A vector is a unidimensional matrix"(){
        given:
        Vector a = CreateVector.from(1, 2, 3)
        def b = CreateVector.from([4, 5, 6]).transpose()

        expect:
        a instanceof Matrix
        a.rows == 1
        a.columns == 3

        b instanceof Matrix
        b.rows == 3
        b.columns == 1
    }

    def "Two vectors can be multiplied using dot product"() {
        given:
        Vector a = CreateVector.from(1, 3, -5)
        Vector b = CreateVector.from(4, -2, -1)

        when:
        Number result = a ^ b

        then:
        result == 3
    }
}
