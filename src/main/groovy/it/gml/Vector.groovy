package it.gml

class Vector extends Matrix {
    Vector(Number[] elems){
        super([elems as List<Number>])
    }

    Vector(Iterable<Number> elems){
        super(elems.collate(1))
    }
}
