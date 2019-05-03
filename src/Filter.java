import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Filter {


    static Path resources = Paths.get("C:\\Users\\christiaand\\IdeaProjects\\19-05-03-Test_IO\\src\\resources");

    static Path unsortedFolder = resources.resolve("unsortedFolder");
    static Path sortedFolder = resources.resolve("sortedFolder");
    static Path summary = sortedFolder.resolve("summary");
    static Path summaryTxt = summary.resolve("summary.txt");

    static List<String> summaryList = new ArrayList<>();


    public static void main(String... args) {
        File[] files = new File(String.valueOf(unsortedFolder)).listFiles();
        copyFiles(files);

        files = new File(String.valueOf(sortedFolder)).listFiles();

        //summaryList.add("name    |   writable    |  readable   |\n");
        summaryList.add("name                |      readable       |      writeable      |\n");
        //"%-20s|%11c          |%11c          |"
        createSummary(files);
        try {
            Files.createDirectories(summary);
            for (var s : summaryList)
                System.out.println(s);

            Files.write(summaryTxt, summaryList, Charset.forName("UTF-8"), StandardOpenOption.CREATE);
        } catch (IOException se) {
            //handle it
        }
    }


    public static void copyFiles(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                copyFiles(file.listFiles()); // Calls same method again.
            } else {
                System.out.print("File: " + file.getName() + " is " + (Files.isWritable(file.toPath()) ? "" : "not") + " writable");
                System.out.print(" -----  hidden ? " + file.isHidden());
                String extension = "";

                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    //System.out.println("i = "+i);
                    extension = file.getName().substring(i + 1);
                    //extension = FilenameUtils.getExtension(file.getPath());
                } else // empty name
                    extension = file.getName().substring(1);
                ;
                System.out.println(",  and extension = " + extension);
                Path newPath = sortedFolder.resolve(extension);
                //new File(sortedFolder.getFileName()).mkdirs();
                //sortedFolder
                try {
                    //if (Files.notExists()) {
                    // create the dir
                    //new File(newPath).mkdirs();
                    Files.createDirectories(newPath);
                    // copy file to sortedFolder
                    Path newFile = newPath.resolve(file.getName());
                    // REPLACE_EXISTING ??
                    Files.copy(file.toPath(), newFile, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException se) {
                    //handle it
                }
            }
        }

    }

    public static void createSummary(File[] files) {
        for (File file : files) {
            if (file.isDirectory()) {
                if (!file.getPath().equals(String.valueOf(summary))) {
                    //System.out.println("Directory: " + file.getName());
                    summaryList.add(file.getName() + ":");
                    summaryList.add("-----\n");
                    createSummary(file.listFiles()); // Calls same method again.
                    summaryList.add("-----");
                }

            } else { // file
                //summaryList.add(file.getName()+"   |    "+(Files.isWritable(file.toPath()) ? "x" : "")+"   |    "+(Files.isReadable(file.toPath()) ? "x" : ""));
                summaryList.add(String.format("%-20s|%11c          |%11c          |", file.getName(),
                        (Files.isWritable(file.toPath()) ? 'x' : ' '),
                        (Files.isReadable(file.toPath()) ? 'x' : ' ')
                ));
            }
        }
    }

}


