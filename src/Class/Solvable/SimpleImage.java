package Class.Solvable;

import java.io.*;

import javax.imageio.ImageIO;

import Class.Matrix;

import java.awt.image.BufferedImage;

//class untuk memperbesar suatu foto
public class SimpleImage {
    /* ***** ATRIBUTE ***** */

    //matriks yang menyimpan nilai RGB dari suatu foto (index 0 RED, index 1 GREEN, index 2 BLUE)
    private Matrix RGBMatrix[];

    //matriks yang menyimpan nilai grayscale dari suatu foto
    private Matrix grayScaleMatrix;

    //matrix hasil perkalian matrix I dan matrix X inverse
    private Matrix matrixXI;

    //matrix x yang merupakan koefisien dari a (bicubic spline interpolation)
    private Matrix matrixX;

    //matrix dengan ukuran panjang dan lebar ditambah 2 untuk mempermudah saat perhitungan interpolasi
    private Matrix RGBMatrixHandleSides[];
    private Matrix grayScaleMatrixHandleSides;

    //lebar foto
    private int width;

    //tinggi foto
    private int height;

    //balikan dari matrix x
    private Matrix matrixXInv;

    //matrix D yang merupakan koefisien dalam persamaan I, Ix, Iy, dan Ixy
    private Matrix matrixD;

    //pilihan warna untuk foto yang akan diperbesar
    private ColorOptions color;

    //berapa kali perbesaran foto yang diinginkan
    private double factor;

    //nama file dari foto yang akan diperbesar
    private String fileName;

    //enum untuk pilihan warna yang tersedia
    public enum ColorOptions {
        RED, GREEN, BLUE, GRAYSCALE, NORMAL
    }

