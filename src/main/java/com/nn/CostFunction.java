package com.nn;

public enum CostFunction{

    /**
     * Quadratic cost function.
     * <br>
     * C = 1/2 * (y - a)^2
     * <br>
     * C' = a - y
     */
    QUADRATIC {
        @Override
        public Matrix f(Matrix y, Matrix a) {
            return a.subtract(y).map(x -> Math.pow((double) x, 2) / 2);
        }

        @Override
        public Matrix der(Matrix y, Matrix a) {
            return a.subtract(y);
        }
    };

    public abstract Matrix f(Matrix y, Matrix a);

    public abstract Matrix der(Matrix y, Matrix a);
}
