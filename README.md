GML - Groovy Matrix Library 
===========================

GML is a library aimed at supporting matrices and matrices operation with a Groovy user-friendly API. 

Using other libraries, you may found the same issues of using float/doubles or BigDecimal in Java: 
* **using floating point numbers**:
they supports all of the standard operations but they lack precision ([This answer](https://stackoverflow.com/a/3730040)
on stackoverlow explains it very clearly) and you can't fully control some aspects such as rounding or their scale
* **using BigDecimals**: they address all floating point number issues but their API is very unpleasant to use in Java 
for complex calculation. 

Groovy with it's operators overloading comes to the rescue, coercing automatically the dataype used to store numbers 
to avoid rounding errors. 

---

To handle matrices in java there are a lot of very good libraries but maybe they lack some proper OO encapsulation
of the operations:

###### [EJML](http://ejml.org/) way:
```java
DMatrixRMaj A = new DMatrixRMaj(new double[][] {{1, 3}, {2, 5}});
DMatrixRMaj b = new DMatrixRMaj(new double[][] {{2}, {1}});

DMatrixRMaj c = new DMatrixRMaj(2, 1);

CommonOps_DDRM.mult(CommonOps_DDRM.invert(A), b, c);
```

###### GML way:
```groovy
Matrix A = new Matrix([
                [1, 3], 
                [2, 5]
	   ])
Vector b = new Vector(2, 1).transpose()

Vector c = A**-1 * b // or to be more explicit: A.invert().multiply(b) 
```

Or maybe they're almost fine but they misses some of the methods which could really simplify writing 
code using Groovy:

###### [Apache commons math](https://commons.apache.org/proper/commons-math/userguide/linear.html) way:
```java
RealMatrix m = MatrixUtils.createRealMatrix(new double[][]{ {0d,0d,1d}, {0d,1d,0d}, {1d,0d,0d}});
m.setColumnVector(2, MatrixUtils.createRealVector(new double[] {1d, 2d, 3d});
RealMatrix n = new Array2DRowRealMatrix(new double[][]{ {1d,2d,3d}, {2d,5d,7d}, {1d,7d,9d}});
RealMatrix p = m.multiply(n);
```

###### GML way:
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

In GML the objective is to create the same abstractions that Groovy introduces to simplify math operations without
loosing precision using the right data type under the hood.

#Getting Started

GML is open source under GPLv3. To join developing GML you can:
1. Fork the repository and submit a pull request if you can 
  * improve some functionality
  * suggest a groovy way to accomplish the same task (there is always a "groovier" way in Groovy)
  * implement new functionality.
2. Write some tests to cover an uncovered code branch or for new functionalities to be implemented (in GML the
   testing framework is [Spock](http://spockframework.org/))
3. Submit a issue if you find a bug which you don't know how to deal or how to fix it
