package it.gml.utils

import groovy.transform.CompileStatic

@CompileStatic
class Product {
    static Number product(Iterable<Number> numbers){
        numbers.inject(1, { final Number a, final Number b -> a * b })
    }
}
