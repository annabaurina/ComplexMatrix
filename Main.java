package src;

public class Main {

    public static void main(String[] args) {

        ComplexMatrix tmp = new ComplexMatrix(0, 0);
        ComplexMatrix x = tmp.scan_matrix();

        System.err.println(x.toString());
        System.err.println(x.add(x).toString());
        System.err.println(x.subtract(x).toString());
        System.err.println(x.multiply(x).toString());
        System.err.println(x.transpose().toString());
        System.err.println(x.determinant().toString());
        System.err.println(x.inverse().toString());

    }
}