package com.nn;

enum ActivationFunction {
    SIGMOID {
        @Override
        public double function(double x) {
            return 1 / (1 + Math.exp(-x));
        }

        @Override
        public double derivative(double x) {
            return function(x) * (1 - function(x));
        }
    },
    TANH {
        @Override
        public double function(double x) {
            return Math.tanh(x);
        }

        @Override
        public double derivative(double x) {
            return 1 - Math.pow(function(x), 2);
        }
    },
    RELU {
        @Override
        public double function(double x) {
            return Math.max(0, x);
        }

        @Override
        public double derivative(double x) {
            return x > 0 ? 1 : 0;
        }
    };

    public abstract double function(double x);

    public abstract double derivative(double x);

}
