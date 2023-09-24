package Matrix;

import Interface.Solvable;
import Utils.Input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Interpolasi implements Solvable {
    Matrix matrix;
    double x;

    public void init(Matrix matrix) {
        this.matrix = matrix;
    }

    public void readFromFile() {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = userInput.next();
        String fileInputPath = "test/input/" + fileName;
        List<String> titiks = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))){
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                titiks.add(line);
            }

            this.matrix.initMatrix(titiks.size() - 1, titiks.size());

            for (int i = 0; i < this.matrix.row; i++) {
                String[] titik = titiks.get(i).split(" ");
                double x = Double.parseDouble(titik[0]);
                double y = Double.parseDouble(titik[1]);
                for (int j = 0; j < this.matrix.col; j++) {
                    if (j == this.matrix.col - 1) {
                        this.matrix.matrix[i][j] = y;
                    }else {
                        this.matrix.matrix[i][j] = Math.pow(x,j);
                    }
                }
            }

            this.x = Double.parseDouble(titiks.get(titiks.size() - 1));

        } catch (IOException e) {
            // Handle case saat file not found atau ada IO error.
            System.out.println("File tidak ditemukan.");
        } catch (NumberFormatException e) {
            // Handle case saat ada nonnumeric di input.
            System.out.println("Sepertinya terdapat suatu nonnumeric value di file Anda. Program berhenti.");
        } catch (IllegalArgumentException e) {
            // Jumlah elemen di setiap baris tidak konsisten.
            System.out.println("Jumlah elemen pada setiap baris tidak konsisten, program berhenti.");
        }

        userInput.close();
    }

    public void readFromUserInput() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Masukkan  banyak titik: ");
        int n = Input.getInt("Banyak titik harus lebih besar dari 0", (num) -> num > 0);

        this.matrix.initMatrix(n, n+1);

        for (int i = 0; i < this.matrix.row; i++) {
            System.out.printf("Masukan titik ke %d: ", i + 1);
            String input = userInput.nextLine();
            String[] titik = input.split(" ");
            double x = Double.parseDouble(titik[0]);
            double y = Double.parseDouble(titik[1]);

            for (int j = 0; j < this.matrix.col; j++) {
                if (j == this.matrix.col - 1) {
                    this.matrix.matrix[i][j] = y;
                }else {
                    this.matrix.matrix[i][j] = Math.pow(x,j);
                }
            }
        }

        System.out.print("Masukkan nilai x yang ingin ditafsir nilai f(x) nya: ");
        this.x = Input.getDouble();
        userInput.close();
    }

    public void solve() {
        double result = 0;
        SPL spl = new SPL(matrix.row, matrix.col);
        spl.init(matrix);
        spl.setMethod(SPL.SPLMethod.GaussJordan);
        spl.setShowProcess(true);
        System.out.printf("--> Bentuk persamaan interpolasi yang akan terbentuk dari %d titik adalah sebagai berikut\n\n", spl.augmentedMatrix.row);
        System.out.print("f(x) = ");
        for (int i = spl.augmentedMatrix.row - 1; i >=  0; i--) {
            if (i > 1) {
                System.out.printf("(a%d)x^%d", i + 1, i);
            } else if (i == 1) {
                System.out.printf("(a%d)x", i + 1);
            } else {
                System.out.printf("(a%d)", i + 1);
            }
            if (i != 0) {
                System.out.print(" + ");
            }
        }
        System.out.println("\n");

        System.out.println("--> Untuk dapat mencari persamaan interpolasi, titik yang anda inputkan akan diubah menjadi sebuah SPL sebagai berikut\n");

        for (int i = 0; i < spl.augmentedMatrix.row; i++) {
            System.out.printf("-> titik (%.4f, %.4f)\n| ", spl.augmentedMatrix.matrix[i][1], spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]);
            for (int j = spl.augmentedMatrix.row - 1; j >=  0; j--) {
                if (j > 1) {
                    System.out.printf("(%.4f)^%d(a%d)", spl.augmentedMatrix.matrix[i][1], j, j + 1);
                } else if (j == 1) {
                    System.out.printf("(%.4f)(a%d)", spl.augmentedMatrix.matrix[i][1], j + 1);
                } else {
                    System.out.printf("(a%d)", j + 1);
                }
                if (j != 0) {
                    System.out.print(" + ");
                }
            }
            System.out.printf(" = %.4f\n| ", spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]);
            for (int j = spl.augmentedMatrix.row - 1; j >=  0; j--) {
                if (j >= 1) {
                    System.out.printf("%.4f(a%d)", spl.augmentedMatrix.matrix[i][j], j + 1);
                } else {
                    System.out.printf("(a%d)", j + 1);
                }
                if (j != 0) {
                    if (spl.augmentedMatrix.matrix[i][j - 1] >= 0) {
                        System.out.print(" + ");
                    } else if (spl.augmentedMatrix.matrix[i][j - 1] < 0) {
                        System.out.print(" - ");
                    }
                }
            }
            System.out.printf(" = %.4f\n\n", spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]);
        }

        System.out.println("\n--> Persamaan diatas kemudian akan diubah ke dalam bentuk augmented matrix dan akan dengan selesaikan secara SPL dengan metode Gauss Jordan");
        System.out.println("--> Dalam penyelesaian SPL ini konstanta a1 = x1 dst");
        System.out.println("--> Penyelesaiannya adalah sebagai berikut\n");

        spl.solve();

        System.out.println("\n--> Setelah didapat solusi dari SPL diatas maka bisa dibangun persamaan interpolasi sebagai berikut");

        System.out.print("f(x) = ");
        for (int i = spl.augmentedMatrix.row - 1; i >=  0; i--) {
            if (i > 1) {
                System.out.printf("%.4fx^%d", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]), i);
            } else if (i == 1) {
                System.out.printf("%.4fx", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]));
            } else {
                System.out.printf("%.4f", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]));
            }
            if (i != 0) {
                if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] >= 0) {
                    System.out.print(" + ");
                } else if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] < 0) {
                    System.out.print(" - ");
                }
            }
            result += (spl.augmentedMatrix.matrix[i][matrix.col - 1] * Math.pow(this.x, i));
        }
        System.out.printf("\n\n--> Melalui persamaan interpolasi di atas akan ditafsir nilai dari f(x) dimana x = %.4f\n\n", this.x);

        System.out.printf("f(%.4f) = ", this.x);
        for (int i = spl.augmentedMatrix.row - 1; i >=  0; i--) {
            if (i > 1) {
                System.out.printf("%.4f(%.4f^%d)", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]), this.x, i);
            } else if (i == 1) {
                System.out.printf("%.4f(%.4f)", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]), this.x);
            } else {
                System.out.printf("%.4f", Math.abs(spl.augmentedMatrix.matrix[i][matrix.col - 1]));
            }
            if (i != 0) {
                if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] >= 0) {
                    System.out.print(" + ");
                } else if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] < 0) {
                    System.out.print(" - ");
                }
            }
        }
        System.out.println("");
        System.out.printf("f(%.4f) = ", this.x);
        for (int i = spl.augmentedMatrix.row - 1; i >=  0; i--) {
            if (i >= 1) {
                System.out.printf("%.4f(%.4f)", Math.abs(spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]), Math.pow(this.x, i));
            } else {
                System.out.printf("%.4f", Math.abs(spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]));
            }
            if (i != 0) {
                if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] >= 0) {
                    System.out.print(" + ");
                } else if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] < 0) {
                    System.out.print(" - ");
                }
            }
        }
        System.out.println("");
        System.out.printf("f(%.4f) = ", this.x);
        for (int i = spl.augmentedMatrix.row - 1; i >=  0; i--) {
            System.out.printf("%.4f", Math.abs(spl.augmentedMatrix.matrix[i][spl.augmentedMatrix.col - 1]) * Math.pow(this.x, i));
            if (i != 0) {
                if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] >= 0) {
                    System.out.print(" + ");
                } else if (spl.augmentedMatrix.matrix[i - 1][spl.augmentedMatrix.col - 1] < 0) {
                    System.out.print(" - ");
                }
            }
        }
        System.out.println("");

        System.out.printf("f(%.4f) = %.4f\n", this.x, result);
    }
}
