package it.gml.utils

final class Format {
    static Closure<String> leftPad = { final String s, final int size ->
        (([' '] * (size - s.length())) + [s]).join('')
    }

    static Closure<String> toPlainString = { final Number elem ->
        elem.toBigDecimal().toPlainString()
    }
}
