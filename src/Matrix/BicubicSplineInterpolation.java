package Matrix;

import Interface.Solvable;
import Utils.Input;
import Utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BicubicSplineInterpolation extends Solvable {
    private Matrix inputMatrix = new Matrix(4, 4);
    private Matrix matrixX;
    private Matrix matrixF;
    private Matrix matrixA;
    private Matrix inverseX;
    private double a;
    private double b;
    private double result = 0;
    @Override
    public void readVariablesFromUserInput() {

    }

    @Override
    public void readVariablesFromTextFile() {
        Matrix mF = new Matrix();
        setMatrix(mF);
        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = Input.getScanner().next();
        System.out.println("");
        this.readOutputFileYesOrNo();
        String fileInputPath = "test/input/" + fileName;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))) {
            this.matrixF.initMatrix(16, 1);
            int row = 0;
            for (int i = 0; i < 4; i++) {
                String[] line = bufferedReader.readLine().split(" ");
                for (String s : line) {
                    this.matrixF.matrix[row++][0] = Double.parseDouble(s);
                }
            }

            String[] line = bufferedReader.readLine().split(" ");

            this.a = Double.parseDouble(line[0]);
            this.b = Double.parseDouble(line[1]);

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
    }

    @Override
    public void solve() {
        solution = "";
        inverseX = this.matrixX.getInverse();
        matrixA = inverseX.multiplyBy(matrixF);
        int rowA = 0;
        result = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                result += (this.matrixA.matrix[rowA++][0] * Math.pow(this.a, i) * Math.pow(this.b, j));
            }
        }
        solution += String.format("f(%.4f, %.4f) = %.4f\n", this.a, this.b, result);
        
    }

    public void displaySolutionToFile() {
        this.solve();
        Utils.printFile(solution, "/output/outputBicubic.txt");
        System.out.println("Jawaban akan terdapat pada folder output dengan nama file 'outputBicubic.txt'");
    }

    public void displaySolutionToTerminal() {
        this.solve();
        System.out.println("\n--> Input yang anda masukan akan diubah menjadi matrix Y sebagai berikut\n");
        this.matrixF.displayMatrix();

        System.out.println(
                "\n--> Kemudian akan dicari matrix X melalui persamaan bicubic spline interpolation yang telah diberikan");
        System.out.println("--> Akan didapat matrix X sebagi berikut\n");

        this.matrixX.displayMatrix();

        System.out.println("\n--> Persamaan matrix bicubic spline interpolation adalah y = Xa");
        System.out.println(
                "--> Pada fungsi ini akan dicari nilai matrix a dengan cara mengubah persamaan diatas menjadi a = (X^-1)y");
        System.out.println("--> Akan dicari matrix balikan dari matrix X sebagai berikut\n");
        // Matrix inverseX = this.matrixX.getInverse();
        inverseX.displayMatrix();

        System.out.println(
                "\n--> Setelah didapat X^-1 akan dicari nilai matrix a dengan mengkalikan matrix balikan X dengan matrix y dan akan diperoleh hasil sebagai berikut");
        // matrixA = inverseX.multiplyBy(matrixF);
        matrixA.displayMatrix();

        System.out.println("\n--> Matrix a diatas sebenarnya merepresentasikan konstanta aij dimana 0 <= i, j <= 3.");
        System.out.println("--> Nilai dari masing-masing konstanta a akan dituliskan di bawah ini\n");

        int rowA = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                System.out.printf("| a%d%d = %.4f\n", i, j, this.matrixA.matrix[rowA++][0]);
            }
        }

        System.out.printf(
                "\n--> Melalui nilai konstanta a yang telah didapat akan di cari nilai f(x,y) dengan x = %.4f dan y = %.4f",
                this.a, this.b);
        System.out.println("\n--> f(x,y) = \u03A3i=0,1,2,3 \u03A3j=0,1,2,3 ((aij)(x^i)(y^j))");
        System.out.println("--> Proses perhitungan nilai f(x,y) adalah sebagai berikut\n");

        rowA = 0;
        System.out.printf("f(%.4f, %.4f) = ", this.a, this.b);
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (rowA != 15) {
                    System.out.printf("(%.4f)(%.4f)^%d(%.4f)^%d + ", this.matrixA.matrix[rowA++][0], this.a, i, this.b,
                            j);
                } else {
                    System.out.printf("(%.4f)(%.4f)^%d(%.4f)^%d", this.matrixA.matrix[rowA++][0], this.a, i, this.b, j);
                }
            }
        }

        rowA = 0;
        System.out.printf("\nf(%.4f, %.4f) = ", this.a, this.b);
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (rowA != 15) {
                    System.out.printf("(%.4f)(%.4f)(%.4f) + ", this.matrixA.matrix[rowA++][0], Math.pow(this.a, i),
                            Math.pow(this.b, j));
                } else {
                    System.out.printf("(%.4f)(%.4f)(%.4f)", this.matrixA.matrix[rowA++][0], Math.pow(this.a, i),
                            Math.pow(this.b, j));
                }
            }
        }

        rowA = 0;
        // result = 0;
        System.out.printf("\nf(%.4f, %.4f) = ", this.a, this.b);
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                if (rowA > 0) {
                    if (this.matrixA.matrix[rowA][0] * Math.pow(this.a, i) * Math.pow(this.b, j) < 0) {
                        System.out.printf(" - %.4f",
                                Math.abs(this.matrixA.matrix[rowA][0] * Math.pow(this.a, i) * Math.pow(this.b, j)));
                    } else if (this.matrixA.matrix[rowA][0] * Math.pow(this.a, i) * Math.pow(this.b, j) >= 0) {
                        System.out.printf(" + %.4f",
                                Math.abs(this.matrixA.matrix[rowA][0] * Math.pow(this.a, i) * Math.pow(this.b, j)));
                    }
                } else {
                    System.out.printf("%.4f", this.matrixA.matrix[rowA][0] * Math.pow(this.a, i) * Math.pow(this.b, j));
                }
                // result += (this.matrixA.matrix[rowA++][0] * Math.pow(this.a, i) *
                // Math.pow(this.b, j));
            }
        }

        System.out.printf("\nf(%.4f, %.4f) = %.4f\n", this.a, this.b, result);
    }

    @Override
    public void displaySolution() {
        if (!getIsPrintFile()){
            this.displaySolutionToTerminal();
        } else {
            this.displaySolutionToFile();
        }
    }



    public void setMatrix(Matrix m) {
        this.matrixF = m;
        matrixX = new Matrix(16, 16);
        matrixA = new Matrix(16, 1);

        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        matrixX.matrix[row][col] = Math.pow(x, i) * Math.pow(y, j);
                        matrixX.matrix[row + 4][col] = (i == 0) ? 0 : i * Math.pow(x, i - 1) * Math.pow(y, j);
                        matrixX.matrix[row + 8][col] = (j == 0) ? 0 : j * Math.pow(x, i) * Math.pow(y, j - 1);
                        matrixX.matrix[row + 12][col] = (i == 0 || j == 0) ? 0
                                : i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1);
                        col++;
                    }
                }
                row++;
            }
        }
    }

    public Matrix getInputMatrix() {
        return this.inputMatrix;
    }
    public void setX(double[] x) {
        a = x[0];
        b = x[1];
    }
    public double[] getX() {
        return new double[] {a,b};
    }
    public void setVariablesFromFile(File file) throws Exception{
        // inputMatrix
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            bufferedReader.mark(1000);
            int row = 0, col = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {   
                int tmpCol = line.split(" ").length;
                if (tmpCol < col) {
                    continue;
                }
                col = Math.max(tmpCol, col);
                row += 1;
            }

            inputMatrix.initMatrix(row, col);
            bufferedReader.reset();

            int i = 0;
            while((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
                if (elmts.length == 0) continue;
                if (elmts.length == 2) {
                    // kemungkinan baris terakhir
                    // baris terakhir panjangnya pasti 2
                    String[] elmtsLastLine = line.split(" ");
                    a = Double.parseDouble(elmtsLastLine[0]);
                    b = Double.parseDouble(elmtsLastLine[1]);
                    break;
                }
                for (int j = 0; j < 4; j++)
                {
                    inputMatrix.matrix[i][j] = Double.parseDouble(elmts[j]);
                }

                i++;
            }
        } catch (IOException e) {
            // Handle case saat file not found atau ada IO error.
            throw new Exception("File tidak ditemukan.");
        } catch (NumberFormatException e) {
            // Handle case saat ada nonnumeric di input.
            throw new Exception("Sepertinya terdapat suatu nonnumeric value di file Anda. Program berhenti.");
        } catch (IllegalArgumentException e) {
            // Jumlah elemen di setiap baris tidak konsisten.
            throw new Exception("Jumlah elemen pada setiap baris tidak konsisten.");
        } catch (Exception e) {
            throw e;
        }
        Matrix m16x1 = new Matrix(16, 1);
        for(int i = 0; i < 16; i++) {
            m16x1.matrix[i][0] = inputMatrix.matrix[i/4][i%4];
        }
        setMatrix(m16x1);
    }

}