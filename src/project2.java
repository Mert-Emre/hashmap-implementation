// This class is mainly used for reading the documents and passing the necessary informations to
// necessary classes.

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class project2 {
    public static void main(String[] args) {
        // Read first file and add employees to the bracnhes. Make a connection between
        // location class and helper class.
        // Helper class allows location class to write to the output file.
        try {
            WriteHelper helper = new WriteHelper(args[2]);
            Locations location = new Locations(helper);
            File initialFile = new File(args[0]);
            Scanner initialReader = new Scanner(initialFile);
            while (initialReader.hasNextLine()) {
                String employeeRaw = initialReader.nextLine();
                String[] employeeInfo = employeeRaw.split(",");
                String city = employeeInfo[0].trim();
                String dist = employeeInfo[1].trim();
                String name = employeeInfo[2].trim();
                String profession = employeeInfo[3].trim();
                location.addEmployee(city, dist, name, profession);
            }
            initialReader.close();
            File inputFile = new File(args[1]);
            Scanner inputReader = new Scanner(inputFile);
            // For each direction send employee informations to the location class.
            while (inputReader.hasNextLine()) {
                String directionRaw = inputReader.nextLine().trim();
                String[] directionInfo = directionRaw.split(":");
                if (directionInfo.length == 1) {
                    location.newMonth();
                    continue;
                }
                String direction = directionInfo[0];
                if (direction.equals("PRINT_MANAGER")) {
                    String[] memberInfo = directionInfo[1].trim().split(",");
                    String city = memberInfo[0].trim();
                    String dist = memberInfo[1].trim();
                    String manager = location.getManager(city, dist);
                    helper.write(String.format("Manager of the %s branch is %s.\n", dist, manager));
                } else if (direction.equals("ADD")) {
                    String[] memberInfo = directionInfo[1].trim().split(",");
                    String city = memberInfo[0].trim();
                    String dist = memberInfo[1].trim();
                    String name = memberInfo[2].trim();
                    String profession = memberInfo[3].trim();
                    location.addEmployee(city, dist, name, profession);
                } else if (direction.equals("PRINT_MONTHLY_BONUSES")) {
                    String[] distInfo = directionInfo[1].trim().split(",");
                    String city = distInfo[0].trim();
                    String dist = distInfo[1].trim();
                    int monthly = location.getMonthly(city, dist);
                    helper.write(String.format("Total bonuses for the %s branch this month are: %d\n", dist, monthly));
                } else if (direction.equals("PRINT_OVERALL_BONUSES")) {
                    String[] distInfo = directionInfo[1].trim().split(",");
                    String city = distInfo[0].trim();
                    String dist = distInfo[1].trim();
                    int total = location.getTotal(city, dist);
                    helper.write(String.format("Total bonuses for the %s branch are: %d\n", dist, total));
                } else if (direction.equals("LEAVE")) {
                    try {
                        String[] memberInfo = directionInfo[1].trim().split(",");
                        String city = memberInfo[0].trim();
                        String dist = memberInfo[1].trim();
                        String name = memberInfo[2].trim();
                        location.leave(city, dist, name);

                    } catch (RuntimeException e) {
                        helper.write("There is no such employee.\n");
                    }
                } else if (direction.equals("PERFORMANCE_UPDATE")) {
                    try {
                        String[] memberInfo = directionInfo[1].trim().split(",");
                        String city = memberInfo[0].trim();
                        String dist = memberInfo[1].trim();
                        String name = memberInfo[2].trim();
                        int bonus = Integer.parseInt(memberInfo[3].trim());
                        location.performanceUpdate(city, dist, name, bonus);
                    } catch (RuntimeException e) {
                        helper.write("There is no such employee.\n");
                    }
                }

            }
            inputReader.close();
            helper.close();
        } catch (FileNotFoundException e) {
            System.out.println("File has not found.");
        }
    }
}
