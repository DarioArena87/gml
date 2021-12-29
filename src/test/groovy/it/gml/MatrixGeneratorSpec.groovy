package it.gml

import spock.lang.Specification

class MatrixGeneratorSpec extends Specification {
	def "Diagonal matrix is a all zero matrix except for elements on diagonal"() {
		given:
		Matrix d = MatrixGenerator.diagonal(4, 7)

		expect:
		d == new Matrix([
			[7, 0, 0, 0],
			[0, 7, 0, 0],
			[0, 0, 7, 0],
			[0, 0, 0, 7],
		])
	}
}
