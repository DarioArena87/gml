package it.gml

import groovy.transform.EqualsAndHashCode

class Vector extends Matrix {
    Vector(Number[] elems){
        super([elems as List<Number>])
    }

    Vector(Iterable<Number> elems){
        super([elems as List<Number>])
    }

}
