package com.nn;

/**
 * Enum for activation functions used in the neural network.
 * <p>
 *     The enum contains the following activation functions:
 *     <ul>
 *         <li>Sigmoid</li>
 *         <li>Tanh</li>
 *         <li>ReLU</li>
 *     </ul>
 * </p>
 */
enum ActivationFunction {
    /**
     * Sigmoid activation function.
     * <br>
     * f(x) = 1 / (1 + e^(-x))
     * <br>
     * f`(x) = f(x) * (1 - f(x))
     */
    SIGMOID {
        @Override
        public Matrix function(Matrix m) {
            return m.map(x -> 1 / (1 + Math.exp(-((double) x))));
        }

        public double function(double x) {
            return 1 / (1 + Math.exp(-x));
        }

        @Override
        public Matrix derivative(Matrix m) {
            return m.map(x -> (double) x * (1 - (double) x));
        }

        public double derivative(double x) {
            return x * (1 - x);
        }
    },
    /**
     * Tanh activation function.
     * <br>
     * f(x) = tanh(x)
     * <br>
     * f`(x) = 1 - f(x)^2
     */
    TANH {
        @Override
        public Matrix function(Matrix m) {
            return m.map(x -> Math.tanh((double) x));
        }

        public double function(double x) {
            return Math.tanh(x);
        }

        @Override
        public Matrix derivative(Matrix m) {
            return m.map(x -> 1 - Math.pow((double) x, 2));
        }

        public double derivative(double x) {
            return 1 - Math.pow(x, 2);
        }
    },
    /**
     * ReLU activation function.
     * <br>
     * f(x) = max(0, x)
     * <br>
     * f`(x) = 1 if x > 0, 0 otherwise
     */
    RELU {
        @Override
        public Matrix function(Matrix m) {
            return m.map(x -> Math.max(0, (double) x));
        }

        public double function(double x) {
            return Math.max(0, x);
        }

        @Override
        public Matrix derivative(Matrix m) {
            return m.map(x -> (double) x > 0 ? 1 : 0);
        }

        public double derivative(double x) {
            return x > 0 ? 1 : 0;
        }
    };

    /**
     * @param m matrix to apply the function to
     * @return a new matrix that is the result of applying the function to each element of the input matrix
     */
    public abstract Matrix function(Matrix m);

    public abstract double function(double x);

    /**
     * @param m matrix to apply the derivative function to
     * @return a new matrix that is the result of applying the derivative of the function to each element of the input matrix
     */
    public abstract Matrix derivative(Matrix m);

    public abstract double derivative(double x);

}
