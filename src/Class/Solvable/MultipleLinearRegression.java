package Class.Solvable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import Class.Matrix;
import Utils.Input;
import Utils.Utils;

public class MultipleLinearRegression extends Solvable {
    public Matrix matrix; // input
    private Matrix mlrMatrix; // bentuk regresi
    private SPL spl;
    private double x[]; // nilai yang ingin diestimasi
    private String persamaan = "";
    private double result = 0;
    private boolean isPrintFile = false;

    @Override
    public void readVariablesFromUserInput() {
        System.out.println("Banyak sampel data:"); // Jumlah data point
        int dataPointAmount = Input.getInt();

        System.out.println("Banyak peubah x:"); // Jumlah predictor
        int predictorAmount = Input.getInt();

        for (int i = 0; i < predictorAmount; i++) {
            System.out.print("x" + (i + 1) + " ");
        }
        System.out.print("y");

        System.out.println("");
        Matrix m = new Matrix(dataPointAmount, predictorAmount + 1);
        try {
            m.readMatrixFromUserInput();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        x = new double[predictorAmount];
        System.out.println("Nilai yang akan ditaksir:");
        for (int i = 0; i < predictorAmount; i++)
            System.out.print("x" + (i + 1) + " ");
        System.out.println("");
        Input.getScanner().nextLine();
        String ln = Input.getScanner().nextLine();
        String[] elmts = ln.split(" ");
        for (int i = 0; i < predictorAmount; i++) {
            x[i] = Double.parseDouble(elmts[i]);
        }
        readOutputFileYesOrNo();
        setMatrix(m);
        setState(State.Unsolved);
        setupMlrMatrix();
    }

    @Override
    public void readVariablesFromTextFile() {
        Scanner scanner = Input.getScanner();

        System.out.println("Masukkan nama file beserta ekstensinya.");
        System.out.print("(dir: test/input): ");
        String fileName = scanner.next();
        scanner.nextLine();
        readOutputFileYesOrNo();
        String fileInputPath = "test/input/" + fileName;
        List<String> titiks = new LinkedList<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileInputPath))) {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                titiks.add(line);
            }

            int col = titiks.get(0).split(" ").length;
            this.matrix = new Matrix(titiks.size() - 1, col);

            for (int i = 0; i < this.matrix.row; i++) {
                String[] titik = titiks.get(i).split(" ");
                for (int j = 0; j < this.matrix.col; j++) {
                    double z = Double.parseDouble(titik[j]);
                    this.matrix.matrix[i][j] = z;
                }
            }

            this.matrix.displayMatrix();

            String[] stringX = titiks.get(titiks.size() - 1).split(" ");
            // System.out.println(stringX.toString());
            this.x = new double[col - 1];

            for (int i = 0; i < col - 1; i++) {
                this.x[i] = Double.parseDouble(stringX[i]);
            }

            setState(State.Unsolved);
            setupMlrMatrix();

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
        solution = ""; // reset biar kalau solve lagi ga numpuk
        persamaan = "";
        this.spl = new SPL(mlrMatrix.row, mlrMatrix.col - 1);
        spl.setMatrix(mlrMatrix)
                .setShowProcess(false)
                .solve();

        persamaan += "f(x) = ";
        persamaan += String.format("%.3f", spl.x[0].getConstant());
        for (int i = 1; i < spl.x.length; i++) {
            if (spl.x[i].getConstant() > 0) {
                persamaan += String.format(" + %.3fx%d", Math.abs(spl.x[i].getConstant()), i);
            } else if (spl.x[i].getConstant() < 0) {
                persamaan += String.format(" - %.3fx%d", Math.abs(spl.x[i].getConstant()), i);
            }
        }

        result = getEstimate();

        String s = "";

        s += "\nEstimate:\n";
        s += "f(";
        s += String.format("%.3f", x[0]);
        for (int i = 1; i < x.length; i++) {
            s += String.format(",%.3f", x[i]);
        }
        s += (") = ");
        s += String.format("%.3f\n", result);

        solution += String.format("%s\n%s", persamaan, s);
        setState(State.Solved);
    }

    @Override
    public void displaySolution() {
        if (!isPrintFile) {
            this.displaySolutionToTerminal();
        } else {
            this.displaySolutionToFile();
        }
    }

    public void init(Matrix matrix) {
        this.matrix = matrix;
    }

    public void displaySolutionToFile() {
        this.solve();
        Utils.printFile(solution, "outputRegresi.txt");
    }

    public void displaySolutionToTerminal() {
        this.solve();
        System.out.println(solution);
    }

    public void display() {
        System.out.print("f(x) = ");
        System.out.printf("%.3f", spl.x[0].getConstant());
        for (int i = 1; i < spl.x.length; i++) {
            System.out.printf(" + %.3fx%d", spl.x[i].getConstant(), i);
        }

        System.out.print("\nEstimate:\n");
        System.out.print("f(");
        System.out.printf("%.3f", x[0]);

        for (int i = 1; i < x.length; i++) {
            System.out.printf(",%.3f", x[i]);
        }
        System.out.print(") = ");
        System.out.printf("%.3f\n", getEstimate());
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getX() {
        return this.x;
    }

    public double getEstimate() {
        double result = 0;
        result += spl.x[0].getConstant();
        // System.out.printf("%.3f + ", spl.x[0].getConstant());
        for (int i = 1; i < mlrMatrix.row; i++) {
            // System.out.printf("%.3f * %.3f + ", spl.x[i].getConstant(), x[i-1]);
            result += spl.x[i].getConstant() * x[i - 1];
        }

        return result;
    }

    public void setIsPrintFile(boolean b) {
        this.isPrintFile = b;
    }

    public void setMatrix(Matrix m) {
        this.matrix = m;
    }

    public void setupMlrMatrix() {
        if (getState() == State.UninitializedVariable)
            throw new Error("Variable not initialized");

        this.mlrMatrix = new Matrix(this.matrix.col, this.matrix.col + 1);

        for (int m = 0; m < this.matrix.col; m++) {
            for (int n = 0; n < this.matrix.col + 1; n++) {

                double total = 0;
                for (int i = 0; i < this.matrix.row; i++) {
                    if (m == 0 && n == 0) {
                        total += 1;
                    } else if (m == 0) {
                        total += this.matrix.matrix[i][n - 1];
                    } else if (n == 0) {
                        total += this.matrix.matrix[i][m - 1];
                    } else {
                        total += this.matrix.matrix[i][m - 1] * this.matrix.matrix[i][n - 1];
                    }
                }
                this.mlrMatrix.matrix[m][n] = total;
            }
        }
    }

    public void setVariablesFromFile(File file) throws Exception {

        Matrix matrix = new Matrix();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.mark(1000);
            int row = 0, col = 0;
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int tmpCol = line.split(" ").length;
                if (tmpCol < col) {
                    continue;
                }
                col = Math.max(tmpCol, col);
                row += 1;
            }

            matrix.initMatrix(row, col);
            bufferedReader.reset();

            int i = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] elmts = line.split(" ");
                if (elmts.length == 0)
                    continue;
                if (elmts.length == matrix.col - 1) {
                    // kemungkinan baris terakhir
                    // baris terakhir panjangnya pasti 1 kurang dari
                    String[] elmtsLastLine = line.split(" ");
                    x = new double[elmtsLastLine.length];
                    for (int j = 0; j < elmtsLastLine.length; j++) {
                        x[j] = Double.parseDouble(elmtsLastLine[j]);
                    }
                    break;
                }
                for (int j = 0; j < elmts.length; j++) {
                    matrix.matrix[i][j] = Double.parseDouble(elmts[j]);
                }
                i++;
            }

            setMatrix(matrix);
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
        setupMlrMatrix();
    }

    public String getSolutionString() {
        return solution;
    }
}
