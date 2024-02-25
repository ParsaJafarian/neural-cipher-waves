package com.mnist;

import com.nn.Matrix;

import java.io.*;

public class MnistLoader  {

    public Matrix[][] load(String dataFilePath, String labelFilePath) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(dataFilePath)));
        int magicNumber = dataInputStream.readInt();
        int numberOfItems = dataInputStream.readInt();
        int rows = dataInputStream.readInt();
        int cols = dataInputStream.readInt();

        System.out.println("number of items is " + numberOfItems);
        System.out.println("number of rows is: " + rows);
        System.out.println("number of cols is: " + cols);

        DataInputStream labelInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(labelFilePath)));
        int labelMagicNumber = labelInputStream.readInt();
        int numberOfLabels = labelInputStream.readInt();

        System.out.println("number of labels is: " + numberOfLabels);

        Matrix[][] data = new Matrix[numberOfItems][2];

        assert numberOfItems == numberOfLabels;

        for(int i = 0; i < numberOfItems; i++) {
            //Set the label (y vector)
            int[][] yData = new int[10][1];
            int label = labelInputStream.readUnsignedByte();
            yData[label][0] = 1;
            Matrix y = new Matrix(yData);

            //Set the input (x vector)
            Matrix x = Matrix.zeros(rows, cols);
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    x.set(r, c, dataInputStream.readUnsignedByte());

            data[i][0] = x.flatten();
            data[i][1] = y;
        }
        dataInputStream.close();
        labelInputStream.close();
        return data;
    }
}
