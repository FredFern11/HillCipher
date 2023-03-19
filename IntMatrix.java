package HillCipher;

import java.util.Arrays;
import java.util.Random;

public class IntMatrix {
    public int[][] data;
    public char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ',.!?".toCharArray();

    public IntMatrix(int[][] dataChar) {
        this.data = dataChar;
    }

    public CharMatrix toChar() {
        CharMatrix newMatrix = new CharMatrix(new char[this.data.length][this.data[0].length]);
        // loop through the entirety of the matrix
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                // parse the value of a case in the matrix
                // double chaining makes sure that it will return an int independantly of the type of matrix
                int value = this.data[i][j];
                // index of the char in the alphabet attribute
                int index = value % this.alphabet.length;
                if (index < 0) {index += this.alphabet.length;}
                // applies the letter to the same case in the character array
                newMatrix.data[i][j] = this.alphabet[index];
            }
        }
        return newMatrix;
    }

    public static IntMatrix genIdentity(int matrixLenght) {
        IntMatrix matrix = new IntMatrix(new int[matrixLenght][matrixLenght]);
        for (int i = 0; i < matrixLenght; i++) {
            for (int j = 0; j < matrixLenght; j++) {
                if (i == j) {matrix.data[i][i] = 1;}
                else {matrix.data[i][j] = 0;}
            }

        }
        return matrix;
    }

    public static IntMatrix randomMatrix(int size) {
        IntMatrix matrix = new IntMatrix(new int[size][size]);
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix.data[i][j] = random.nextInt(101);
            }
        }
        return matrix;
    }


    public IntMatrix multi(IntMatrix matrix) {
        // check if the matricies are in the right shape
        if (!(this.data[0].length == matrix.data.length || this.data.length == matrix.data[0].length)) {
            System.out.println("Matrices not in good shape -> should be n x mn");
            return null;
        }

        IntMatrix result = new IntMatrix(new int[this.data.length][matrix.data[0].length]);

        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                int value = 0;
                for (int k = 0; k < this.data[0].length; k++) {
                    value += this.data[i][k] * matrix.data[k][j];
                }
                result.data[i][j] = value;
            }
        }
        return result;
    }

    public int determinant() {
        if (this.data.length == 2) {return this.data[0][0] * this.data[1][1] - this.data[0][1] * this.data[1][0];}

        int sum = 0;
        for (int i = 0; i < this.data.length; i++) {
            sum += Math.pow(-1, i) * this.data[0][i] * this.remainder(i).determinant();
        }

        return sum;
    }

    public IntMatrix remainder(int column) {
        IntMatrix result = new IntMatrix(new int[this.data.length - 1][this.data.length - 1]);
        int place = 0;
        for (int i = 1; i < this.data.length; i++) {
            for (int j = 0; j < this.data.length; j++) {
                if (j != column) {
                    result.data[place/result.data.length][place%result.data.length] = this.data[i][j];
                    place++;
                }
            }
        }
        return result;
    }

    public IntMatrix clone() {
        IntMatrix copy = new IntMatrix(new int[this.data.length][this.data[0].length]);
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[0].length; j++) {
                copy.data[i][j] = this.data[i][j];
            }
        }
        return copy;
    }

    public IntMatrix inverse() {
        System.out.println(this.repr());
        if (this.determinant() == 0) {return null;}

        IntMatrix clone = this.clone();
        IntMatrix identity = genIdentity(clone.data.length);

        for (int i = 0; i < clone.data.length; i++) {
//            System.out.println(i);
//            System.out.println(clone.repr());
//            System.out.println(identity.repr() + "\n\n");


            if (clone.data[i][i] == 0) {
//                System.out.println("swap");
                int row = i;
                while (clone.data[row][i] == 0) {row++;}

                clone.swapRows(i, row);
                identity.swapRows(i, row);
//                System.out.println(clone.repr());
//                System.out.println(identity.repr() + "\n\n");
            }

            for (int j = 0; j < clone.data.length; j++) {
                if (j != i && clone.data[j][i] != 0) {
                    int lcm = HillCipher.LCM(Math.abs(clone.data[i][i]), Math.abs(clone.data[j][i]));
                    if (lcm == 0) {return null;}

//                    System.out.println(clone.data[i][i] + " / " + clone.data[j][i] + "-> " + lcm);
                    int upFactor = lcm / clone.data[i][i];
                    int downFactor = lcm / clone.data[j][i];
//                    System.out.println("upFactor: " + upFactor);
//                    System.out.println("downFactor: " + downFactor);
                    for (int k = 0; k < clone.data.length; k++) {
//                        System.out.println("(" + i + "," + j + "," + k + ")" + ": " + clone.data[i][k] + " * " + upFactor + " - " + clone.data[j][k] + " * " + downFactor);
                        clone.data[j][k] = clone.data[i][k] * upFactor - clone.data[j][k] * downFactor;
                        identity.data[j][k] = identity.data[i][k] * upFactor - identity.data[j][k] * downFactor;
                    }
                }
            }
        }
        int[] diagonalValues = new int[clone.data.length];
        for (int i = 0; i < clone.data.length; i++) {
            diagonalValues[i] = clone.data[i][i];
        }
//        System.out.println("----");
        System.out.println(clone.repr());
        System.out.println(identity.repr());
//        System.out.println(Arrays.toString(diagonalValues));

        return identity.cleanUp(diagonalValues);
    }

    public IntMatrix cleanUp(int[] diagonalValues){
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data.length; j++) {
                if (this.data[i][j] % diagonalValues[i] == 0) {
                    this.data[i][j] /= diagonalValues[i];
                } else {
                    return null;
                }
            }
        }
        return this;
    }

    public void swapRows(int first, int second) {
        int[] intermediate = this.data[first];
        this.data[first] = this.data[second];
        this.data[second] = intermediate;
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
