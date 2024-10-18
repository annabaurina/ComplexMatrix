import java.util.Scanner;

public class ComplexMatrix {
    private int rows;
    private int cols;
    private Complex[][] data;


    public ComplexMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex[rows][cols];
    }

    public ComplexMatrix(Complex[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = data;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isSquare() {
        return rows == cols;
    }

    //умножение на число
    public ComplexMatrix multiply(Complex c) {
        Complex[][] result = new Complex[this.getRows()][this.getCols()];

        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                result[i][j] = data[i][j].multiply(c);
            }
        }
        return new ComplexMatrix(result);
    }

    //деление на число
    public ComplexMatrix divide(Complex c) {
        Complex[][] result = new Complex[this.getRows()][this.getCols()];

        for (int i = 0; i < this.getRows(); i++) {
            for (int j = 0; j < this.getCols(); j++) {
                result[i][j] = data[i][j].divide(c);
            }
        }
        return new ComplexMatrix(result);
    }

    // сложение матриц
    public ComplexMatrix add(ComplexMatrix other) {
        if (this.rows != other.getRows() || this.cols != other.getCols()) { // нельзя сложить матрицы разного размера
            System.out.println(":(");
        }
        Complex[][] result = new Complex[this.rows][this.cols]; //массив комплексных
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result[i][j] = this.data[i][j].add(other.data[i][j]); //используем методы сложения для комплексных
            }
        }
        return new ComplexMatrix(result); //делаем из этого массива матрицу
    }

    // вычитание матриц
    public ComplexMatrix subtract(ComplexMatrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            System.out.println("(((((((((((((((");
        }
        Complex[][] result = new Complex[this.rows][this.cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result[i][j] = this.data[i][j].subtract(other.data[i][j]); //используем методы вычитания для комплексных
            }
        }
        return new ComplexMatrix(result);
    }

    //умножение
    public ComplexMatrix multiply(ComplexMatrix other) {
        if (this.cols != other.rows) {
            System.out.println("((((((((((((((("); // правило чтоб можно было умножить
        }
        Complex[][] result = new Complex[this.rows][other.cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                result[i][j] = new Complex(0, 0); // сюда результат будем класть
                for (int k = 0; k < this.cols; k++) {
                    result[i][j] = result[i][j].add(this.data[i][k].multiply(other.data[k][j]));
                }
            }
        }
        return new ComplexMatrix(result);
    }

    //транспонируем
    public ComplexMatrix transpose() {
        Complex[][] result = new Complex[cols][rows];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result[j][i] = this.data[i][j];
            }
        }
        return new ComplexMatrix(result);
    }

    //строковый формат (для вывода)
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.append(data[i][j].toString()).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    //определитель
    public Complex determinant() {
        if (!isSquare()) {
            System.out.println("(((((((((((("); //упс проблемс кент каунт детерминант
        }
        return determinant_for_arr(this.data); 

    }
    //приват а не паблик потому что извне определитель считать должны только для матрицы, а не для массива комплексных. этот метод нужен только для метода determinant()
    private Complex determinant_for_arr(Complex[][] matrix) { //отдельной функцией потому что нам рекурсивно нужно считать для алг доп
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0]; 
        }
        if (n == 2) {
            return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        }

        Complex det = new Complex(0, 0);
        for (int i = 0; i < n; i++) {
            det = det.add(matrix[0][i].multiply(determinant_for_arr(getMinor(matrix, 0, i))).multiply(new Complex(Math.pow(-1, i), 0)));
        }
        //чисто по формулам считаем (выглядит в одну строку страшно потому что в джаве нет перегрузки операторов(это обидно))
        return det;
    }
    //приват а не паблик потому что извне определитель считать должны только для матрицы, а не для массива комплексных. этот метод нужен только для метода determinant()
    //в целом можно и паблик сделать, но будем считать что для пользователя метод получения минора - не необходим (в тз же не было)
    private Complex[][] getMinor(Complex[][] matrix, int row, int col) {
        int n = matrix.length;
        Complex[][] minor = new Complex[n - 1][n - 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != row && j != col) {
                    minor[i < row ? i : i - 1][j < col ? j : j - 1] = matrix[i][j];
                }
            }
        }
        return minor;
    }

    
    //ищем обратную
    public ComplexMatrix inverse() {
        if (!isSquare()) { //обратные бывают только для квадратных
            System.err.println("ОШИБОЧКА");
        }
        int n = rows;
        Complex[][] tmp = new Complex[n][n*2]; //будем дописыванием единичной матрицы считать 

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tmp[i][j] = this.data[i][j]; //наша матрица лежит
                 //единичная матрица лежит рядом
                if (i == j) {
                    tmp[i][j + n] = new Complex(1, 0); 
                } else {
                    tmp[i][j + n] = new Complex(0, 0);
                }
            }
        }
    // гаусс
    for (int i = 0; i < n; i++) {
        Complex pivot = tmp[i][i];
        for (int j = 0; j < 2 * n; j++) {
            tmp[i][j] = tmp[i][j].divide(pivot);
        }
        for (int j = 0; j < n; j++) {
            if (j != i) {
                Complex cur = tmp[j][i];
                for (int k = 0; k < 2 * n; k++) {
                    tmp[j][k] = tmp[j][k].subtract(cur.multiply(tmp[i][k]));
                }
            }
        }
    }
        //обратную перекладываем в отдельный массив
        Complex[][] result = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = tmp[i][j + n];
            }
        }
        return new ComplexMatrix(result);
    }

    public ComplexMatrix scan_matrix(){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Complex arr[][] = new Complex[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int real = scanner.nextInt();
                int imaginary = scanner.nextInt();
                Complex tmp = new Complex(real, imaginary);
                arr[i][j] = tmp;
            }
        }
        ComplexMatrix x = new ComplexMatrix(arr);
        return x;

    }

    //тестирование того что все работает
    //все правда работает
    public static void main(String[] args) {
        
        ComplexMatrix tmp = new ComplexMatrix(0, 0);
        ComplexMatrix x = tmp.scan_matrix();

        /*
        System.err.println(x.toString());
        System.err.println(x.add(x).toString());
        System.err.println(x.subtract(x).toString());
        System.err.println(x.multiply(x).toString());
        System.err.println(x.transpose().toString());
        System.err.println(x.determinant().toString());
        */

        System.err.println(x.inverse().toString());

    }

}