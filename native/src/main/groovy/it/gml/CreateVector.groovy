package it.gml

class CreateVector {

    static Vector from(Number[] elems) {
        return new NativeVector(elems)
    }

    static Vector from(Iterable<Number> elems) {
        return new NativeVector(elems)
    }
}
