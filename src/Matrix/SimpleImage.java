package Matrix;

import java.io.*;

import javax.imageio.ImageIO;

import Utils.Utils;

import java.awt.image.BufferedImage;

public class SimpleImage {
    private Matrix RGBMatrix[];
    private Matrix grayScaleMatrix;
    private Matrix matrixXI;
    private Matrix matrixX;
    private Matrix RGBMatrixHandleSides[];
    private Matrix grayScaleMatrixHandleSides;
    private int width;
    private int height;
    private Matrix matrixXInv;
    private Matrix matrixD;
    private ColorOptions color;
    private double factor;
    private String fileName;

    public enum ColorOptions {
        RED, GREEN, BLUE, GRAYSCALE, NORMAL
    }

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
                // System.out.printf("%d %d %d\n", redWeight, greenWeight, blueWeight);

                double grayScaleColor = 0.299 * redWeight + 0.587 * greenWeight + 0.114 * blueWeight;
                this.grayScaleMatrix.matrix[row][col] = grayScaleColor;
                this.grayScaleMatrixHandleSides.matrix[row + 1][col + 1] = grayScaleColor;
            }
        }

        this.handleSides();
        this.init();

    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public void setOptions(ColorOptions color) {
        this.color = color;
    }

    public Matrix getRedMatrix() {
        return this.RGBMatrix[0];
    }

    public Matrix getGreenMatrix() {
        return this.RGBMatrix[1];
    }

    public Matrix getBlueMatrix() {
        return this.RGBMatrix[2];
    }

    public Matrix getGrayscaleMatrix() {
        return this.grayScaleMatrix;
    }

    public void saveImage(Matrix m) throws IOException {
        BufferedImage theImage = new BufferedImage(m.col, m.row,
                color == ColorOptions.GRAYSCALE ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_INT_RGB);

        int numshift = 0;
        if (color == ColorOptions.RED)
            numshift = 16;
        else if (color == ColorOptions.GREEN)
            numshift = 8;

        for (int x = 0; x < m.row; x++) {
            for (int y = 0; y < m.col; y++) {
                theImage.setRGB(x, y, (int) m.matrix[x][y] << numshift);
            }
        }
        File outputfile = new File("test/output/gana.png");
        ImageIO.write(theImage, "png", outputfile);
    }

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

    public void EnlargeImage() throws IOException {
        Matrix enlargedMatrix = new Matrix();
        int numshift = 0;

        if (this.color == ColorOptions.GRAYSCALE) {
            enlargedMatrix = EnlargedMatrix(grayScaleMatrix, factor);
        } else if (this.color == ColorOptions.RED) {
            enlargedMatrix = EnlargedMatrix(RGBMatrix[0], factor);
            // enlargedMatrix.displayMatrix();

            numshift = 16;
        } else if (this.color == ColorOptions.GREEN) {
            enlargedMatrix = EnlargedMatrix(RGBMatrix[1], factor);
            // enlargedMatrix.displayMatrix();

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

            // enlargedMatrixB.displayMatrix();

            enlargedMatrix = new Matrix(enlargedMatrixB.row, enlargedMatrixB.col);

            for (int row = 0; row < enlargedMatrixB.row; row++) {
                for (int col = 0; col < enlargedMatrixB.col; col++) {
                    int RGB = (((int)(Math.round(enlargedMatrixR.matrix[row][col]))) << 16) |
                            (((int)(Math.round(enlargedMatrixG.matrix[row][col]))) << 8)
                            | (int)(Math.round(enlargedMatrixB.matrix[row][col]));
                    // System.out.printf("R: %f\n", (enlargedMatrixR.matrix[row][col]));
                    // System.out.printf("G: %x\n", ((int)(Math.round(enlargedMatrixG.matrix[row][col]))) << 8);
                    // System.out.printf("B: %x\n", (int)(Math.round(enlargedMatrixB.matrix[row][col])));
                    // System.out.printf("RGB: %x\n", RGB);
                    enlargedMatrix.matrix[row][col] = RGB;
                }
            }

        }

        System.out.println("NOW OUTPUT file");

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

    private Matrix EnlargedMatrix2(Matrix m, int factor) {
        Matrix resultMatrix = new Matrix(m.row * factor, m.col * factor);

        double incCol = (m.col - 1) / (double) (resultMatrix.col - 1);
        double incRow = (m.row - 1) / (double) (resultMatrix.row - 1);
        System.out.println(incCol);
        int usingX = 0;
        int usingY = 0;

        Matrix a = getMatrixA(usingX, usingY, color);
        int i = 0;
        for (double row = 0; row <= m.row - 2 + Utils.getTolerance(); row += incRow) {
            int j = 0;
            for (double col = 0; col <= m.col - 2 + Utils.getTolerance(); col += incCol) {
                int roundedRow = (int) Math.floor(row);
                int roundedCol = (int) Math.floor(col);

                if ((usingX != (roundedCol)) || (usingY != (roundedRow))) {
                    usingX = roundedCol;
                    usingY = roundedRow;
                    a = getMatrixA(roundedRow, roundedCol, color);
                }

                double color;
                if ((Math.abs(col - roundedCol) < 10e-3) || (Math.abs(row - roundedRow) < 10e-3))
                    color = m.matrix[(int) Math.round(row)][(int) Math.round(col)];
                else
                    color = interpolate(col - usingX, row - usingY, a);

                resultMatrix.matrix[i][j] = color;
                // System.out.printf("currenly in %d %d and color is %.4f \n", i, j, color);
                j++;
            }
            i++;
        }

        return resultMatrix;
    }

    private Matrix EnlargedMatrix(Matrix m, double factor) {
        int newRow = (int) Math.round(m.row * factor);
        int newCol = (int) Math.round(m.col * factor);
        System.out.println(newCol);
        int range1 = newRow / (m.row - 1);
        int range2 = newCol / (m.col - 1);
        int sisaX = newRow % (m.row - 1);
        int sisaY = newCol % (m.col - 1);
        Matrix resultMatrix = new Matrix(newRow, newCol);
        int x = 0;
        int y = 0;

        int i, j, k, l;
        for (i = 0; i < m.row - 1; i++) {
            int rangeX = range1;
            if (sisaX != 0) {
                rangeX = range1 + 1;
                sisaX--;
            }
            for (j = 0; j < m.col - 1; j++) {
                Matrix a = getMatrixA(j, i, color);
                int rangeY = range2;
                if (sisaY != 0) {
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
            }
            y = 0;
            sisaY = newCol % (m.col - 1);
            x += rangeX;
            // System.out.println(x);

        }

        return resultMatrix;
    }

    private void init() {
        // RAPIHIN !!!
        setMatrixX();
        setMatrixD();
        // this.matrixX.displayMatrix();
        Matrix cpy = this.matrixX.getCopyMatrix();
        this.matrixXInv = cpy.getInverse();
        // System.out.println(this.matrixXInv.getDeterminant());
        this.matrixXI = this.matrixXInv.multiplyBy(this.matrixD);
        // this.matrixX.displayMatrix();
        // this.matrixD.displayMatrix();
        // this.matrixXInv.displayMatrix();
        // System.exit(0);
    }

    // public void setMatrixF(int row, int col) {
    //     this.matrixF = new Matrix(16, 1);

    // }

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

    private void setMatrixD() {
        this.matrixD = new Matrix(16, 16);
        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int y2 = -1; y2 < 3; y2++) {
                    for (int x2 = -1; x2 < 3; x2++) {
                        // matrixD.matrix[row][col] = 4 * (x2 == x ? 1 : 0) * (y2 == y ? 1 : 0);
                        // matrixD.matrix[row + 4][col] = 2 * ((x2 == (x + 1)) ? 1 : 0) * (y2 == (y) ? 1 : 0) - 2 * ((x2 == (x - 1) ? 1 : 0) * (y2 == y ? 1 : 0));
                        // matrixD.matrix[row + 8][col] = 2 * ((x2 == x ? 1 : 0) * (y2 == (y + 1) ? 1 : 0))- 2 * ((x2 == x ? 1 : 0) * (y2 == (y - 1) ? 1 : 0));
                        // matrixD.matrix[row + 12][col] = (((x2 == (x - 1)) && (y2 == (y - 1))) ? 1 : 0)+ (((x2 == (x + 1)) && (y2 == (y + 1))) ? 1 : 0)- (((x2 == (x + 1)) && (y2 == (y - 1))) ? 1 : 0)- (((x2 == (x - 1)) && (y2 == (y + 1))) ? 1 : 0);
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
        // this.matrixD.MultiplyByConst(4).displayMatrix();
    }

    public Matrix getMatrixD() {
        return this.matrixD;

    }

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

    private double interpolate(double x, double y, Matrix a) {
        double res = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                // System.out.printf("x is %.4f\n", x);
                // System.out.printf("y is %.4f\n", y);
                res += a.matrix[j * 4 + i][0] * Math.pow(x, i) * Math.pow(y, j);
            }
        }
        // System.out.println(res);
        // a.displayMatrix();
        return res;
    }

}
