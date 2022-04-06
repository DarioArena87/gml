package it.gml

class NativeVector extends NativeMatrix implements Vector {

    NativeVector(Number[] elems){
        super([elems as List<Number>])
    }

    NativeVector(Iterable<Number> elems){
        super([elems as List<Number>])
    }

}
