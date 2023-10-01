package Matrix;

import java.io.*;

import javax.imageio.ImageIO;

import Utils.Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;


public class SimpleImage {
    private Matrix RGBMatrix[];
    private Matrix grayScaleMatrix;
    private Matrix matrixF;
    private Matrix matrixX;
    private Matrix RGBMatrixHandleSides[];
    private Matrix grayScaleMatrixHandleSides;
    private int width;
    private int height;
    private Matrix matrixXInv;
    


    public SimpleImage (String fileName) throws IOException {
        
        BufferedImage img = ImageIO.read(new File("test/input/"+ fileName));
        this.width = img.getWidth();
        this.height = img.getHeight();

        
        this.RGBMatrix = new Matrix[3];
        this.RGBMatrixHandleSides = new Matrix[3];
        
        this.RGBMatrix[0] = new Matrix(height, width);
        this.RGBMatrix[1] = new Matrix(height, width);
        this.RGBMatrix[2] = new Matrix(height, width);
        this.grayScaleMatrix = new Matrix(height, width);

        this.RGBMatrixHandleSides[0] = new Matrix(width+2, height+2);
        this.RGBMatrixHandleSides[1] = new Matrix(width+2, height+2);
        this.RGBMatrixHandleSides[2] = new Matrix(width+2, height+2);
        this.grayScaleMatrixHandleSides = new Matrix(width+2, height+2);

        for (int row = 0; row < height; row++ )
        {
            for (int col = 0; col < width; col++)
            {
                int color = img.getRGB(row, col);

                int redWeight = (color & 0xff0000) >> 16;
                this.RGBMatrix[0].matrix[row][col] = redWeight;
                this.RGBMatrixHandleSides[0].matrix[row+1][col+1] = redWeight;


                int greenWeight = (color & 0xff00) >> 8;
                this.RGBMatrix[1].matrix[row][col] = greenWeight;
                this.RGBMatrixHandleSides[1].matrix[row+1][col+1] = greenWeight;

                int blueWeight = color & 0xff;
                this.RGBMatrix[2].matrix[row][col] = blueWeight;
                this.RGBMatrixHandleSides[2].matrix[row+1][col+1] = blueWeight;


                double grayScaleColor = 0.299*redWeight + 0.587*greenWeight + 0.114*blueWeight;
                this.grayScaleMatrix.matrix[row][col] = grayScaleColor;
                this.grayScaleMatrixHandleSides.matrix[row+1][col+1] = grayScaleColor;
            }
        }

        this.handleSides();

        

    }

