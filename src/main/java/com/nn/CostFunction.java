package com.nn;

public enum CostFunction{

    /**
     * Quadratic cost function.
     * <br>
     * C = 1/2 * (y - a)^2
     * <br>
     * C' = a - y
     */
    MSE {
        @Override
        public Matrix f(Matrix y, Matrix a) {
            return a.subtract(y).map(x -> Math.pow(x, 2) / 2);
        }

        @Override
        public Matrix der(Matrix y, Matrix a) {
            return a.subtract(y);
        }
    },
    /**
     * Mean Absolute Error cost function.
     * <br>
     * C = |y - a|
     * <br>
     * C' = 1 if a > y, -1 otherwise
     */
    MAE {
        @Override
        public Matrix f(Matrix y, Matrix a) {
            return a.subtract(y).map(Math::abs);
        }

        @Override
        public Matrix der(Matrix y, Matrix a) {
            return a.subtract(y).map(x ->  x > 0 ? 1.0 : -1.0);
        }
    };

    public abstract Matrix f(Matrix y, Matrix a);

    public abstract Matrix der(Matrix y, Matrix a);
}
