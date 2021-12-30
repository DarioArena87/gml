package it.gml.utils

final class Format {
    static leftPad(String s, int size, String padStr = ' ') {
        return repeat(padStr, size - s.length()) + s
    }

    static repeat(String s, int times) {
        if (times <= 0) {
            return ""
        }
        return s * times
    }

}
