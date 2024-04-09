package com.nn;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
public class Matrix {
    private final int rows;
    private final int cols;
    protected final double[][] data;

    /**
     * @param rows number of rows
     * @param cols number of columns
     */
    protected Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0)
            throw new IllegalArgumentException("Invalid matrix size");

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

        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public Matrix(int[][] d) {
        this.rows = d.length;
        this.cols = d[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                this.data[i][j] = d[i][j];
    }

    /**
     * Turns a nxm matrix into a (n*m)x1 matrix
     *
     * @return a new matrix that is the vectorized version of this matrix
     */
    public Matrix flatten() {
        Matrix result = new Matrix(rows * cols, 1);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i * cols + j][0] = data[i][j];
        return result;
    }

    public static @NotNull Matrix ones(int rows, int cols) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = 1;
        return result;
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Matrix zeros(int rows, int cols) {
        return new Matrix(rows, cols);
    }

    public static @NotNull Matrix random(int rows, int cols) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = Math.random();
        return result;
    }

    /**
     * @return a deep copy of the data
     */
    public double[][] getData() {
        return data.clone();
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
    public Matrix hadamard(@NotNull Matrix m) {
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
    public Matrix map(Function<Double, Double> f) {
        Matrix result = new Matrix(rows, cols);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                result.data[i][j] = f.apply(data[i][j]);
        return result;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return cols;
    }

    @Override
    public String toString() {
        StringBuilder matrix = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.append(data[i][j]).append(" ");
            }
            matrix.append("\n");
        }
        return matrix.toString();
    }

    public boolean equals(@NotNull Matrix m) {
        if (rows != m.rows || cols != m.cols)
            return false;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (data[i][j] != m.data[i][j])
                    return false;
        return true;
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

    /**
     * @param r row
     * @param c column
     * @return the value at the specified row and column
     */
    public double get(int r, int c) {
        return data[r][c];
    }

    public void set(int r, int c, int i) {
        data[r][c] = i;
    }

    public boolean hasNan() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (Double.isNaN(data[i][j]))
                    return true;
        return false;
    }

    public Matrix addRow(){
        Matrix result = new Matrix(rows + 1, cols); // Create a new matrix with one additional row
        for (int i = 0; i < rows; i++) {
            if (cols >= 0) {
                System.arraycopy(data[i], 0, result.data[i], 0, cols); // Copy existing data
            }
        }
        return result;
    }

    public Matrix addColumn() {
        Matrix result = new Matrix(rows, cols + 1); // Create a new matrix with one additional column
        for (int i = 0; i < rows; i++) {
            if (cols >= 0) {
                System.arraycopy(data[i], 0, result.data[i], 0, cols); // Copy existing data
            }
        }
        return result;
    }

    public Matrix removeRow() {
        Matrix result = new Matrix(rows - 1, cols); // Create a new matrix with one less row
        for (int i = 0, k = 0; i < rows; i++) {
            if (i == rows - 1) continue; // Skip the row to be removed
            System.arraycopy(data[i], 0, result.data[k], 0, cols); // Copy existing data
            k++;
        }
        return result;
    }

    public Matrix removeColumn() {
        Matrix result = new Matrix(rows, cols - 1); // Create a new matrix with one less column
        for (int i = 0; i < rows; i++) {
            for (int j = 0, k = 0; j < cols; j++) {
                if (j == cols - 1) continue; // Skip the column to be removed
                result.data[i][k] = data[i][j]; // Copy existing data
                k++;
            }
        }
        return result;
    }

}
