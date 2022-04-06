package it.gml

class EchelonFormComputation {
    Matrix result
    int numberOfRowsExchanges = 0
}

@Category(Matrix)
class GaussJordan {

    EchelonFormComputation getEchelonForm(){
        final Matrix result = CreateMatrix.of(this.rows, this.columns)
        (0..<this.rows).each { i ->
            (0..<this.columns).each { j ->
                result[i][j] = this[i][j]
            }
        }
        int numberOfRowsExchanges = 0
        for (int pivotIndex = 0; pivotIndex < result.rows; pivotIndex++) {
            if (!result[pivotIndex][pivotIndex]) {
                int nonZeroPivotRowIndex = findPivotRowIndex(result, pivotIndex + 1)
                if (nonZeroPivotRowIndex == -1) {
                    continue
                }
                exchangeRows(result, nonZeroPivotRowIndex, pivotIndex)
                numberOfRowsExchanges++
            }

            Number pivot = result[pivotIndex][pivotIndex]

            for (int i = pivotIndex + 1; i < result.rows; i++) {
                Number factor = result[i][pivotIndex] / pivot
                for (int j = pivotIndex; j < result.columns; j++) {
                    result[i][j] -= factor * result[pivotIndex][j]
                }
            }
        }

        return new EchelonFormComputation(result: result, numberOfRowsExchanges: numberOfRowsExchanges)
    }

    EchelonFormComputation getReducedEchelonForm() {
        final EchelonFormComputation ecf = this.getEchelonForm()
        final Matrix echelonForm = ecf.result
        for (int pivotIndex = echelonForm.rows - 1; pivotIndex >= 0; pivotIndex--) {
            Number pivot = echelonForm[pivotIndex][pivotIndex]

            for (int j = echelonForm.columns - 1; j >= 0; j--) {
                echelonForm[pivotIndex][j] /= pivot
            }

            for (int i = pivotIndex - 1; i >= 0; i--) {
                for (int j = echelonForm.columns - 1; j >= 0; j--) {
                    echelonForm[i][j] -= echelonForm[i][pivotIndex] * echelonForm[pivotIndex][j]
                }
            }
        }

        ecf.result = echelonForm

        return ecf
    }

    void exchangeRows(Matrix m, int rowIndexA, int rowIndexB) {
        def temp = m[rowIndexA]
        m[rowIndexA] = m[rowIndexB]
        m[rowIndexB] = temp
    }

    int findPivotRowIndex(Matrix matrix, int startPivotIndex) {
        final int column = startPivotIndex
        for (int i = startPivotIndex; i < matrix.rows; i++) {
            if (matrix[i][column] != 0) {
                return i
            }
        }
        return -1
    }
}
