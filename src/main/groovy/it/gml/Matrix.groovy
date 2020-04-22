package it.gml


import groovy.transform.CompileStatic
import it.gml.utils.Format
import it.gml.utils.Product

import static it.gml.MatrixGenerator.identity

@CompileStatic
class Matrix {

    final List<List<Number>> elements

    Matrix(final int rows, final int cols) {
        assert rows > 0: "Rows must be positive"
        assert cols > 0: "Columns must be positive"

        elements = new ArrayList<>(rows)
        rows.times { final row -> elements[row] = new ArrayList<Number>(cols) }
    }

    Matrix(final List<List<Number>> elements) {
        assert elements: "Matrix must not be empty"
        assert elements.collect { it.size() }.unique().size() == 1: "Number of columns for each row must be equal"

        this.elements = elements
    }

    int getRows() {
        elements.size()
    }

    int getColumns() {
        elements[0].size()
    }

    List<Number> getAt(final int row) {
        elements[row]
    }

    void putAt(final int rowIndex, final List<Number> row) {
        assert row.size() == rows: "New row must be of the same size as other rows"

        elements[rowIndex] = row
    }

    String toString() {
        final List<List<String>> elementsString = elements.collectNested Format.toPlainString
        final int maxDigits = elementsString.flatten().collect { it.toString().size() }.max()
        final Closure<String> leftPadMaxDigits = Format.leftPad.rcurry(maxDigits)

        elementsString.collect { "| ${it.collect(leftPadMaxDigits).join('  ')} |" }.join('\n')
    }

    @Override
    int hashCode() {
        Objects.hash(elements)
    }

    @Override
    boolean equals(final Object obj) {
        if (obj == null || !obj in Matrix) {
            return false
        }

        final Matrix b = obj as Matrix

        return equalsDimensions(b) && elements == b.elements
    }

    boolean equalsDimensions(final Matrix b) {
        rows == b.rows && columns == b.columns
    }

    Matrix plus(final Matrix b) {
        assert equalsDimensions(b): "Dimensions mismatch"

        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elements[i][j] + b[i][j]
            }
        }

        return result
    }

    Matrix plus(final Number scalar) {
        new Matrix(elements.collectNested { Number elem -> elem + scalar })
    }

    Matrix minus(final Matrix b) {
        assert equalsDimensions(b): "Dimensions mismatch"

        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elements[i][j] - b[i][j]
            }
        }

        return result
    }

    Matrix minus(final Number scalar) {
        new Matrix(elements.collectNested { Number elem -> elem - scalar })
    }

    Matrix multiply(final Matrix b) {
        assert columns == b.rows: "Number of left matrix's columns and right matrix's rows must be equal"

        final Matrix result = new Matrix(rows, b.columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < b.columns; j++) {
                result[i][j] = 0 as Number
                for (int k = 0; k < columns; k++) {
                    result[i][j] += elements[i][k] * b[k][j]
                }
            }
        }

        return result
    }

    Matrix multiply(final Number scalar) {
        new Matrix(elements.collectNested { Number elem -> elem * scalar })
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
        multiply(-1)
    }

    /** Dot product */
    Number xor(final Matrix b) {
        dotProduct(b)
    }

    Number dotProduct(final Matrix b) {
        (transpose() * b)[0][0]
    }

    Matrix transpose() {
        new Matrix(elements.transpose())
    }

    Number getDeterminant() {
        assert rows == columns: "Cannot compute determinant on non square matrix"

        if (rows == 1) {
            return elements[0][0]
        }

        if (rows == 2) {
            return elements[0][0] * elements[1][1] - elements[0][1] * elements[1][0]
        }

        final GaussJordan.EchelonFormComputation efc = GaussJordan.getEchelonForm(this)
        return (-1)**efc.numberOfRowsExchanges * Product.of(efc.result.trace)
    }

    List<List<Number>> getCopyOfElements() {
        elements.collect { it.collect() }
    }

    Matrix minor(final int row, final int column) {
        new Matrix(
            copyOfElements.tap {
                it.remove(row)
                it*.remove(column)
            }
        )
    }

    Number getSparsity() {
        elements.flatten().findAll().size() / (rows * columns)
    }

    Number getDensity() {
        (1 - sparsity) as Number
    }

    Matrix invert() {
        assert determinant != 0: "Singular matrix cannot be inverted"

        final Matrix augmentedMatrix = augment(identity(rows))
        final Matrix result = GaussJordan.getReducedEchelonForm(augmentedMatrix).result
        new Matrix(result.elements.collect { it[columns..<result.columns] })
    }

    Matrix round(final int precision) {
        new Matrix(elements.collectNested { Number elem -> elem.toBigDecimal().round(precision) })
    }

    Matrix augment(final Matrix m) {
        assert m.rows == rows: "Matrices' row number must be equal"

        new Matrix(elements.indexed().collect { int rowNumber, List<Number> row -> row + m[rowNumber] })
    }

    List<Number> getTrace() {
        (0..<rows).collect { elements[it][it] }
    }
}
