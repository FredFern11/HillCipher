package HillCipher;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class CharMatrix {
    public char[][] data;
    public char filler = "~".charAt(0);
    public char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ',.!?".toCharArray();

    public CharMatrix(char[][] data) {this.data = data;}

    public CharMatrix(String message, int[] dimension){
        char[][] data = new char[dimension[1]][dimension[0]];
        int pointer = 0;
        for (int i = 0; i < dimension[1]; i++) {
            for (int j = 0; j < dimension[0]; j++) {
                if (pointer >= message.length()) {
                    data[i][j] = Character.valueOf(this.filler);
                    pointer++;
                }
                else {
                    data[i][j] = message.charAt(pointer);
                    pointer++;
                }
            }
        }
        this.data = data;
    }

    public static CharMatrix randomMatrix(int size) {
        CharMatrix matrix = new CharMatrix(new char[size][size]);
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.data[i][j] = matrix.alphabet[random.nextInt(matrix.alphabet.length-1)];
            }
        }
        return matrix;
    }

    public IntMatrix toInt() {
        IntMatrix newMatrix = new IntMatrix(new int[this.data.length][this.data[0].length]);
        // loop through the entirety of the matrix
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                // parse a component of the matrix to a char
                char character = (char) this.data[i][j];
                // search in the alphabet the character that matches the character in the matrix
                if (character == this.filler) {newMatrix.data[i][j] = -1; continue;}
                for (int k = 0; k < this.alphabet.length; k++) {
                    if (character == this.alphabet[k]) {
                        // return the index of the character in the alphabet
                        newMatrix.data[i][j] = k;
                    }
                }
            }
        }
        return newMatrix;
    }

    public String flatten(){
        String data = "";
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                if (this.data[i][j] == (this.filler)) {return data;}
                data += this.data[i][j];
            }
        }
        return data;
    }
    
    private int[] maxLen() {
        int[] maxLenList = new int[this.data[1].length];

        for (int i = 0; i < this.data[1].length; i++) {
            int maxLen = 0;
            for (int j = 0; j < this.data[0].length; j++) {
                int lenCase = String.valueOf(this.data[j][i]).length();
                if (lenCase > maxLen) {maxLen = lenCase;}
            }
            maxLenList[i] = maxLen;
        }
        return maxLenList;
    }

    public String repr() {
        String data = "";
        int width = this.data[0].length;
        int height = this.data.length;
        int[] maxWidth = maxLen();

        for (int i = 0; i < height; i++) {
            if (i == 0) {data += "⌈";}
            else if (i == height-1) {data += "⌊";}
            else {data += "|";}

            for (int j = 0; j < width; j++) {
                String component = String.valueOf(this.data[i][j]);
                int buffer = 0;
                if (j < width-1) {buffer = 2;}
                while (component.length() < maxWidth[j] + buffer) {
                    component += " ";
                }

                data += component;
            }

            if (i == 0) {data += "⌉";}
            else if (i == height-1) {data += "⌋";}
            else {data += "|";}

            data = i < this.data.length-1 ? data + "\n" : data;
        }
        return data;
    }
}
