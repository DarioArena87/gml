package it.gml

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import it.gml.utils.Format

@CompileStatic
class Matrix {

    private static final Integer ZERO = Integer.valueOf(0)

    final List<List<Number>> elems

    Matrix(final int rows, final int cols) {
        assert rows > 0: "Rows must be positive"
        assert cols > 0: "Columns must be positive"

        elems = new ArrayList<>(rows)
        rows.times { final row -> elems[row] = new ArrayList<Number>(cols) }
    }

    Matrix(final List<List<Number>> elems) {
        assert elems: "Matrix must not be empty"
        assert elems.collect { it.size() }.unique().size() == 1: "Number of columns for each row must be equal"

        this.elems = elems
    }

    int getRows() {
        elems.size()
    }

    int getColumns() {
        elems[0].size()
    }

    List<Number> getAt(final int row) {
        elems[row]
    }

    void putAt(final int rowIndex, final List<Number> row) {
        assert row.size() == rows: "New row must be of the same size as other rows"

        elems[rowIndex] = row
    }

    String toString() {
        final List<List<String>> elemsString = elems.collect { final row -> row.collect Format.toPlainString }
        final int maxDigits = elemsString.flatten().collect { it.toString().size() }.max()
        final Closure<String> leftPadMaxDigits = Format.leftPad.rcurry(maxDigits)

        elemsString.collect { "| ${it.collect(leftPadMaxDigits).join('  ')} |" }.join('\n')
    }

    @Override
    int hashCode() {
        Objects.hash(elems)
    }

    @Override
    boolean equals(final Object obj) {
        if (obj == null || this.class != obj.class) {
            return false
        }

        final Matrix b = obj as Matrix

        return equalsDimensions(b) && elems == b.elems
    }

    boolean equalsDimensions(final Matrix b) {
        rows == b.rows && columns == b.columns
    }

    Matrix plus(final Matrix b) {
        assert equalsDimensions(b): "Dimensions mismatch"

        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j] + b[i][j]
            }
        }

        return result
    }

    Matrix plus(final Number scalar) {
        final Matrix result = new Matrix(rows, columns)

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j] + scalar
            }
        }

        return result
    }

    Matrix minus(final Matrix b) {
        assert equalsDimensions(b): "Dimensions mismatch"

        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j] - b[i][j]
            }
        }

        return result
    }

    Matrix minus(final Number scalar) {
        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j] - scalar
            }
        }

        return result
    }

    Matrix multiply(final Matrix b) {
        assert columns == b.rows: "Number of left matrix's columns and right matrix's rows must be equal"

        final Matrix result = new Matrix(rows, b.columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < b.columns; j++) {
                result[i][j] = ZERO
                for (int k = 0; k < columns; k++) {
                    result[i][j] += elems[i][k] * b[k][j]
                }
            }
        }

        return result
    }

    Matrix multiply(final Number scalar) {

        final Matrix result = new Matrix(rows, columns)

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j] * scalar
            }
        }

        return result
    }

    Matrix power(final long exponent) {
        assert rows == columns: "Matrix must be a square matrix"

        Matrix a = exponent > 0 ? this : this.invert()
        final Matrix i = MatrixGenerator.identity(rows)
        Matrix result = i

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
        final Matrix result = new Matrix(columns, rows)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[j][i] = elems[i][j]
            }
        }
        return result
    }

    Number getDeterminant() {
        assert rows == columns: "Cannot compute determinant on non square matrix"

        if (rows == 1) {
            return elems[0][0]
        }

        if (rows == 2) {
            return elems[0][0] * elems[1][1] - elems[0][1] * elems[1][0]
        }

        final def efc = GaussJordan.getEchelonForm(this)
        return (-1)**efc.numberOfRowsExchanges * (efc.result.trace.inject(1, { final Number a, final Number b -> a * b }) as Number)
    }

    List<List<Number>> getCopyOfElems() {
        final List<List<Number>> newElems = new ArrayList<List<Number>>(elems)
        for (int i = 0; i < rows; i++) {
            newElems[i] = new ArrayList<Number>(elems[i])
        }

        return newElems
    }

    Matrix minor(final int row, final int column) {
        final List<List<Number>> newElems = copyOfElems
        newElems.remove(row)
        newElems*.remove(column)
        new Matrix(newElems)
    }

    Number getSparsity() {
        elems.flatten().findAll().size() / (rows * columns)
    }

    Number getDensity() {
        (1 - sparsity) as Number
    }

    @CompileDynamic
    Matrix invert() {
        assert determinant != 0: "Singular matrix cannot be inverted"

        use(GaussJordan) {
            final Matrix identity = MatrixGenerator.identity(rows)

            final Matrix result = augment(identity).reducedEchelonForm.result

            final List<List<Number>> elemsResult = new ArrayList<>(rows)

            for (int i = 0; i < rows; i++) {
                elemsResult[i] = result[i][columns..<result.columns]
            }

            new Matrix(elemsResult)
        }
    }

    Matrix round(final int precision) {
        final Matrix result = new Matrix(rows, columns)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                result[i][j] = elems[i][j].toBigDecimal().round(precision)
            }
        }
        return result
    }

    Matrix augment(final Matrix m) {
        assert m.rows == rows: "Matrices' row number must be equal"

        final List<List<Number>> newElems = new ArrayList<>(rows)
        for (int i = 0; i < m.rows; i++) {
            newElems[i] = [elems[i], m[i]].flatten() as List<Number>
        }

        new Matrix(newElems)
    }

    List<Number> getTrace() {
        (0..<rows).collect { elems[it][it] }
    }
}
