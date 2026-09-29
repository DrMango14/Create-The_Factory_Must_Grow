package com.drmangotea.tfmg.content.electricity.experimental.testing;

import java.util.HashMap;

 class SparseRow {

        final HashMap<Integer, Complex> values =
                new HashMap<>();

        void add(int column, Complex value) {

            if (Math.abs(value.re) < 1e-15 &&
                    Math.abs(value.im) < 1e-15) {
                return;
            }

            Complex old = values.get(column);

            if (old == null) {
                values.put(
                        column,
                        new Complex(value.re, value.im)
                );
            } else {
                old.re += value.re;
                old.im += value.im;

                if (Math.abs(old.re) < 1e-15 &&
                        Math.abs(old.im) < 1e-15) {
                    values.remove(column);
                }
            }
        }

        Complex get(int column) {
            Complex c = values.get(column);

            if (c == null)
                return new Complex();

            return c;
        }
    }