    /* ***** FUNGSI DAN PROSEDUR ***** */
    //konstruktor untuk menginisialisasi objeck SimpleImage
    public SimpleImage(String fileName) throws IOException {
        this.fileName = fileName;
        BufferedImage img = ImageIO.read(new File("test/input/" + fileName));
        this.width = img.getWidth();
        this.height = img.getHeight();

        this.RGBMatrix = new Matrix[3];
        this.RGBMatrixHandleSides = new Matrix[3];

        this.RGBMatrix[0] = new Matrix(height, width);
        this.RGBMatrix[1] = new Matrix(height, width);
        this.RGBMatrix[2] = new Matrix(height, width);
        this.grayScaleMatrix = new Matrix(height, width);

        this.RGBMatrixHandleSides[0] = new Matrix(height + 2, width + 2);
        this.RGBMatrixHandleSides[1] = new Matrix(height + 2, width + 2);
        this.RGBMatrixHandleSides[2] = new Matrix(height + 2, width + 2);
        this.grayScaleMatrixHandleSides = new Matrix(height + 2, width + 2);

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int color = img.getRGB(col, row);

                int redWeight = ((color & 0xff0000) >> 16);
                this.RGBMatrix[0].matrix[row][col] = redWeight;
                this.RGBMatrixHandleSides[0].matrix[row + 1][col + 1] = redWeight;

                int greenWeight = ((color & 0xff00) >> 8);
                this.RGBMatrix[1].matrix[row][col] = greenWeight;
                this.RGBMatrixHandleSides[1].matrix[row + 1][col + 1] = greenWeight;

                int blueWeight = color & 0xff;
                this.RGBMatrix[2].matrix[row][col] = blueWeight;
                this.RGBMatrixHandleSides[2].matrix[row + 1][col + 1] = blueWeight;

                double grayScaleColor = 0.299 * redWeight + 0.587 * greenWeight + 0.114 * blueWeight;
                this.grayScaleMatrix.matrix[row][col] = grayScaleColor;
                this.grayScaleMatrixHandleSides.matrix[row + 1][col + 1] = grayScaleColor;
            }
        }

        this.handleSides();
        this.init();

    }

    //setter factor
    public void setFactor(double factor) {
        this.factor = factor;
    }

    //setter colorOptions
    public void setOptions(ColorOptions color) {
        this.color = color;
    }

    //getter RGBMatrix untuk warna merah
    public Matrix getRedMatrix() {
        return this.RGBMatrix[0];
    }

    //getter RGBMatrix untuk warna hijau
    public Matrix getGreenMatrix() {
        return this.RGBMatrix[1];
    }

    //getter RGBMatrix untuk warna biru
    public Matrix getBlueMatrix() {
        return this.RGBMatrix[2];
    }

    //getter grayScaleMatrix
    public Matrix getGrayscaleMatrix() {
        return this.grayScaleMatrix;
    }


    // public void saveImage(Matrix m) throws IOException {
    //     BufferedImage theImage = new BufferedImage(m.col, m.row,
    //             color == ColorOptions.GRAYSCALE ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);

    //     int numshift = 0;
    //     if (color == ColorOptions.RED)
    //         numshift = 16;
    //     else if (color == ColorOptions.GREEN)
    //         numshift = 8;

    //     for (int x = 0; x < m.row; x++) {
    //         for (int y = 0; y < m.col; y++) {
    //             theImage.setRGB(x, y, (int) m.matrix[x][y] << numshift);
    //         }
    //     }
    //     File outputfile = new File("test/output/gana.png");
    //     ImageIO.write(theImage, "png", outputfile);
    // }

    //prosedure untuk menginisialisasi matrix handle sides
    private void handleSides() {
        int row, col;
        this.grayScaleMatrixHandleSides.matrix[0][0] = this.grayScaleMatrixHandleSides.matrix[1][1];
        this.grayScaleMatrixHandleSides.matrix[height + 1][width
                + 1] = this.grayScaleMatrixHandleSides.matrix[height][width];
        this.grayScaleMatrixHandleSides.matrix[height + 1][0] = this.grayScaleMatrixHandleSides.matrix[height][1];
        this.grayScaleMatrixHandleSides.matrix[0][width + 1] = this.grayScaleMatrixHandleSides.matrix[1][width];

        this.RGBMatrixHandleSides[0].matrix[0][0] = this.RGBMatrixHandleSides[0].matrix[1][1];
        this.RGBMatrixHandleSides[0].matrix[height + 1][width + 1] = this.RGBMatrixHandleSides[0].matrix[height][width];
        this.RGBMatrixHandleSides[0].matrix[height + 1][0] = this.RGBMatrixHandleSides[0].matrix[height][1];
        this.RGBMatrixHandleSides[0].matrix[0][width + 1] = this.RGBMatrixHandleSides[0].matrix[1][width];

        this.RGBMatrixHandleSides[1].matrix[0][0] = this.RGBMatrixHandleSides[1].matrix[1][1];
        this.RGBMatrixHandleSides[1].matrix[height + 1][width + 1] = this.RGBMatrixHandleSides[1].matrix[height][width];
        this.RGBMatrixHandleSides[1].matrix[height + 1][0] = this.RGBMatrixHandleSides[1].matrix[height][1];
        this.RGBMatrixHandleSides[1].matrix[0][width + 1] = this.RGBMatrixHandleSides[1].matrix[1][width];

        this.RGBMatrixHandleSides[2].matrix[0][0] = this.RGBMatrixHandleSides[2].matrix[1][1];
        this.RGBMatrixHandleSides[2].matrix[height + 1][width + 1] = this.RGBMatrixHandleSides[2].matrix[height][width];
        this.RGBMatrixHandleSides[2].matrix[height + 1][0] = this.RGBMatrixHandleSides[2].matrix[height][1];
        this.RGBMatrixHandleSides[2].matrix[0][width + 1] = this.RGBMatrixHandleSides[2].matrix[1][width];

        row = 0;
        for (col = 1; col < width + 1; col++) {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row + 1][col];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row + 1][col];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row + 1][col];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row + 1][col];
        }

        row = height + 1;
        for (col = 1; col < width + 1; col++) {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row - 1][col];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row - 1][col];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row - 1][col];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row - 1][col];
        }

        col = 0;
        for (row = 1; row < height + 1; row++) {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row][col + 1];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row][col + 1];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row][col + 1];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row][col + 1];
        }

        col = width + 1;
        for (row = 1; row < height + 1; row++) {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row][col - 1];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row][col - 1];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row][col - 1];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row][col - 1];
        }
    }

    //prosedure untuk memulai proses perbesaran foto hasil dari prosedur ini adalah output file foto yang sudah diperbesar
    //nama file foto yang sudah diperbesar akan menjadi 'enlarge<filename>'
    public void EnlargeImage() throws IOException {
        Matrix enlargedMatrix = new Matrix();
        int numshift = 0;

        if (this.color == ColorOptions.GRAYSCALE) {
            enlargedMatrix = EnlargedMatrix(grayScaleMatrix, factor);
        } else if (this.color == ColorOptions.RED) {
            enlargedMatrix = EnlargedMatrix(RGBMatrix[0], factor);

            numshift = 16;
        } else if (this.color == ColorOptions.GREEN) {
            enlargedMatrix = EnlargedMatrix(RGBMatrix[1], factor);

            numshift = 8;
        } else if (this.color == ColorOptions.BLUE) {
            enlargedMatrix = EnlargedMatrix(RGBMatrix[2], factor);
        } else {
            Matrix enlargedMatrixR = new Matrix();
            Matrix enlargedMatrixG = new Matrix();
            Matrix enlargedMatrixB = new Matrix();
            this.color = ColorOptions.RED;
            enlargedMatrixR = EnlargedMatrix(RGBMatrix[0], factor);
            this.color = ColorOptions.GREEN;
            enlargedMatrixG = EnlargedMatrix(RGBMatrix[1], factor);
            this.color = ColorOptions.BLUE;
            enlargedMatrixB = EnlargedMatrix(RGBMatrix[2], factor);
            this.color = ColorOptions.NORMAL;


            enlargedMatrix = new Matrix(enlargedMatrixB.row, enlargedMatrixB.col);

            for (int row = 0; row < enlargedMatrixB.row; row++) {
                for (int col = 0; col < enlargedMatrixB.col; col++) {
                    int RGB = (((int)(Math.round(enlargedMatrixR.matrix[row][col]))) << 16) |
                            (((int)(Math.round(enlargedMatrixG.matrix[row][col]))) << 8)
                            | (int)(Math.round(enlargedMatrixB.matrix[row][col]));
                    enlargedMatrix.matrix[row][col] = RGB;
                }
            }

        }

        BufferedImage theImage;
        if (color == ColorOptions.GRAYSCALE) {
            theImage = new BufferedImage(enlargedMatrix.col, enlargedMatrix.row, BufferedImage.TYPE_BYTE_GRAY);
            for (int y = 0; y < enlargedMatrix.row; y++) {
                for (int x = 0; x < enlargedMatrix.col; x++) {
                    theImage.setRGB(x, y, ((int) Math.round(enlargedMatrix.matrix[y][x])));
                }
            }
        
        } else if (color == ColorOptions.NORMAL){
            theImage = new BufferedImage(enlargedMatrix.col, enlargedMatrix.row, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < enlargedMatrix.row; x++) {
                for (int y = 0; y < enlargedMatrix.col; y++) {
                    theImage.setRGB(y, x, ((int) Math.round(enlargedMatrix.matrix[x][y])));
                }
            }
        }else {
            theImage = new BufferedImage(enlargedMatrix.col, enlargedMatrix.row, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < enlargedMatrix.row; x++) {
                for (int y = 0; y < enlargedMatrix.col; y++) {
                    theImage.setRGB(y, x, ((int) Math.round(enlargedMatrix.matrix[x][y])) << numshift);
                }
            }
        }

        File outputfile = new File("test/output/enlarge" + fileName);
        ImageIO.write(theImage, "png", outputfile);
    }

    //fungsi yang digunakan untuk memperbesar suatu matrix yang merepresentasikan sebuah foto
    private Matrix EnlargedMatrix(Matrix m, double factor) {
        int newRow = (int) Math.round(m.row * factor);
        int newCol = (int) Math.round(m.col * factor);
        int range1 = newRow / (m.row - 1);
        int range2 = newCol / (m.col - 1);
        int sisaX = newRow % (m.row - 1);
        int sisaY = newCol % (m.col - 1);
        Matrix resultMatrix = new Matrix(newRow, newCol);
        int x = 0;
        int y = 0;

        boolean d = true;
        boolean e = true;

        int i, j, k, l;
        for (i = 0; i < m.row - 1; i++) {
            int rangeX = range1;
            if (sisaX != 0 && d) {
                rangeX = range1 + 1;
                sisaX--;
            }
            for (j = 0; j < m.col - 1; j++) {
                Matrix a = getMatrixA(j, i, color);
                int rangeY = range2;
                if (sisaY != 0 && e) {
                    rangeY = range2 + 1;
                    sisaY--;
                }
                double gapX = 1.0 / (double) (rangeX - 1);
                double gapY = 1.0 / (double) (rangeY - 1);
                for (k = 0; k < rangeX; k++) {
                    for (l = 0; l < rangeY; l++) {
                        resultMatrix.matrix[x + k][y + l] = interpolate(l * gapY, k * gapX, a);

                    }
                }
                y += rangeY;
                e = !e;
            }
            y = 0;
            sisaY = newCol % (m.col - 1);
            x += rangeX;
            d = !d;

        }

        return resultMatrix;
    }

    //prosedure untuk membantu proses inisialisasi objek SimpleImage
    private void init() {
        setMatrixX();
        setMatrixD();
        Matrix cpy = this.matrixX.getCopyMatrix();
        this.matrixXInv = cpy.getInverse();
        this.matrixXI = this.matrixXInv.multiplyBy(this.matrixD);
    }


    //prosedur untuk meng-generate matrix x
    public void setMatrixX() {
        this.matrixX = new Matrix(16, 16);

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

    //fungsi untuk meng-generate matrix D
    private void setMatrixD() {
        this.matrixD = new Matrix(16, 16);
        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int y2 = -1; y2 < 3; y2++) {
                    for (int x2 = -1; x2 < 3; x2++) {
                        matrixD.matrix[row][col] = (x == x2 && y == y2) ? 1 : 0;
                        matrixD.matrix[row + 4][col] = (y2 == y && (x2 == x + 1 || x2 == x-1) ? 0.5 : 0) * (y2 == y && (x2 == x-1) ? -1: 1);
                        matrixD.matrix[row + 8][col] = (x2 == x && (y2 == y + 1 || y2 == y-1) ? 0.5 : 0) * (x2 == x && (y2 == y-1) ? -1: 1);
                        matrixD.matrix[row + 12][col] = ((((x2 == x + 1) && (y2 == y + 1)) || (x2 == x - 1 && y2 == y) || ( x2 == x && y2 == y - 1) || (x2 == x && y2 == y)) ? 0.25 : 0) * (((x2 == x - 1 && y2 == y) || ( x2 == x && y2 == y - 1)) ? -1 : 1);
                        col++;
                    }
                }
                row++;
            }
        }
    }

    //getter matrixD
    public Matrix getMatrixD() {
        return this.matrixD;

    }

    //fungsi untuk mendapatkan nilai a pada sebuah sistem persamaan interpolasi bicubic spline
    //nilai a akan disimpan dalam bentuk matrix
    //tlx dan tly merupakan koordinat pada foto untuk nilai I(0,0)
    private Matrix getMatrixA(int tlx, int tly, ColorOptions color) {
        // TLX, TLY WILL BE 0 <= tlx, tly <= (width, height) -2
        Matrix i = new Matrix(16, 1);

        int cnt = 0;
        for (int y = -1; y < 3; y++) {
            for (int x = -1; x < 3; x++) {
                if (color == ColorOptions.RED)
                    i.matrix[cnt][0] = this.RGBMatrixHandleSides[0].matrix[tly + x + 1][tlx + y + 1];
                if (color == ColorOptions.GREEN)
                    i.matrix[cnt][0] = this.RGBMatrixHandleSides[1].matrix[tly + x + 1][tlx + y + 1];
                if (color == ColorOptions.BLUE)
                    i.matrix[cnt][0] = this.RGBMatrixHandleSides[2].matrix[tly + x + 1][tlx + y + 1];
                if (color == ColorOptions.GRAYSCALE)
                    i.matrix[cnt][0] = this.grayScaleMatrixHandleSides.matrix[tly + x + 1][tlx + y + 1];
                cnt++;
            }
        }

       
        Matrix a = this.matrixXI.multiplyBy(i);
        return a;
    }

    //fungsi untuk menghitung aproksimasi nilai f(x,y) dengan nilai a berasal dari matrix a yang dikirimkan lewat parameter
    private double interpolate(double x, double y, Matrix a) {
        double res = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                res += a.matrix[j * 4 + i][0] * Math.pow(x, i) * Math.pow(y, j);
            }
        }
        return res;
    }

}
