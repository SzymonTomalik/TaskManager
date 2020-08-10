package pl.coderslab;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    private static String[][] tasks;

    public static void main(String[] args) {
        try {
            tasks = readTasks();
        } catch (FileNotFoundException e) {
            System.out.println(ConsoleColors.RED + "File not found!!");
            System.exit(0);
        }
        Scanner scanner = new Scanner(System.in);
        options();
        while (scanner.hasNextLine()) {
            String choice = scanner.nextLine();
            switch (choice) {

                case "exit":
                    exit();
                    System.exit(0);
                    break;
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "list":
                    list();
                    break;
                case "options":
                    options();
                    break;
                default:
                    System.out.println(ConsoleColors.RED + "Select correct operations");
            }
            options();
        }
    }

    public static void options() {
        System.out.println(ConsoleColors.BLUE + "Please select an option" + ConsoleColors.RESET);
        System.out.println("add \nremove \nlist \nexit ");
    }

    public static String[][] readTasks() throws FileNotFoundException {
        File file = new File("taskData.csv");
        Scanner scanner = new Scanner(file);
        String[][] loadedData = new String[1][3];
        int counter = 0;
        while (scanner.hasNextLine()) {
            String[] split = scanner.nextLine().split(",");
            loadedData[counter] = split;
            counter++;
            loadedData = Arrays.copyOf(loadedData, loadedData.length + 1);
        }
        loadedData = Arrays.copyOf(loadedData, loadedData.length - 1);
        return loadedData;

    }

    public static void list() {

        for (int i = 0; i < tasks.length - 1; i++) {
            System.out.println(i + ":" + tasks[i][0] + ",   " + tasks[i][1] + ",    " + tasks[i][2]);
        }
    }

    static void add() {

        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add some task description");
        stringBuilder.append(scanner.nextLine() + ",");
        System.out.println("Please add task due date(format :yyyy-MM-dd)");
        stringBuilder.append(scanner.nextLine() + ",");
        System.out.println("Is your task is important:true/false");
        stringBuilder.append(scanner.nextLine());
        String[] split = stringBuilder.toString().split(",");
        if (isValidDAte(split[1]) && (split[2].equals("true") || split[2].equals("false"))) {
            tasks = Arrays.copyOf(tasks, tasks.length + 1);
            tasks[tasks.length - 1] = split;
        } else {
            System.out.println(ConsoleColors.RED + "Unsupported task format! Please try again");
        }
    }

    private static boolean isValidDAte(String s) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(s.trim());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    static void remove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter task number to delete");
        String str = scanner.nextLine();
        if (StringUtils.isNumeric(str)) {
            try {
                tasks = (String[][]) ArrayUtils.remove(tasks, Integer.parseInt(str));
                System.out.println(ConsoleColors.GREEN + "Task " + str + " deleted");
            } catch (IndexOutOfBoundsException ex) {
                System.out.println(ConsoleColors.RED + "Task's number doesn't exist");

            }
        } else {
            System.out.println(ConsoleColors.RED + "Task's numnber has been not entered, please try again");
        }
    }

    private static void exit() {
        Path path = Paths.get("taskData.csv");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < tasks.length; i++) {
            list.add(tasks[i][0] + "," + tasks[i][1] + "," + tasks[i][2]);
        }
        try {
            Files.write(path, list);
        } catch (IOException E) {
            System.out.println(ConsoleColors.RED + "File does not exist");
        }

    }

}