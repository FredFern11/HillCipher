package HillCipher;

import java.util.Random;
import java.util.*;

public class HillCipher {
    public static String[] encrypt(String message, int keySize) {
        // generate the dimension of the message and the key
        int[] dimension = messDim(message.length(), keySize);
        // build a matrix containing the message
        CharMatrix rawMatrix = new CharMatrix(message, dimension);
        // convert the letter matrix to integer
        IntMatrix numberMatrix = rawMatrix.toInt();
        System.out.println(numberMatrix.repr());
        // generate a key with the height of the message matrix
        IntMatrix key = genKey(dimension[0], rawMatrix.alphabet);
        System.out.println(key.repr());
        // compute the product between the message and the key matricies
        IntMatrix encryMatrix = numberMatrix.multi(key);
        System.out.println(encryMatrix.repr());
        // return the encrypted message and its key
        return new String[]{encryMatrix.toChar().flatten(), key.toChar().flatten()};
    }

    public static IntMatrix genKey(int lenght, char[] alphabet) {
        // generate a square matrix
        IntMatrix key = new IntMatrix(new int[lenght][lenght]);
        Random random = new Random();
        // traverse matrix case by case
        for (int i = 0; i < lenght; i++) {
            for (int j = 0; j < lenght; j++) {
                // generate a random number to put in the matrix
                key.data[i][j] = random.nextInt(alphabet.length-1);
            }
        }
        return key;
    }

    public static List<int[]> sort(List<int[]> array, int index) {
        List<List<int[]>> result = new LinkedList<>();

        for (int i = 0; i < array.size(); i+=2) {
            if (i + 1 == array.size()) {
                List<int[]> list = new ArrayList<>();
                list.add(array.get(i));
                result.add(list);
            } else {
                if (array.get(i)[index] > array.get(i + 1)[index]) {
                    List<int[]> list = new ArrayList<>();
                    list.add(array.get(i + 1));
                    list.add(array.get(i));
                    result.add(list);
                } else {
                    List<int[]> list = new ArrayList<>();
                    list.add(array.get(i));
                    list.add(array.get(i + 1));
                    result.add(list);
                }
            }
        }

        while (result.size() != 1) {
            for (int i = 0; i < result.size(); i += 1) {
                if (i + 1 < result.size()) {
                    List<int[]> component = new ArrayList<>();
                    while (!(result.get(i).isEmpty() || result.get(i + 1).isEmpty())) {
                        if (result.get(i).get(0)[index] > result.get(i + 1).get(0)[index]) {
                            component.add(result.get(i + 1).get(0));
                            result.get(i + 1).remove(0);
                        } else {
                            component.add(result.get(i).get(0));
                            result.get(i).remove(0);
                        }
                    }

                    if (result.get(i).isEmpty()) {
                        component.addAll(result.get(i + 1));
                    } else if (result.get(i + 1).isEmpty()) {
                        component.addAll(result.get(i));
                    }
                    result.remove(i);
                    result.set(i, component);
                }
            }
        }
        return result.get(0);
    }

    public static int LCM(int first, int second) {
        int lcm = first > second ? first : second;

        while (!(lcm % first == 0 && lcm % second == 0)) {lcm++;}
        return lcm;
    }

    public static int[] messDim(int messLen, int tolerance) {
//        plus la longueur de la clé est proche de tolerance, plus de point
//        plus le nombre de rest est petit, plus de point

        List<int[]> dimRest = new ArrayList<>();
        int i = 2;
        int idealDim = 0;
        int upper = messLen * tolerance / 100;
        int lower = messLen * tolerance / 100;
        boolean running = true;

        while (running) {
            if (Math.sqrt(upper++) % 1 == 0) {
                idealDim = (int) Math.sqrt(upper-1);
                running = false;
            } else if (Math.sqrt(lower--) % 1 == 0) {
                idealDim = (int) Math.sqrt(lower+1);
                running = false;
            }
        }

        while ((i-1)*(i-1) < messLen) {
            int lenght = messLen;

            int rest = 0;

            while (lenght % i != 0) {
                lenght++;
                rest++;
            }

            dimRest.add(new int[]{i, rest});
            i++;
        }

        dimRest = sort(dimRest, 1);

        int low = 100000;
        int width = 0;
        int height = 0;

        for (int j = 0; j < dimRest.size(); j++) {
//            System.out.println(Arrays.toString(dimRest.get(j)));
            int points = Math.abs(idealDim - dimRest.get(j)[0]) + dimRest.get(j)[1];
//            System.out.println(points);

            if (points < low) {
                low = points;
                height = dimRest.get(j)[0];
                width = (messLen+dimRest.get(j)[1])/height;
            }
        }
        return new int[]{height, width};
    }
}


