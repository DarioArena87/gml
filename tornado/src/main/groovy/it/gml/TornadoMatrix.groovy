package it.gml

import uk.ac.manchester.tornado.api.annotations.Parallel
import uk.ac.manchester.tornado.api.collections.types.Double4
import uk.ac.manchester.tornado.api.collections.types.Matrix2DDouble
import uk.ac.manchester.tornado.matrix.MatrixMath
import static it.gml.CreateMatrix.identity

class TornadoMatrix implements Matrix{

    private final Matrix2DDouble matrix

    TornadoMatrix(int rows, int cols) {
        matrix = new Matrix2DDouble(rows, cols)
    }

    TornadoMatrix(List<List<Number>> elements) {
        matrix = new Matrix2DDouble(elements as double[][])
    }

    @Override
    int getRows() {
        return matrix.N()
    }

    @Override
    int getColumns() {
        return matrix.M()
    }

    @Override
    Number[] getAt(int row) {
        def rowBuffer = matrix.row(row).asBuffer()
        assert rowBuffer.hasArray() : "Row $row buffer is not backed by accessible array"
        return rowBuffer.array()
    }

    @Override
    void putAt(int rowIndex, Number[] row) {
        matrix.row(rowIndex).set(row as double[])
    }

    @Override
    boolean equals(Object o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }

        TornadoMatrix that = o as TornadoMatrix

        return equalsDimensions(that) && elementsEquals(that)
    }

    @Override
    int hashCode() {
        return matrix.hashCode()
    }

    @Override
    boolean equalsDimensions(Matrix b) {
        return rows == b.rows && columns == b.columns
    }

    @Override
    boolean elementsEquals(Matrix other) {
        def a = matrix.flattenedArray
        def b = (other as TornadoMatrix).matrix.flattenedArray

        for (@Parallel int i = 0; i < a.size(); i++) {
            if(a[i] != b[i]) {
                return false
            }
        }

        return true
    }

    @Override
    Matrix plus(Matrix other) {
        return this + other as TornadoMatrix
    }

    Matrix plus(TornadoMatrix other) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j) + other.matrix.get(i, j))
            }
        }
        return ret
    }

    @Override
    Matrix plus(Number scalar) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j) + scalar.doubleValue())
            }
        }
        return ret
    }

    @Override
    Matrix minus(Matrix other) {
        return this - other as TornadoMatrix
    }

    Matrix minus(TornadoMatrix other) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j) - other.matrix.get(i, j))
            }
        }
        return ret
    }

    @Override
    Matrix minus(Number scalar) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j) - scalar.doubleValue())
            }
        }
        return ret
    }

    @Override
    Matrix multiply(Matrix other) {
        return this * other as TornadoMatrix
    }

    Matrix multiply(TornadoMatrix other) {
        TornadoMatrix ret = new TornadoMatrix(rows, other.columns)
//        MatrixMath.dgemm(matrix, other.matrix, ret.matrix)
        for (@Parallel int i = 0; i < ret.rows; i++) {
            for (@Parallel int j = 0; j < ret.columns; j++) {
                double sum = 0.0
                for(@Parallel int k = 0; k < columns; k++) {
                    sum += matrix.get(i, k) * other.matrix.get(k, j)
                }
                ret.matrix.set(i, j, sum)
            }
        }
        return ret
    }

    @Override
    Matrix multiply(Number scalar) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j) * scalar.doubleValue())
            }
        }
        return ret
    }

    @Override
    Matrix power(long exponent) {
        assert rows == columns: "Matrix must be a square matrix"

        Matrix a = exponent > 0 ? this : this.invert()
        Matrix result = identity(rows)

        long power = exponent.abs()
        while (power) {
            if (power % 2 == 1) {
                result *= a
            }

            a *= a
            power /= 2
        }

        return result
    }

    @Override
    Matrix negative() {
        return null
    }

    @Override
    Number xor(Matrix b) {
        return null
    }

    @Override
    Number dotProduct(Matrix b) {
        return null
    }

    @Override
    Matrix transpose() {
        return null
    }

    @Override
    Number getDeterminant() {
        return null
    }

    @Override
    Matrix minor(int row, int column) {
        return null
    }

    @Override
    Number getSparsity() {
        return null
    }

    @Override
    Number getDensity() {
        return null
    }

    @Override
    Matrix invert() {
        return null
    }

    @Override
    Matrix round(int precision) {
        TornadoMatrix ret = new TornadoMatrix(rows, columns)
        for (@Parallel int i = 0; i < rows; i++) {
            for (@Parallel int j = 0; j < columns; j++) {
                ret.matrix.set(i, j, matrix.get(i, j).round(precision))
            }
        }
        return ret
    }

    @Override
    Matrix augment(Matrix m) {
        return null
    }

    @Override
    List<Number> getTrace() {
        return null
    }
}
