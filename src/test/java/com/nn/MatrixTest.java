package com.nn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    @Test
    public void testConstructor() {
        Matrix m = new Matrix(2, 3);
        assert m.getRows() == 2;
        assert m.getCols() == 3;
    }

    @Test
    public void testScalarMultiply1() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m.multiply(2);
        Matrix actual = new Matrix(new double[][]{{2, 4, 6}, {8, 10, 12}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testScalarMultiply2() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m.multiply(0);
        Matrix actual = new Matrix(new double[][]{{0, 0, 0}, {0, 0, 0}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testMap1() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m.map(x -> (double) x * 2);
        Matrix actual = new Matrix(new double[][]{{2, 4, 6}, {8, 10, 12}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testMap2() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m.map(x -> (double) x /4);
        Matrix actual = new Matrix(new double[][]{{0.25, 0.5, 0.75}, {1, 1.25, 1.5}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testDotProduct1() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        Matrix result = m1.dot(m2);
        Matrix actual = new Matrix(new double[][]{{22, 28}, {49, 64}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testDotProduct2() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertThrows(IllegalArgumentException.class, () -> m1.dot(m2));
    }

    @Test
    public void testEquals1() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertTrue(m1.equals(m2), "Expected " + m2 + " but got " + m1);
    }

    @Test
    public void testEquals2() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 7}});
        assertFalse(m1.equals(m2), "Expected " + m2 + " but got " + m1);
    }

    @Test
    public void testHadamardProduct1() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m1.multiply(m2);
        Matrix actual = new Matrix(new double[][]{{1, 4, 9}, {16, 25, 36}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testHadamardProduct2() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assertThrows(IllegalArgumentException.class, () -> m1.multiply(m2.transpose()));
    }

    @Test
    public void testTranspose() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m.transpose();
        Matrix actual = new Matrix(new double[][]{{1, 4}, {2, 5}, {3, 6}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testSubtract() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m1.subtract(m2);
        Matrix actual = new Matrix(new double[][]{{0, 0, 0}, {0, 0, 0}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testAdd() {
        Matrix m1 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix m2 = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Matrix result = m1.add(m2);
        Matrix actual = new Matrix(new double[][]{{2, 4, 6}, {8, 10, 12}});
        assertTrue(result.equals(actual), "Expected " + actual + " but got " + result);
    }

    @Test
    public void testConstructor2() {
        Matrix m = new Matrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        assert m.getRows() == 2;
        assert m.getCols() == 3;
    }
}
