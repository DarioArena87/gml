package it.gml

class GaussJordan {
    static class EchelonFormComputation {
        Matrix result
        int numberOfRowsExchanges = 0
    }

    static EchelonFormComputation transformToEchelonForm(final Matrix m){
        final Matrix result = new Matrix(m.copyOfElems)
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

    static EchelonFormComputation transformToReducedEchelonForm(final Matrix m) {
        final EchelonFormComputation ecf = transformToEchelonForm(m)
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

        return ecf.tap {
            result = echelonForm
        }
    }

    private static int findPivotRowIndex(final Matrix m, final int startPivotIndex) {
        final int column = startPivotIndex
        for (int i = startPivotIndex; i < m.rows; i++) {
            if (m.elems[i][column] != 0) {
                return i
            }
        }
        return -1
    }

    private static void exchangeRows(final Matrix m, final int rowIndexA, final int rowIndexB) {
        final List<Number> rowB = m.elems[rowIndexB]
        m.elems[rowIndexB] = m.elems[rowIndexA]
        m.elems[rowIndexA] = rowB
    }
}