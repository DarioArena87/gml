package it.gml

class Vector extends Matrix {
    Vector(Number[] elems){
        super([elems as List<Number>])
    }

    Vector(Iterable<Number> elems){
        super([elems as List<Number>])
    }

    Number xor(Vector b) {
        dotProduct(b)
    }

    Number dotProduct(Vector b) {
        assert elements[0].size() == b.elements[0].size(): "Vector should have the same size"
        return [elements[0], b.elements[0]].transpose().collect { it[0] * it[1] }.sum()
    }
}