    private void handleSides() {
        int row, col;
        this.grayScaleMatrixHandleSides.matrix[0][0] = this.grayScaleMatrixHandleSides.matrix[1][1]; 
        this.grayScaleMatrixHandleSides.matrix[height+1][width+1] = this.grayScaleMatrixHandleSides.matrix[height][width]; 
        this.grayScaleMatrixHandleSides.matrix[height+1][0] = this.grayScaleMatrixHandleSides.matrix[height][1]; 
        this.grayScaleMatrixHandleSides.matrix[0][width+1] = this.grayScaleMatrixHandleSides.matrix[1][width]; 

        this.RGBMatrixHandleSides[0].matrix[0][0] = this.RGBMatrixHandleSides[0].matrix[1][1]; 
        this.RGBMatrixHandleSides[0].matrix[height+1][width+1] = this.RGBMatrixHandleSides[0].matrix[height][width]; 
        this.RGBMatrixHandleSides[0].matrix[height+1][0] = this.RGBMatrixHandleSides[0].matrix[height][1]; 
        this.RGBMatrixHandleSides[0].matrix[0][width+1] = this.RGBMatrixHandleSides[0].matrix[1][width]; 

        this.RGBMatrixHandleSides[1].matrix[0][0] = this.RGBMatrixHandleSides[1].matrix[1][1]; 
        this.RGBMatrixHandleSides[1].matrix[height+1][width+1] = this.RGBMatrixHandleSides[1].matrix[height][width]; 
        this.RGBMatrixHandleSides[1].matrix[height+1][0] = this.RGBMatrixHandleSides[1].matrix[height][1]; 
        this.RGBMatrixHandleSides[1].matrix[0][width+1] = this.RGBMatrixHandleSides[1].matrix[1][width]; 

        this.RGBMatrixHandleSides[2].matrix[0][0] = this.RGBMatrixHandleSides[2].matrix[1][1]; 
        this.RGBMatrixHandleSides[2].matrix[height+1][width+1] = this.RGBMatrixHandleSides[2].matrix[height][width]; 
        this.RGBMatrixHandleSides[2].matrix[height+1][0] = this.RGBMatrixHandleSides[2].matrix[height][1]; 
        this.RGBMatrixHandleSides[2].matrix[0][width+1] = this.RGBMatrixHandleSides[2].matrix[1][width]; 
        
        row = 0;
        for (col = 1; col < width+1; col++ )
        {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row+1][col];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row+1][col];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row+1][col];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row+1][col];
        }

        row = height+1;
        for (col = 1; col < width+1; col++ )
        {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row-1][col];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row-1][col];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row-1][col];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row-1][col];
        }

        col = 0;
        for (row = 1; row < height+1; row++)
        {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row][col+1];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row][col+1];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row][col+1];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row][col+1];
        }

        col = width+1;
        for (row = 1; row < height+1; row++)
        {
            this.RGBMatrixHandleSides[0].matrix[row][col] = this.RGBMatrixHandleSides[0].matrix[row][col-1];
            this.RGBMatrixHandleSides[1].matrix[row][col] = this.RGBMatrixHandleSides[1].matrix[row][col-1];
            this.RGBMatrixHandleSides[2].matrix[row][col] = this.RGBMatrixHandleSides[2].matrix[row][col-1];
            this.grayScaleMatrixHandleSides.matrix[row][col] = this.grayScaleMatrixHandleSides.matrix[row][col-1];
        }
    }

    public void EnlargeImage() {

    }

    private Matrix EnlargeMatrix(Matrix m, int factor) {
        Matrix resultMatrix = new Matrix(m.row*factor,m.col*factor );

        double incCol=(resultMatrix.col/m.col);
        double incRow = (resultMatrix.row/m.row);
        int usingX = 0;
        int usingY = 0;

        //get matrix a masih grayscale
        Matrix a = getMatrixA(usingX, usingY);


        int i = 0;
        for (double row = 0; row <= resultMatrix.row + Utils.getTolerance(); row += incRow)
        {
            int j = 0;
            for (double col = 0; col <= resultMatrix.col + Utils.getTolerance(); col += incCol)
            {
                int roundedRow = (int) Math.round(row);
                int roundedCol = (int) Math.round(col);

                if ((usingX != (roundedCol)) || (usingY != (roundedCol))) {
                    usingX = roundedCol;
                    usingY = roundedRow;
                    a = getMatrixA(roundedRow, roundedCol);
                }

                resultMatrix.matrix[i][j] = interpolate(col-usingX, row-usingY, a, usingX, usingY);
                j++;
            }
            i++;
        }




        return resultMatrix;
    }

    private void init() {
        this.matrixX = new Matrix(16, 16);

        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int j = 0; j < 4; j++){
                    for (int i = 0; i < 4; i++) {
                        matrixX.matrix[row][col] = Math.pow(x, i) * Math.pow(y, j);
                        matrixX.matrix[row + 4][col] = (i == 0) ? 0 : i * Math.pow(x, i - 1) * Math.pow(y, j);
                        matrixX.matrix[row + 8][col] = (j == 0) ? 0 : j * Math.pow(x, i) * Math.pow(y, j - 1);
                        matrixX.matrix[row + 12][col] = (i == 0 || j == 0) ? 0 : i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1);
                        col++;
                    }
                }
                row++;
            }
        }

        this.matrixXInv = this.matrixX.getInverse();

    }

    public void setMatrixF(int row, int col) {
        this.matrixF = new Matrix(16, 1);

    }

    public void setMatrixX() {
        this.matrixX = new Matrix(16, 16);

        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int j = 0; j < 4; j++){
                    for (int i = 0; i < 4; i++) {
                        matrixX.matrix[row][col] = Math.pow(x, i) * Math.pow(y, j);
                        matrixX.matrix[row + 4][col] = (i == 0) ? 0 : i * Math.pow(x, i - 1) * Math.pow(y, j);
                        matrixX.matrix[row + 8][col] = (j == 0) ? 0 : j * Math.pow(x, i) * Math.pow(y, j - 1);
                        matrixX.matrix[row + 12][col] = (i == 0 || j == 0) ? 0 : i * j * Math.pow(x, i - 1) * Math.pow(y, j - 1);
                        col++;
                    }
                }
                row++;
            }
        }

        
    }

    public Matrix getMatrixD() {
        Matrix matrixD = new Matrix(16, 16);
        int row = 0;
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                int col = 0;
                for (int y2 = -1; y2 < 3;y2++){
                    for (int x2 = -1; x2 < 3; x2++) {
                        matrixD.matrix[row][col] = 4*(x2 == x ?1 : 0)*(y2 == y? 1: 0);
                        matrixD.matrix[row + 4][col] = 2*((x2==(x+1))?1:0)*(y2==(y)?1:0) - 2*((x2==(x-1)?1:0)*(y2 == y?1:0));
                        matrixD.matrix[row + 8][col] = 2*((x2 == x?1:0)*(y2==(y+1)?1:0)) - 2*((x2==x?1:0)*(y2 == (y-1)?1:0));
                        matrixD.matrix[row + 12][col] =(((x2 == (x-1))&&(y2 == (y-1))) ? 1: 0) + (((x2 == (x+1))&&(y2 == (y+1))) ? 1: 0) - (((x2 == (x+1))&&(y2 == (y-1))) ? 1: 0) - (((x2 == (x-1))&&(y2 == (y+1))) ? 1: 0) ;
                        col++;
                    }
                }
                row++;
            }
        }

        return matrixD;

    }

    private Matrix getMatrixA(int tlx, int tly){
        Matrix d = getMatrixD();
        Matrix i = new Matrix(16, 1);

        int cnt = 0;
        for (int y = -1; y < 3; y++)
        {
            for (int x = -1; x < 3; x++)
            {
                i.matrix[cnt][0] = this.grayScaleMatrixHandleSides.matrix[tlx-y][tlx-x];
            }
        }

        Matrix a = matrixXInv.multiplyBy(d).multiplyBy(i);
        return a;
    }

    private double interpolate(double x, double y, Matrix a, int usingX, int usingY)
    {
        int res = 0;
        for (int j = 0; j < 4; j++ )
        {
            for (int i = 0; i < 4; i++)
            {
                res += a.matrix[j*4 + i][0];
            }
        }
        return res;
    }



    
}
