package com.nn.algo;

import java.util.HashMap;

/**
 * Enum for loss functions used in the neural network.
 */
public enum Loss {
    /**
     * Mean Squared Error loss function.
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
     * Mean Absolute Error loss function.
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

    static final HashMap<String, Loss> losses = new java.util.HashMap<>() {{
        put("mse", Loss.MSE);
        put("mae", Loss.MAE);
    }};

    public abstract Matrix f(Matrix y, Matrix a);

    public abstract Matrix der(Matrix y, Matrix a);

    public static Loss getLoss(String loss) {
        if (!losses.containsKey(loss))
            throw new IllegalArgumentException("Loss function not found");
        return losses.get(loss);
    }

    public boolean equals(Loss l){
        return this.toString().equals(l.toString());
    }

    public boolean equals(String s){
        return this.toString().equals(s);
    }
}
