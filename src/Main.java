import api.*;
import impl.StubGpsNavigator;

import java.util.Scanner;


public class Main {

    public static void main(String[] args){
        String defaultPath = "your file path here";
        String defaultPointA = "A";
        String defaultPointB = "C";

        String userPath;
        String userPointA;
        String userPointB;

        String filePath;
        String pointA;
        String pointB;

        final GpsNavigator navigator = new StubGpsNavigator();

        /* Dialogue with the user */
        Scanner in = new Scanner(System.in);
        System.out.println("Input path to the file or press Enter to use default path: ");
        userPath = in.nextLine();
        if (userPath.equals(""))
            filePath = defaultPath;
        else
            filePath = userPath;

        System.out.println("Input start point or press Enter to use default point: ");
        userPointA = in.nextLine();
        if (userPointA.equals(""))
            pointA = defaultPointA;
        else
            pointA = userPointA;

        System.out.println("Input end point or press Enter to use default point: ");
        userPointB = in.nextLine();
        if (userPointB.equals(""))
            pointB = defaultPointB;
        else
            pointB = userPointB;
        /* ======================= */

        navigator.readData(filePath);

        try {
            final Path path = navigator.findPath(pointA, pointB);
            System.out.println(path.toString());
        } catch (Exception exc) {}
    }
}