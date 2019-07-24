package it.gml

import groovy.transform.CompileStatic

@CompileStatic
class MatrixGenerator {

	static Matrix zeroMatrix(final int rows, final int columns) {
		final List<List<Number>> elems = new ArrayList<List<Number>>(rows)
		for (int i = 0; i< rows; i++) {
			elems[i] = new ArrayList<Number>(columns)
			for (int j = 0; j < columns; j++) {
				elems[i][j] = 0 as Number
			}
		}
		new Matrix(elems)
	}

	static Matrix diagonal(final int size, final Number elem) {
		final Matrix m = zeroMatrix(size, size)

		for(int i = 0; i < size; i++) {
			m[i][i] = elem
		}

		return m
	}

	static Matrix identity(final int size) {
		diagonal(size, 1)
	}

	static Matrix random(final int rows, final int columns) {
		final Matrix m = new Matrix(rows, columns)
		for (int i = 0; i< rows; i++){
			for (int j = 0; j<columns; j++){
				m[i][j] = Math.random() as Number
			}
		}

		return m

	}
}
