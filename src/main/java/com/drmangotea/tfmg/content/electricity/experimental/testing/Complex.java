package com.drmangotea.tfmg.content.electricity.experimental.testing;

 public class Complex {
        double re;
        double im;

        Complex() {
            this(0, 0);
        }

        Complex(double re, double im) {
            this.re = re;
            this.im = im;
        }

        Complex add(Complex b) {
            return new Complex(re + b.re, im + b.im);
        }

        Complex sub(Complex b) {
            return new Complex(re - b.re, im - b.im);
        }

        Complex mul(Complex b) {
            return new Complex(
                    re * b.re - im * b.im,
                    re * b.im + im * b.re
            );
        }

        Complex div(Complex b) {
            double d = b.re * b.re + b.im * b.im;

            if (d < 1e-30)
                throw new ArithmeticException("Complex division by zero");

            return new Complex(
                    (re * b.re + im * b.im) / d,
                    (im * b.re - re * b.im) / d
            );
        }

        Complex neg() {
            return new Complex(-re, -im);
        }

        double abs() {
            return Math.hypot(re, im);
        }

        double phaseDegrees() {
            return Math.toDegrees(Math.atan2(im, re));
        }

        @Override
        public String toString() {
            return String.format(
                    "%.5f %+.5fj",
                    re, im
            );
        }
    }