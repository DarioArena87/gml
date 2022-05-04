package it.gml

import it.gml.CreateMatrix
import spock.lang.Specification

class MatrixGeneratorSpec extends Specification {
	def "Diagonal matrix is a all zero matrix except for elements on diagonal"() {
		given:
		Matrix d = CreateMatrix.diagonal(4, 7)

		expect:
		d == CreateMatrix.from([
			[7, 0, 0, 0],
			[0, 7, 0, 0],
			[0, 0, 7, 0],
			[0, 0, 0, 7],
		])
	}
}
