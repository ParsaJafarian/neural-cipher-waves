package com.nn;

public enum CostFunction {

    /**
     * Quadratic cost function.
     * <br>
     * C = 1/2 * (y - a)^2
     * <br>
     * C' = a - y
     */
    QUADRATIC {
        @Override
        public Matrix function(Matrix y, Matrix a) {
            return a.subtract(y).map(x -> Math.pow((double) x, 2) / 2);
        }

        @Override
        public Matrix derivative(Matrix y, Matrix a) {
            return a.subtract(y);
        }
    };

    public abstract Matrix function(Matrix y, Matrix a);

    public abstract Matrix derivative(Matrix y, Matrix a);
}
