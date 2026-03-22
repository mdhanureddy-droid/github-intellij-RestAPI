package files;

import java.io.IOException;
import java.util.ArrayList;

public class DataDrivenSampleReview {

    public static void main(String[] args) throws IOException {
        DataDrivenReview d = new DataDrivenReview();
        ArrayList data = d.getData("Add Profile", "testdata");
        System.out.println(data.get(0));
        System.out.println(data.get(1));
        System.out.println(data.get(2));
        System.out.println(data.get(3));
    }

}
