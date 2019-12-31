GML - Groovy Matrix Library 
===========================

GML is a library aimed at supporting matrices and matrices operation with a Groovy-friendly API. 

Using other libraries, you may found the same issues of using doubles or BigDecimal in Java: the formers has all the standard operators supports but lacks precision and you can't fully control some aspects such as rounding or its scale and you can address this issues using the latter but it's API is very unpleasant to use in Java for complex calculation. Groovy with it's operators overloading comes to the rescue coercing automatically the dataype used to store numbers to avoid rounding errors. 

---
For matrices there's a lot of libraries but maybe they lack some proper OO encapsulation of operations:

###### Wrong way:
```java
DMatrixRMaj A = new DMatrixRMaj(new double[][] {{1, 3}, {2, 5}});
DMatrixRMaj b = new DMatrixRMaj(new double[][] {{2}, {1}});

DMatrixRMaj c = new DMatrixRMaj(2, 1);

CommonOps_DDRM.mult(CommonOps_DDRM.invert(A), b, c);
```

###### Right way:
```groovy
Matrix A = new Matrix([
                [1, 3], 
                [2, 5]
		    ])
Vector b = new Vector([2, 1])

Vector c = A.invert().multiply(b) // or better c = A**-1 * b
```


Or maybe they're almost fine but misses some methods which could really simplify writing code using Groovy:

###### Wrong way:
```java
RealMatrix m = MatrixUtils.createRealMatrix(new double[][]{ {0d,0d,1d}, {0d,1d,0d}, {1d,0d,0d}});
m.setColumnVector(2, MatrixUtils.createRealVector(new double[] {1d, 2d, 3d});
RealMatrix n = new Array2DRowRealMatrix(new double[][]{ {1d,2d,3d}, {2d,5d,7d}, {1d,7d,9d}});
RealMatrix p = m.multiply(n);
```

###### Right way:
```groovy
Matrix m = new Matrix([
                [0, 0, 1], 
                [0, 1, 0],
                [1, 0, 0],
            ])

m[0][2] = 1
m[1][2] = 2
m[2][2] = 3

Matrix n = new Matrix([
                [1, 2, 3], 
                [2, 5, 7],
                [1, 7, 9],
            ])

Matrix p = m * n
```

In GML the objective is to create the same abstractions that Groovy introduces to simplify math operations without loosing precision using the right data type under the hood.

#Getting Started

GML is open source under GPLv3. To join developing GML you can:
1. Fork the repository and submit a pull request if you can 
  * improve some functionality
  * suggest a groovy way to accomplish the same task (there is always a "groovier" way in Groovy)
  * implement new functionality.
2. Write some tests to cover an uncovered code branch or for new functionalities to be implemented (we use [Spock framework](http://spockframework.org/))
3. Submit a issue if you find a bug which you don't know how to deal or how to fix it
