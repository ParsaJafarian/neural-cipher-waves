package com.nn;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Matrix class for matrix operations such as :
 * <ul>
 *     <li>Addition</li>
 *     <li>Subtraction</li>
 *     <li>Transpose</li>
 *     <li>Hadamard product</li>
 *     <li>Dot product</li>
 *     <li>Map</li>
 *     <li>Max index</li>
 * </ul>
 */
class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    /**
     * @param rows number of rows
     * @param cols number of columns
     */
    public Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Invalid matrix size");
        if (rows == 1 && cols == 1)
            throw new IllegalArgumentException("Use a scalar instead of a matrix");

        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    /**
     * @param data 2D array of doubles
     */
    public Matrix(double[] @NotNull [] data) {
        if (data.length == 0 || data[0].length == 0)
            throw new IllegalArgumentException("Invalid matrix size");
        if (data.length == 1 && data[0].length == 1)
            throw new IllegalArgumentException("Use a scalar instead of a matrix");

        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    /**
     * @return a deep copy of the matrix
     */
    public Matrix clone() {
        double[][] newData = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            System.arraycopy(data[i], 0, newData[i], 0, cols);
        return new Matrix(newData);
    }

    /**
     * @param m matrix to add
     * @return a new matrix that is the sum of this matrix and m
     */
    public Matrix add(@NotNull Matrix m) {
        if (rows != m.rows || cols != m.cols)
            throw new IllegalArgumentException("Invalid matrix size");

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = data[i][j] + m.data[i][j];
        return result;
    }

    /**
     * @param m matrix to subtract
     * @return a new matrix that is the difference of this matrix and m
     */
    public Matrix subtract(@NotNull Matrix m) {
        if (rows != m.rows || cols != m.cols)
            throw new IllegalArgumentException("Invalid matrix size");

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = data[i][j] - m.data[i][j];
        return result;
    }

    /**
     * @return a new matrix that is the transpose of this matrix
     */
    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[j][i] = data[i][j];
        return result;
    }

    /**
     * Hadamard product is the element-wise product of two matrices
     *
     * @param m matrix to multiply
     * @return a new matrix that is the Hadamard product of this matrix and m
     */
    public Matrix multiply(@NotNull Matrix m) {
        if (rows != m.rows || cols != m.cols)
            throw new IllegalArgumentException("Invalid matrix size");

        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = data[i][j] * m.data[i][j];
        return result;
    }

    /**
     * Dot product is the matrix product of two matrices
     *
     * @param m matrix to multiply
     * @return a new matrix that is the dot product of this matrix and m
     */
    public Matrix dot(@NotNull Matrix m) {
        if (cols != m.rows)
            throw new IllegalArgumentException("Invalid matrix size" + rows + "x" + cols + " " + m.rows + "x" + m.cols);

        Matrix result = new Matrix(rows, m.cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < m.cols; j++)
                for (int k = 0; k < cols; k++)
                    result.data[i][j] += data[i][k] * m.data[k][j];
        return result;
    }

    /**
     * @param scalar scalar to multiply
     * @return a new matrix that is the product of this matrix and the scalar
     */
    public Matrix multiply(double scalar) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = data[i][j] * scalar;
        return result;
    }

    /**
     * map applies a function to each element of the matrix
     *
     * @param f function to apply
     * @return a new matrix that is the result of applying the function to each element of this matrix
     */
    public Matrix map(Function f) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = (double) f.apply(data[i][j]);
        return result;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    /**
     * @return the index of the maximum element in the matrix
     */
    public int maxIndex() {
        if (cols != 1)
            throw new IllegalArgumentException("Invalid matrix size");

        double max = Double.NEGATIVE_INFINITY;
        int index = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (data[i][j] > max) {
                    max = data[i][j];
                    index = i;
                }
        return index;
    }
}
