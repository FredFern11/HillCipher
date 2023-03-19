package HillCipher;

import org.w3c.dom.html.HTMLIsIndexElement;

import javax.swing.text.html.HTML;
import java.util.Arrays;
import java.util.Objects;

public class Test {
    public static void matrixMultiplication() {
        IntMatrix matrix1 = new IntMatrix(new int[][]{{12,31,67}, {90,32,47}, {95,78,65}});
        IntMatrix matrix2 = new IntMatrix(new int[][]{{71,23,79}, {21,46,78}, {91,28,47}});
        IntMatrix encryptedMatrix = matrix1.multi(matrix2);
        CharMatrix encryptMatrix = encryptedMatrix.toChar();
        System.out.println(encryptedMatrix.repr());
        System.out.println(encryptMatrix.repr());

    }

    public static void buildMatrixWithMessage() {
        String message = "This message needs to be encrypted as soon as possible! I swear to god if you don't do it I'll be really mad at you, don't play with me please!";
        int[] dimension = HillCipher.messDim(message.length(), 40);
        CharMatrix matrixBuilt = new CharMatrix(message, dimension);
        String flattenMessage = matrixBuilt.flatten();
        System.out.println(message);
        System.out.println(matrixBuilt.repr());
        System.out.println(flattenMessage);
    }

    public static void genPasswordRandom() {
        System.out.println((HillCipher.genKey(10, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ',.!?".toCharArray())).repr());
    }

    public static void findDimensionRandomMessage() {
        int[] dimension = HillCipher.messDim(232, 20);
        System.out.println(dimension[0] + "x" + dimension[1]);
    }

    public static void encryptAMessage() {
        String message = "These messages are spam texts, also known as robotexts. The fact is, most spam texts don't come from another mobile phone,lsmah";
        System.out.println(Arrays.toString(HillCipher.encrypt(message, 20)));
    }

    public static void findDeterminant() {
        IntMatrix matrix = new IntMatrix(new int[][]{{1,0,0},{0,1,0},{0,0,1}});
        System.out.println(matrix.repr());
        System.out.println(matrix.determinant());
    }

    public static void findInverseKey() {
        IntMatrix random;
        IntMatrix inverse = null;
        int i=0;
        while (inverse == null) {
            System.out.println(i);
            random = IntMatrix.randomMatrix(3);
            inverse = random.inverse();
            i++;
        }
    }

    public static void decypher() {
        String message = "These messages are spam texts, also known as robotexts. The fact is, most spam texts don't come from another mobile phone,lsmah";
        System.out.println(message);
        int[] dimension = HillCipher.messDim(message.length(), 15);
        IntMatrix matrixMess = new CharMatrix(message, dimension).toInt();
        IntMatrix key = IntMatrix.randomMatrix(dimension[0]);
        System.out.println(key.repr());
        System.out.println(key.toChar().flatten());
        IntMatrix encrypted = matrixMess.multi(key);
        System.out.println(encrypted.toChar().flatten());
        IntMatrix inverseKey = key.inverse();
        IntMatrix decrypted = encrypted.multi(inverseKey);
        System.out.println(decrypted.toChar().flatten());
    }
}
