package it.gml

class CreateMatrix {
    static Matrix of(int rows, int columns) {
        assert rows > 0 : 'Number of rows must be positive'
        assert columns > 0 : 'Number of columns must be positive'

        return new TornadoMatrix(rows, columns)
    }
    static Matrix from(List<List<Number>> elements) {
        assert elements : 'Elements list must not be empty'
        assert elements.every {it.size() == elements.first().size()} : 'All rows must be of equal length'

        return new TornadoMatrix(elements)
    }

    static Matrix zero(int size) {
        return new TornadoMatrix((([0.0] * size) * size) as List<List<Number>>)
    }

    static Matrix identity(final int size) {
        diagonal(size, 1.0)
    }

    static Matrix diagonal(int size, Number elem) {
        Matrix ret = zero(size)
        for(int i = 0; i < size; i++) {
            ret[i][i] = elem
        }
        return ret
    }
}
