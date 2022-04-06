package it.gml

interface Matrix {

    abstract int getRows();

    abstract int getColumns();

    abstract Number[] getAt(int row)

    abstract void putAt(int rowIndex, Number[] row)

    abstract boolean equalsDimensions(Matrix b);

    abstract boolean elementsEquals(Matrix other);

    abstract Matrix plus(Matrix other)

    abstract Matrix plus(Number scalar)

    abstract Matrix minus(Matrix other)

    abstract Matrix minus(Number scalar)

    abstract Matrix multiply(Matrix b)

    abstract Matrix multiply(Number scalar)

    abstract Matrix power(final long exponent)

    abstract Matrix negative()

    /** Dot product alias */
    abstract Number xor(Matrix b)

    abstract Number dotProduct(Matrix b)

    abstract Matrix transpose()

    abstract Number getDeterminant()

    abstract Matrix minor(int row, int column)

    abstract Number getSparsity()

    abstract Number getDensity()

    abstract Matrix invert()

    abstract Matrix round(int precision)

    abstract Matrix augment(Matrix m)

    abstract List<Number> getTrace()

}
