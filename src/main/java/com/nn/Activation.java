package com.nn;

import java.util.HashMap;

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
public enum Activation {
    /**
     * Sigmoid activation function.
     * <br>
     * f(x) = 1 / (1 + e^(-x))
     * <br>
     * f`(x) = f(x) * (1 - f(x))
     */
    SIGMOID {
        @Override
        public Matrix f(Matrix m) {
            return m.map(x -> 1 / (1 + Math.exp(-((double) x))));
        }

        public double f(double x) {
            return 1 / (1 + Math.exp(-x));
        }

        @Override
        public Matrix der(Matrix m) {
            return m.map(x -> (double) x * (1 - (double) x));
        }

        public double der(double x) {
            return x * (1 - x);
        }

        public String toString() {
            return "sigmoid";
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
        public Matrix f(Matrix m) {
            return m.map(x -> Math.tanh((double) x));
        }

        public double f(double x) {
            return Math.tanh(x);
        }

        @Override
        public Matrix der(Matrix m) {
            return m.map(x -> 1 - Math.pow((double) x, 2));
        }

        public double der(double x) {
            return 1 - Math.pow(x, 2);
        }

        public String toString() {
            return "tanh";
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
        public Matrix f(Matrix m) {
            return m.map(x -> Math.max(0, (double) x));
        }

        public double f(double x) {
            return Math.max(0, x);
        }

        @Override
        public Matrix der(Matrix m) {
            return m.map(x -> (double) ( x > 0 ? 1 : 0));
        }

        public double der(double x) {
            return x > 0 ? 1 : 0;
        }

        public String toString() {
            return "relu";
        }
    };

    static final HashMap<String, Activation> activationFunctions = new HashMap<>() {{
        put("sigmoid", Activation.SIGMOID);
        put("tanh", Activation.TANH);
        put("relu", Activation.RELU);
    }};

    /**
     * @param m matrix to apply the function to
     * @return a new matrix that is the result of applying the function to each element of the input matrix
     */
    public abstract Matrix f(Matrix m);

    public abstract double f(double x);

    /**
     * @param m matrix to apply the derivative function to
     * @return a new matrix that is the result of applying the derivative of the function to each element of the input matrix
     */
    public abstract Matrix der(Matrix m);

    public abstract double der(double x);

    public abstract String toString();

    public boolean equals(Activation a){
        return this.toString().equals(a.toString());
    }

    public boolean equals(String s){
        return this.toString().equals(s);
    }

    public static Activation getActivation(String activation) {
        if (!activationFunctions.containsKey(activation))
            throw new IllegalArgumentException("Activation function not found");
        return activationFunctions.get(activation);
    }

}
