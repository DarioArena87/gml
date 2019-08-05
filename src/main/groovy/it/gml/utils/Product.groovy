package it.gml.utils

import groovy.transform.CompileStatic

@CompileStatic
class Product {
    static Number of(Iterable<Number> numbers){
        numbers.inject(1, { final Number a, final Number b -> a * b })
    }
}
