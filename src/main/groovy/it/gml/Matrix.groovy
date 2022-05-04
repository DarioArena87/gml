package it.gml

import it.gml.utils.Format
import org.codehaus.groovy.util.StringUtil

import java.util.function.BiFunction

import static it.gml.MatrixGenerator.identity

class Matrix {

    private final Number[][] matrix

    Matrix(int rows, int cols) {
        assert rows > 0: "Rows must be positive"
        assert cols > 0: "Columns must be positive"

        matrix = new Number[rows][cols]
        matrix.each {
            Arrays.fill(it, 0)
        }
    }

    Matrix(List<List<Number>> elements) {
        assert elements: "Matrix must not be empty"
        assert elements.collect { it.size() }.unique().size() == 1: "Number of columns for each row must be equal"

        def rows = elements.size()
        def columns = elements.first().size()
        matrix = new Number[rows][columns]
        for (int i in 0..<rows) {
            for (int j in 0..<columns) {
                matrix[i][j] = elements[i][j]
            }
        }
    }

    int getRows() {
        matrix.length
    }

    int getColumns() {
        matrix[0].length
    }

    Number[] getAt(int row) {
        matrix[row]
    }

    void putAt(int rowIndex, Number[] row) {
        assert row.length == columns: "New row must be of the same size as other rows"
        matrix[rowIndex] = row
    }

    String toString() {
        List<List<Number>> elements = matrix
        def maxScale = elements.flatten()
            .collect { Number n -> n.toBigDecimal().stripTrailingZeros() }
            .max { n -> n.unscaledValue().toString().length() }
            .unscaledValue().toString().length()

        matrix.collect { row ->
            "| ${row.collect { Format.leftPad(it.toBigDecimal().stripTrailingZeros().toPlainString(), maxScale + 1)}.join(' ')} |"
        }.join('\n')
    }

    @Override
    int hashCode() {
        matrix.hashCode()
    }

    @Override
    boolean equals(Object obj) {
        if (obj instanceof Matrix) {
            return equalsDimensions(obj) && elementsEquals(obj)
        }

        return false
    }

    boolean equalsDimensions(Matrix b) {
        rows == b.rows && columns == b.columns
    }

    boolean elementsEquals(Matrix other) {
        for (i in 0..<rows) {
            for (j in 0..<columns) {
                if (matrix[i][j] != other[i][j]) {
                    return false
                }
            }
        }

        return true
    }

    private static elementWise(Matrix a, Matrix b, Matrix result, BiFunction<Number, Number, Number> operation) {
        for (i in (0..<result.rows)) {
            for (j in (0..<result.columns)) {
                result[i][j] = operation.apply(a[i][j], b[i][j])
            }
        }
    }

    private static elementWise(Matrix a, Number number, Matrix result, BiFunction<Number, Number, Number> operation) {
        for (i in (0..<result.rows)) {
            for (j in (0..<result.columns)) {
                result[i][j] = operation.apply(a[i][j], number)
            }
        }
    }

    Matrix plus(Matrix other) {
        assert equalsDimensions(other): "Dimensions mismatch"

        Matrix result = new Matrix(rows, columns)
        elementWise(this, other, result) { a, b -> a + b }

        return result
    }

    Matrix plus(Number scalar) {
        Matrix result = new Matrix(rows, columns)
        elementWise(this, scalar, result) { a, b -> a + b }
        return result
    }

    Matrix minus(Matrix other) {
        assert equalsDimensions(other): "Dimensions mismatch"

        Matrix result = new Matrix(rows, columns)
        elementWise(this, other, result) { a, b -> a - b }
        return result
    }

    Matrix minus(Number scalar) {
        Matrix result = new Matrix(rows, columns)
        elementWise(this, scalar, result) { a, b -> a - b }
        return result
    }

    Matrix multiply(Matrix b) {
        assert columns == b.rows: "Number of left matrix's columns and right matrix's rows must be equal"

        Matrix result = new Matrix(rows, b.columns)
        for (i in (0..<result.rows)) {
            for (j in (0..<result.columns)) {
                result[i][j] = (0..<columns).collect { k -> this[i][k] * b[k][j]}.sum() as Number
            }
        }

        return result
    }

    Matrix multiply(Number scalar) {
        Matrix result = new Matrix(rows, columns)
        elementWise(this, scalar, result) { a, b -> a * b }
        return result
    }

    Matrix power(final long exponent) {
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

    Matrix negative() {
        Matrix result = new Matrix(rows, columns)
        for (i in (0..<result.rows)) {
            for (j in (0..<result.columns)) {
                result[i][j] = -this[i][j]
            }
        }
        return result
    }

    /** Dot product alias */
    Number xor(Matrix b) {
        dotProduct(b)
    }

    Number dotProduct(Matrix b) {
        (this * b.transpose())[0][0]
    }

    Matrix transpose() {
        Matrix result = new Matrix(columns, rows)
        for (i in (0..<result.rows)) {
            for (j in (0..<result.columns)) {
                result[i][j] = this[j][i]
            }
        }
        return result
    }

    Number getDeterminant() {
        assert rows == columns: "Cannot compute determinant on non square matrix"

        if (rows == 1) {
            return matrix[0][0]
        }

        if (rows == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        }

        final EchelonFormComputation efc = GaussJordan.getEchelonForm(this)
        return (-1)**efc.numberOfRowsExchanges * efc.result.trace.product()
    }

    Matrix minor(int row, int column) {
        List<List<Number>> result = []
        matrix.each {
            result << it.toList()
        }

        result.remove(row)
        result*.remove(column)

        return new Matrix(result)
    }

    Number getSparsity() {
        def elements = matrix as List<List<Number>>
        elements.flatten().findAll().size() / (rows * columns)
    }

    Number getDensity() {
        (1 - sparsity) as Number
    }

    Matrix invert() {
        assert determinant != 0: "Singular matrix cannot be inverted"

        final Matrix augmentedMatrix = augment(identity(rows))
        final Matrix result = GaussJordan.getReducedEchelonForm(augmentedMatrix).result
        return new Matrix(result.matrix.collect { it[columns..<result.columns] })
    }

    Matrix round(int precision) {
        List<List<Number>> result = []
        for (i in 0..<rows) {
            result[i] = []

            for (j in 0..<columns) {
                result[i][j] = matrix[i][j].toBigDecimal().round(precision)
            }
        }

        return new Matrix(result)
    }

    Matrix augment(Matrix m) {
        assert m.rows == rows: "Matrices' row number must be equal"

        List<Number[]> result = new ArrayList<>(rows)
        (0..<rows).each {
            result[it] = this[it] + m[it]
        }
        return new Matrix(result)
    }

    List<Number> getTrace() {
        (0..<rows).collect { matrix[it][it] }
    }

}
