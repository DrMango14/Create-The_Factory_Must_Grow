package com.drmangotea.tfmg.content.electricity.experimental.simulation;

public class ComplexValue {
    public double real;
    public double imag;

    public ComplexValue(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public static final ComplexValue ZERO = new ComplexValue(0, 0);
    public static final ComplexValue ONE = new ComplexValue(1, 0);

    public ComplexValue plus(ComplexValue b) {
        return new ComplexValue(this.real + b.real, this.imag + b.imag);
    }

    public ComplexValue minus(ComplexValue b) {
        return new ComplexValue(this.real - b.real, this.imag - b.imag);
    }

    public ComplexValue times(ComplexValue b) {
        return new ComplexValue(this.real * b.real - this.imag * b.imag, this.real * b.imag + this.imag * b.real);
    }

    public ComplexValue div(ComplexValue b) {
        double denom = b.real * b.real + b.imag * b.imag;
     //   if (denom == 0) throw new ArithmeticException("Division by zero in ComplexValue solver.");
        if (denom == 0) return this;
        return new ComplexValue(
                (this.real * b.real + this.imag * b.imag) / denom,
                (this.imag * b.real - this.real * b.imag) / denom
        );
    }
    public static ComplexValue fromPolar(double r, double deg) {
        double rad = Math.toRadians(deg);
        return new ComplexValue(r * Math.cos(rad), r * Math.sin(rad));
    }


    public ComplexValue reciprocal() {
        return ComplexValue.ONE.div(this);
    }

    public double abs() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double phaseDegrees() {
        return Math.toDegrees(Math.atan2(imag, real));
    }

    @Override
    public String toString() {
        return String.format("%.4f V ∠ %.2f°", abs(), phaseDegrees());
    }
}