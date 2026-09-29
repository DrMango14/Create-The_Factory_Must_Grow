package com.drmangotea.tfmg.content.electricity.experimental.testing;

 class SparseMatrix {

        final SparseRow[] rows;

        SparseMatrix(int size) {
            rows = new SparseRow[size];

            for (int i = 0; i < size; i++)
                rows[i] = new SparseRow();
        }

        void add(
                int row,
                int column,
                Complex value
        ) {
            rows[row].add(column, value);
        }

        Complex get(
                int row,
                int column
        ) {
            return rows[row].get(column);
        }
    }