package it.gml

class CreateMatrix {

    static Matrix of(int rows, int cols) {
        return new NativeMatrix(rows, cols)
    }

    static Matrix from(List<List<Number>> elements) {
        return new NativeMatrix(elements)
    }

    static Matrix zero(int rows, int columns) {
        of(rows, columns)
    }

    static Matrix diagonal(int size, Number elem) {
        final Matrix m = zero(size, size)

        for(int i = 0; i < size; i++) {
            m[i][i] = elem
        }

        return m
    }

    static Matrix identity(final int size) {
        diagonal(size, 1)
    }

    static Matrix random(final int rows, final int columns) {
        final Matrix m = of(rows, columns)
        for (int i = 0; i< rows; i++){
            for (int j = 0; j<columns; j++){
                m[i][j] = Math.random() as Number
            }
        }

        return m

    }
}
